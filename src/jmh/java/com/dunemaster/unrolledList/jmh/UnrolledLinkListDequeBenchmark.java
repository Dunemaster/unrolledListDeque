package com.dunemaster.unrolledList.jmh;

import com.dunemaster.unrolleddeque.UnrolledLinkedListDeque;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.LinkedList;

@State(Scope.Benchmark)
@Fork(1)
public class UnrolledLinkListDequeBenchmark {

    public static final int OBJECTS_TO_ADD = 100_000;
    public static final int WARMUP_ITERATIONS = 6;
    private final Object objectToAdd = new Object();

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAdd() {
        UnrolledLinkedListDeque<Object> unrolledList = new UnrolledLinkedListDeque<>(256);
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            unrolledList.add(objectToAdd);
        }
        if (unrolledList.size() != OBJECTS_TO_ADD) {
            throw new IllegalStateException("List size is not correct");
        }
    }



    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddArrayList() {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            list.add(objectToAdd);
        }
        if (list.size() != OBJECTS_TO_ADD) {
            throw new IllegalStateException("List size is not correct");
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddArrayListPreallocated() {
        ArrayList<Object> list = new ArrayList<>(OBJECTS_TO_ADD);
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            list.add(objectToAdd);
        }
        if (list.size() != OBJECTS_TO_ADD) {
            throw new IllegalStateException("List size is not correct");
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddLinkedList() {
        LinkedList<Object> list = new LinkedList<>();
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            list.add(objectToAdd);
        }
        if (list.size() != OBJECTS_TO_ADD) {
            throw new IllegalStateException("List size is not correct");
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAdd1024() {
        UnrolledLinkedListDeque<Object> unrolledList = new UnrolledLinkedListDeque<>(1024);
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            unrolledList.add(objectToAdd);
        }
        if (unrolledList.size() != OBJECTS_TO_ADD) {
            throw new IllegalStateException("List size is not correct");
        }
    }

}