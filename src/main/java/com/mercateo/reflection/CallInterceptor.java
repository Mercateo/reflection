package com.mercateo.reflection;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

class CallInterceptor<T> implements MethodInterceptor {

    private static final Map<Method, Function<CallInterceptor<?>, ?>> PASS_THROUGHS = new HashMap<>();
    static {
        try {
            PASS_THROUGHS.put(InvocationRecorder.class.getMethod("getInvocationRecordingResult"),
                    interceptor -> new Call<>(interceptor.invokedClass, interceptor.method,
                            interceptor.args));

            PASS_THROUGHS.put(Object.class.getMethod("toString"), interceptor -> "CallInterceptor("
                    + interceptor.clazz.getSimpleName() + ")");
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("error initializing passThrough map", e);
        }
    }

    private final Class<T> clazz;

    private Method method;

    private Object[] args;

    private Class<T> invokedClass;

    CallInterceptor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {

        if (PASS_THROUGHS.containsKey(method)) {
            return PASS_THROUGHS.get(method).apply(this);
        }

        this.method = method;
        this.args = args;
        this.invokedClass = clazz;

        return bogusReturn(method.getReturnType());
    }

    @SuppressWarnings("boxing")
    private Object bogusReturn(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType.equals(byte.class)) {
                return (byte) 0;
            } else if (returnType.equals(short.class)) {
                return (short) 0;
            } else if (returnType.equals(int.class)) {
                return 0;
            } else if (returnType.equals(long.class)) {
                return (long) 0;
            } else if (returnType.equals(float.class)) {
                return (float) 0;
            } else if (returnType.equals(double.class)) {
                return (double) 0;
            } else if (returnType.equals(boolean.class)) {
                return false;
            } else if (returnType.equals(char.class)) {
                return (char) 0;
            } else if (returnType.equals(void.class)) {
                return null;
            }
            throw new IllegalArgumentException();
        } else {
            return null;
        }
    }

    public interface InvocationRecorder<T> {
        Call<T> getInvocationRecordingResult();
    }
}
