package com.dunemaster.unrolledList.jmh;

import com.dunemaster.unrolleddeque.UnrolledLinkedListDeque;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayDeque;
import java.util.LinkedList;

@State(Scope.Benchmark)
@Fork(1)
public class UnrolledLinkListDequeAddAndRemoveTwoSideBenchmark {

    public static final int INBENCHMARK_ITERATIONS = 10_000;
    public static final  int ADD_BATCH_SIZE = 800;
    public static final int WARMUP_ITERATIONS = 6;
    private final Object objectToAdd = new Object();

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddRemove() {
        UnrolledLinkedListDeque<Object> unrolledList = new UnrolledLinkedListDeque<>(256);
        for (int i = 0; i < INBENCHMARK_ITERATIONS; i++) {

            for (int j = 0; j < ADD_BATCH_SIZE; j++) {
                unrolledList.addFirst(objectToAdd);
                unrolledList.addLast(objectToAdd);
            }

            for (int j = 0; j < ADD_BATCH_SIZE; j++) {
                Object obj1 = unrolledList.removeFirst();
                Object obj2 =unrolledList.removeLast();
                if (obj1 != obj2) {
                    throw new IllegalStateException();
                }
            }
        }

    }



    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddRemoveArrayDeque() {
        ArrayDeque<Object> deque = new ArrayDeque<>();
        for (int i = 0; i < INBENCHMARK_ITERATIONS; i++) {

            for (int j = 0; j < ADD_BATCH_SIZE; j++) {
                deque.addFirst(objectToAdd);
                deque.addLast(objectToAdd);
            }

            for (int j = 0; j < ADD_BATCH_SIZE; j++) {
                Object obj1 = deque.removeFirst();
                Object obj2 = deque.removeLast();
                if (obj1 != obj2) {
                    throw new IllegalStateException();
                }
            }
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddRemoveArrayDequePreallocated() {
        ArrayDeque<Object> deque = new ArrayDeque<>(ADD_BATCH_SIZE * 2);
        for (int i = 0; i < INBENCHMARK_ITERATIONS; i++) {

            for (int j = 0; j < ADD_BATCH_SIZE; j++) {
                deque.addFirst(objectToAdd);
                deque.addLast(objectToAdd);
            }

            for (int j = 0; j < ADD_BATCH_SIZE; j++) {
                Object obj1 = deque.removeFirst();
                Object obj2 = deque.removeLast();
                if (obj1 != obj2) {
                    throw new IllegalStateException();
                }
            }
        }
    }

    @Benchmark
    @Warmup(iterations = WARMUP_ITERATIONS)
    public void benchmarkAddRemoveLinkedList() {
        LinkedList<Object> list = new LinkedList<>();
        for (int i = 0; i < INBENCHMARK_ITERATIONS; i++) {

            for (int j = 0; j < ADD_BATCH_SIZE; j++) {
                list.addFirst(objectToAdd);
                list.addLast(objectToAdd);
            }

            for (int j = 0; j < ADD_BATCH_SIZE; j++) {
                Object obj1 = list.removeFirst();
                Object obj2 =list.removeLast();
                if (obj1 != obj2) {
                    throw new IllegalStateException();
                }
            }
        }
    }

}