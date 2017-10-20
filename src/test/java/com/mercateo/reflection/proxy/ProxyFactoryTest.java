package com.mercateo.reflection.proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.mercateo.reflection.Call;
import com.mercateo.reflection.CallInterceptor;

@RunWith(MockitoJUnitRunner.class)
public class ProxyFactoryTest {

    private TestClass proxy;

    private CallInterceptor<TestClass> interceptor;

    @Before
    public void setUp() {
        interceptor = new CallInterceptor<>(TestClass.class);

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

        final Call<TestClass> call = interceptor.getInvocationRecordingResult();

        assertThat(call.declaringClass()).isEqualTo(TestClass.class);
        assertThat(call.method()).isEqualTo(TestClass.class.getDeclaredMethod("testMethod", String.class));
        assertThat(call.args()).containsExactly("bar");
    }

    @Test
    public void proxyDoesNotRecordCallsToObjectMethods() throws Throwable {
        proxy.toString();

        final Call<TestClass> call = interceptor.getInvocationRecordingResult();
        assertThat(call.method()).isNull();
    }

    public static class TestClass {
        TestClass delegate = mock(TestClass.class);

        void testMethod(String name) {
            delegate.testMethod(name);
        }
    }

    @Test
    public void failsWithFinalMethod() throws Exception {
        assertThatThrownBy( //
                () -> ProxyFactory.createProxy(FailingTestClass.class, new CallInterceptor<>(FailingTestClass.class)) //
        )
            .isInstanceOf(IllegalStateException.class) //
            .hasMessage(
                    "The proxied class candidate com.mercateo.reflection.proxy.ProxyFactoryTest$FailingTestClass contains a final method 'test'");
    }

    public static class FailingTestClass {
        final public void test() {
        }
    }

    @Test
    public void skipsStaticMethod() throws Exception {
        ProxyFactory.createProxy(NotFailingTestClass.class, new CallInterceptor<>(NotFailingTestClass.class));
    }

    public static class NotFailingTestClass {
        static void test() {
        }
    }

}