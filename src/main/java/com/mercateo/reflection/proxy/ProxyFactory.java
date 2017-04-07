package com.mercateo.reflection.proxy;

import static net.bytebuddy.description.modifier.Visibility.PRIVATE;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;
import org.omg.PortableInterceptor.Interceptor;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;

public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> clazz, InvocationHandler invocationHandler, Class<?>... interfaces) {
        checkClassForFinalPublicMethods(clazz);
        try {
            final Class<? extends T> loaded = new ByteBuddy()
                .subclass(clazz)
                .implement(interfaces)
                .method(not(isDeclaredBy(Object.class)))
                .intercept(InvocationHandlerAdapter.of(invocationHandler))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();

            Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
            ObjectInstantiator thingyInstantiator = objenesis.getInstantiatorOf(loaded);
            return (T) thingyInstantiator.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error creating proxy for class " + clazz.getSimpleName(), e);
        }
    }

    private static void checkClassForFinalPublicMethods(Class<?> ct) {
        int classModifiers = ct.getModifiers();
        if (Modifier.isFinal(classModifiers)) {
            throw new IllegalStateException("The proxied class is not allowed to be final!");

        }
        Method[] methods = ct.getMethods();
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            if (Modifier.isFinal(modifiers) && !method.getDeclaringClass().equals(Object.class)) {
                throw new IllegalStateException("The proxied class does not have to have any final public method!");
            }
        }
    }

}
