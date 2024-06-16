package com.dunemaster.unrolledList.jmh;

import com.dunemaster.unrolledlist.UnrolledLinkList;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.LinkedList;

@State(Scope.Benchmark)
@Fork(1)
public class UnrolledLinkListBenchmark {

    public static final int OBJECTS_TO_ADD = 100_000;
    public static final int WARMUP_ITERATIONS = 4;
    private final Object objectToAdd = new Object();

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAdd() {
        UnrolledLinkList unrolledList = new UnrolledLinkList<>(128);
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            unrolledList.add(objectToAdd);
        }
    }
    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddArrayList() {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            list.add(objectToAdd);
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddArrayListPreallocated() {
        ArrayList<Object> list = new ArrayList<>(OBJECTS_TO_ADD);
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            list.add(objectToAdd);
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddLinkedList() {
        LinkedList<Object> list = new LinkedList<>();
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            list.add(objectToAdd);
        }
    }

}