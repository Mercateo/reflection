package com.mercateo.reflection.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> clazz, Callback callback, Class<?>... interfaces) {
        checkClassForFinalPublicMethods(clazz);
        try {
            return (T) Enhancer.create(clazz, interfaces, callback);
        } catch (Exception e) {
            throw new RuntimeException("Error creating proxy for class " + clazz.getSimpleName(),
                    e);
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
                throw new IllegalStateException(
                        "The proxied class does not have to have any final public method!");
            }
        }
    }

}
