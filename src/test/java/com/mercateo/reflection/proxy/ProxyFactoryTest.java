package com.mercateo.reflection.proxy;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.reflect.InvocationHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProxyFactoryTest {

    private TestClass proxy;

    @Mock
    private InvocationHandler interceptor;

    @Before
    public void setUp() {
        proxy = ProxyFactory.createProxy(TestClass.class, interceptor);
    }

    @Test
    public void realMethodShouldNotBeCalled() {
        proxy.delegate = mock(TestClass.class);
        proxy.testMethod("foo");

        verify(proxy.delegate, never()).testMethod(anyString());
    }

    @Test
    public void proxyRecordsArguementsUsedForCall() throws Throwable {
        proxy.testMethod("bar");

        verify(interceptor).invoke(eq(proxy), eq(TestClass.class.getDeclaredMethod("testMethod", String.class)), eq(
                new Object[] { "bar" }));
    }

    @Test
    public void proxyDoesNotRecordCallsToObjectMethods() throws Throwable {
        proxy.toString();

        verifyZeroInteractions(interceptor);
    }

    public static class TestClass {

        TestClass delegate = mock(TestClass.class);

        void testMethod(String name) {
            delegate.testMethod(name);
        }
    }

}