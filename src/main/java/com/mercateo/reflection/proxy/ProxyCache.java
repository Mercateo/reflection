package com.mercateo.reflection.proxy;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.integration.CacheLoader;

public class ProxyCache {

    private final AtomicInteger counter = new AtomicInteger();

    private final Cache<Class, Object> cache;

    public ProxyCache() {
        cache = new Cache2kBuilder<Class, Object>() {
        }.name("proxyCache_" + counter.incrementAndGet()).loader(new CacheLoader<Class, Object>() {
            @Override
            public Object load(Class aClass) {
                return ProxyFactory.createProxy(aClass);
            }
        }).expireAfterWrite(1, TimeUnit.HOURS).entryCapacity(1000).build();
    }

    public <T> T createProxy(Class<T> clazz) {
        // noinspection unchecked
        return (T) cache.get(clazz);
    }

}
