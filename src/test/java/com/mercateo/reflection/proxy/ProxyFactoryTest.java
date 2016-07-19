package com.mercateo.reflection.proxy;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.mercateo.reflection.proxy.ProxyFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@RunWith(MockitoJUnitRunner.class)
public class ProxyFactoryTest {

    private TestClass proxy;

    @Mock
    private MethodInterceptor interceptor;

    @Before
    public void setUp() {
        proxy = ProxyFactory.createProxy(TestClass.class, interceptor);
    }

    @Test
    public void realMethodShouldNotBeCalled() {
        proxy.testMethod("foo");

        verify(proxy.delegate, never()).testMethod(anyString());
    }

    @Test
    public void proxyRecordsArguementsUsedForCall() throws Throwable {
        proxy.testMethod("bar");

        verify(interceptor).intercept(eq(proxy), eq(TestClass.class.getDeclaredMethod("testMethod",
                String.class)), eq(new Object[] { "bar" }), any(MethodProxy.class));
    }

    public static class TestClass {

        final TestClass delegate = mock(TestClass.class);

        void testMethod(String name) {
            delegate.testMethod(name);
        }
    }

}