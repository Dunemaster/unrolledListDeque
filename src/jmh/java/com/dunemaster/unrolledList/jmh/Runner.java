package com.dunemaster.unrolledList.jmh;

import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Runner {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(UnrolledLinkListDequeAddBenchmark.class.getSimpleName())
                .include(UnrolledLinkListDequeAddTwoSideBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
