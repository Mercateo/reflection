package com.mercateo.reflection;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import com.mercateo.reflection.proxy.ProxyCache;

public class Call<T> {

    private final static ProxyCache proxyCache = new ProxyCache();

    private final Class<T> clazz;

    private final Method method;

    private final Object[] args;

    public Call(Class<T> clazz, Method method, Object[] args) {
        this.clazz = clazz;
        this.method = method;
        this.args = args;
    }

    public static <T> Call<T> of(Class<T> clazz, Consumer<T> invocation) {
        final T proxy = proxyCache.createProxy(clazz);

        synchronized (proxy) {
            invocation.accept(proxy);
            return ((InvocationRecorder<T>) proxy).getInvocationRecordingResult();
        }
    }

    public static <T> Method methodOf(Class<T> clazz, Consumer<T> invocation) {
        return of(clazz, invocation).method();
    }

    public Class<T> declaringClass() {
        return clazz;
    }

    public Method method() {
        return method;
    }

    public Object[] args() {
        return args;
    }
}
