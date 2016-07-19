package com.mercateo.reflection;


import com.mercateo.reflection.Call;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class CallTest {

    static class TestClass {
        public Integer testMethod(String name, Long counter) {
            return 42;
        }
    }

    @Test
    public void callRefExample() throws NoSuchMethodException {
        final Call<TestClass> call = Call.of(TestClass.class, c -> c.testMethod("foo", 1234l));

        assertThat(call.declaringClass()).isEqualTo(TestClass.class);
        final Method testMethod = TestClass.class.getDeclaredMethod("testMethod", String.class, Long.class);
        assertThat(call.method()).isEqualTo(testMethod);
        assertThat(call.args()).containsExactly("foo", 1234l);
    }

    @Test
    public void methodRefExample() throws NoSuchMethodException {
        final Method method = Call.methodOf(TestClass.class, c -> c.testMethod("foo", 1234l));

        final Method testMethod = TestClass.class.getDeclaredMethod("testMethod", String.class, Long.class);
        assertThat(method).isEqualTo(testMethod);
    }

}