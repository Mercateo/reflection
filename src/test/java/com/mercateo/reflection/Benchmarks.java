package com.mercateo.reflection;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.mercateo.reflection.proxy.ProxyFactory;

public class Benchmarks {

    static class Test {
        String foo(String bar) {
            return bar + "foo";
        }
    }

    static Test proxy = createProxy();

    @Benchmark
    public static Test createProxy() {
        return ProxyFactory.createProxy(Test.class);
    }

    @Benchmark
    public static void interceptCall() {
        proxy.foo("bar");

        ((InvocationRecorder<Test>) proxy).getInvocationRecordingResult();
    }

    public static void main(String[] args) throws RunnerException, InterruptedException {
        Options opt = new OptionsBuilder().warmupIterations(10).measurementIterations(10).forks(1).build();

        new Runner(opt).run();
    }
}
