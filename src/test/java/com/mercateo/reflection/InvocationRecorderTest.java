package com.mercateo.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

import com.mercateo.reflection.CallInterceptor.InvocationRecorder;
import com.mercateo.reflection.proxy.ProxyFactory;

public class InvocationRecorderTest {

    @Test
    public void testPrimitveMethod() throws NoSuchMethodException, SecurityException {
        A a = ProxyFactory.createProxy(A.class);
        assertThat(a.primitiveReturn()).isEqualTo(0);
        Method method = ((InvocationRecorder) a).getInvocationRecordingResult().method();
        assertThat(method).isEqualTo(A.class.getMethod("primitiveReturn"));
    }

    @Test
    public void testObjectMethod() throws NoSuchMethodException, SecurityException {
        A a = ProxyFactory.createProxy(A.class);
        assertThat(a.objectReturn()).isNull();
        Method method = ((InvocationRecorder) a).getInvocationRecordingResult().method();
        assertThat(method).isEqualTo(A.class.getMethod("objectReturn"));
    }

    @Test(expected = IllegalStateException.class)
    public void testFinalClassShouldBeRejected() {
        ProxyFactory.createProxy(C.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testFinalMethodsShouldBeRejected() {
        ProxyFactory.createProxy(B.class);
    }

    @Test
    public void testPrimitiveReturnTypes() {
        final PrimitiveReturnTypes proxy = ProxyFactory.createProxy(PrimitiveReturnTypes.class);

        assertThat(proxy.getByte()).isEqualTo((byte) 0);
        assertThat(proxy.getShort()).isEqualTo((short) 0);
        assertThat(proxy.getInt()).isEqualTo(0);
        assertThat(proxy.getLong()).isEqualTo(0L);
        assertThat(proxy.getFloat()).isEqualTo(0.0f);
        assertThat(proxy.getDouble()).isEqualTo(0.0);
        assertThat(proxy.getBoolean()).isEqualTo(false);
        assertThat(proxy.getChar()).isEqualTo('\0');
        proxy.getVoid();
    }

    @Test
    public void shouldCreateProxyFromClassWithoutDefaultConstructor() {
        final WithoutDefaultConstructor proxy = ProxyFactory.createProxy(WithoutDefaultConstructor.class);

        assertThat(proxy).isNotNull();
    }

    public static

    class A {
        public int primitiveReturn() {
            return 1;
        }

        public String

                objectReturn() {
            return "in method";
        }
    }

    public static

    class B {
        public final int primitiveReturn() {
            return 1;
        }

        public String

                objectReturn() {
            return "in method";
        }
    }

    public static

    final class C {
        public int primitiveReturn() {
            return 1;
        }

        public String

                objectReturn() {
            return "in method";
        }
    }

    public static

    class WithoutDefaultConstructor {
        public WithoutDefaultConstructor(String foo) {
        }
    }

    public static

    class PrimitiveReturnTypes {
        public byte getByte() {
            return 5;
        }

        public short getShort() {
            return 6;
        }

        public int getInt() {
            return 7;
        }

        public long getLong() {
            return 8;
        }

        public float getFloat() {
            return 3.14f;
        }

        public double getDouble() {
            return 3.1415;
        }

        public boolean getBoolean() {
            return true;
        }

        public char getChar() {
            return 't';
        }

        public void getVoid() {
        }
    }

}