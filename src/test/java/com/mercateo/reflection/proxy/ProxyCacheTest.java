package com.mercateo.reflection.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class ProxyCacheTest {

    private ProxyCache uut;

    @Before
    public void setUp() throws Exception {
        uut = new ProxyCache();
    }

    @Test
    public void cachesProxyObjects() {
        final ProxyFactoryTest.TestClass result1 = uut.createProxy(ProxyFactoryTest.TestClass.class);
        final ProxyFactoryTest.TestClass result2 = uut.createProxy(ProxyFactoryTest.TestClass.class);

        assertThat(result1).isSameAs(result2);
    }

    @Test
    public void doesNotCacheProxyObjectsFromDifferentCaches() {
        final ProxyCache uut2 = new ProxyCache();

        final ProxyFactoryTest.TestClass result1 = uut.createProxy(ProxyFactoryTest.TestClass.class);
        final ProxyFactoryTest.TestClass result2 = uut2.createProxy(ProxyFactoryTest.TestClass.class);

        assertThat(result1).isNotSameAs(result2);
    }
}