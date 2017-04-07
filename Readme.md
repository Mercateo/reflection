# com.mercateo.reflection

## ProxyFactory &mdash; create an intercepting proxy

```
    MethodInterceptor interceptor = ...
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