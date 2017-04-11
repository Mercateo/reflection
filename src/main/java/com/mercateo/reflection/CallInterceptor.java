package com.mercateo.reflection;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class CallInterceptor<T> implements InvocationRecorder<T> {

    private Method method;

    private Object[] args;

    private Class<T> invokedClass;

    public CallInterceptor(Class<T> clazz) {
    }

    @RuntimeType
    public Object invoke(@SuperCall Callable<?> zuper, @AllArguments Object[] args, @Origin Method method,
            @Origin Class clazz) {

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

    public Call<T> getInvocationRecordingResult() {
        return new Call<T>(invokedClass, method, args);
    }
}
