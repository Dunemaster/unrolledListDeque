package com.dunemaster.unrolledList.jmh;

import com.dunemaster.unrolleddeque.UnrolledLinkedListDeque;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

@State(Scope.Benchmark)
@Fork(1)
public class UnrolledLinkListDequeAddTwoSideBenchmark {

    public static final int OBJECTS_TO_ADD = 100_000;
    public static final int WARMUP_ITERATIONS = 6;
    private final Object objectToAdd = new Object();

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAdd() {
        UnrolledLinkedListDeque<Object> unrolledList = new UnrolledLinkedListDeque<>(256);
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            unrolledList.add(objectToAdd);
            unrolledList.push(objectToAdd);
        }
        if (unrolledList.size() != OBJECTS_TO_ADD * 2) {
            throw new IllegalStateException("List size is not correct");
        }
    }



    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddArrayDeque() {
        ArrayDeque<Object> deque = new ArrayDeque<>();
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            deque.add(objectToAdd);
            deque.push(objectToAdd);
        }
        if (deque.size() != OBJECTS_TO_ADD * 2) {
            throw new IllegalStateException("List size is not correct");
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddArrayDequePreallocated() {
        ArrayDeque<Object> deque = new ArrayDeque<>(OBJECTS_TO_ADD * 2);
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            deque.add(objectToAdd);
            deque.push(objectToAdd);
        }
        if (deque.size() != OBJECTS_TO_ADD * 2) {
            throw new IllegalStateException("List size is not correct");
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddLinkedList() {
        LinkedList<Object> list = new LinkedList<>();
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            list.add(objectToAdd);
            list.push(objectToAdd);
        }
        if (list.size() != OBJECTS_TO_ADD*2) {
            throw new IllegalStateException("List size is not correct");
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAdd1024() {
        UnrolledLinkedListDeque<Object> unrolledList = new UnrolledLinkedListDeque<>(1024);
        for (int i = 0; i < OBJECTS_TO_ADD; i++) {
            unrolledList.add(objectToAdd);
            unrolledList.push(objectToAdd);
        }
        if (unrolledList.size() != OBJECTS_TO_ADD*2) {
            throw new IllegalStateException("List size is not correct");
        }
    }

}