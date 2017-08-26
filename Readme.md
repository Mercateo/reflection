[![Build Status](https://travis-ci.org/Mercateo/reflection.svg?branch=master)](https://travis-ci.org/Mercateo/reflection)
[![Coverage Status](https://coveralls.io/repos/github/Mercateo/reflection/badge.svg?branch=master)](https://coveralls.io/github/Mercateo/reflection?branch=master)
[![MavenCentral](https://img.shields.io/maven-central/v/com.mercateo/reflection.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.mercateo%22%20AND%20a%3A%22reflection%22)


# com.mercateo.reflection

## ProxyFactory &mdash; create an intercepting proxy

```
    InvocationHandler interceptor = ...
    TestClass proxy = ProxyFactory.createProxy(TestClass.class, interceptor);
```

## Call &mdash; A type safe variant for examination of method calls

Given an arbitrary class
```
class TestClass {
    public Integer testMethod(String name, Long counter) {
        return 42;
    }
}
```

Call can record a given method call

```
Method method = Call.of(TestClass.class, c -> c.testMethod("foo", 1234l)).method();
```

and returns all relevant parameters like

  * Method
  * Call Arguments
  * Object of Method

There is an additional shortcut to get the called method with Call.methodOf(...):
```
Method method = Call.methodOf(TestClass.class, c -> c.testMethod(null, 0));
```

Which replaces using reflection to get the method:

```
Method method = TestClass.class.getDeclaredMethod("testMethod", String.class, Long.class);
```

The latter can throw a NoSuchMethodException. Using Call.of(...)/Call.methodOf(...) with an unknown method just leads to a compile error.