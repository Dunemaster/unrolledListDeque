package com.dunemaster.unrolleddeque;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class UnrolledLinkedListDeque<E> implements Deque<E> {

    private static final int DEFAULT_BLOCK_SIZE = 128;
    private final int blockSize;
    private final int center;
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private int indexInHeadBlock;
    private int indexInTailBlock;

    private static final class Node<E> {

        private final E[] elements;
        private Node<E> next;
        private Node<E> prev;

        @SuppressWarnings("unchecked")
        Node(int blockSize) {
            elements = (E[]) new Object[blockSize];
        }

    }

    public UnrolledLinkedListDeque() {
        this(DEFAULT_BLOCK_SIZE);
    }

    public UnrolledLinkedListDeque(int blockSize) {
        this.blockSize = blockSize;
        this.center = blockSize / 2 - 1;
        validateBlockSize();
        head = new Node<>(blockSize);
        setToClearState();
    }

    private void validateBlockSize() {
        if (blockSize <= 0) throw new IllegalArgumentException("blockSize must be positive");
        if (blockSize % 2 != 0) throw new IllegalArgumentException("blockSize must be even");
    }

    private void setToClearState() {
        tail = head;
        size = 0;
        indexInHeadBlock = center + 1;
        indexInTailBlock = center;
    }

    @Override
    public void addFirst(E e) {
        if (!tryAddFirst(e))
            throw new IllegalStateException();
    }

    @Override
    public void addLast(E e) {
        tryAddLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        return tryAddFirst(e);
    }

    @Override
    public boolean offerLast(E e) {
        tryAddLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        throwIfEmpty();
        return tryRemoveFirst();
    }

    @Override
    public E removeLast() {
        throwIfEmpty();
        return tryRemoveLast();
    }

    @Override
    public E pollFirst() {
        return tryRemoveFirst();
    }

    @Override
    public E pollLast() {
        return tryRemoveLast();
    }

    @Override
    public E getFirst() {
        throwIfEmpty();
        return tryGetFirst();
    }

    @Override
    public E getLast() {
        throwIfEmpty();
        return tryGetLast();
    }

    @Override
    public E peekFirst() {
        return tryGetFirst();
    }

    @Override
    public E peekLast() {
        return tryGetLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        return tryAddLast(e);
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return tryGetFirst();
    }

    @Override
    public boolean addAll(Collection<? extends E> elements) {
        boolean anyAdded = false;
        for (E element : elements) {
            boolean added = tryAddLast(element);
            anyAdded = anyAdded || added;
        }
        return anyAdded;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Node<E> currentBlock = head;
            private int indexInCurrentBlock = indexInHeadBlock;
            private int remaining = size;

            @Override
            public boolean hasNext() {
                return remaining > 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = (E) currentBlock.elements[indexInCurrentBlock];
                indexInCurrentBlock++;
                remaining--;
                if (indexInCurrentBlock == blockSize && remaining > 0) {
                    currentBlock = currentBlock.next;
                    indexInCurrentBlock = 0;
                }
                return element;
            }
        };
    }

    @Override
    public E[] toArray() {
        //TODO
        return (E[])new Object[size];
    }

      @Override
    public <T> T[] toArray(T[] ts) {
        //TODO
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> currentBlock = tail;
            private int indexInCurrentBlock = indexInTailBlock;
            private int remaining = size;

            @Override
            public boolean hasNext() {
                return remaining > 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                E element = currentBlock.elements[indexInCurrentBlock];
                indexInCurrentBlock--;
                remaining--;
                if (indexInCurrentBlock < 0 && remaining > 0) {
                    currentBlock = currentBlock.prev;
                    indexInCurrentBlock = blockSize - 1;
                }
                return element;
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        head = new Node<>(blockSize);
        setToClearState();
    }

    private E tryGetFirst() {
        if (size == 0) {
            return null;
        }
        return (E) head.elements[indexInHeadBlock];
    }

    private E tryGetLast() {
        if (size == 0) {
            return null;
        }
        return (E) tail.elements[indexInTailBlock];
    }

    private boolean tryAddFirst(E element) {
        indexInHeadBlock--;
        if (indexInHeadBlock < 0) {
            Node<E> newNode = new Node<>(blockSize);
            if (size != 0) { // Only link nodes if the deque is not empty
                newNode.next = head;
                head.prev = newNode;
            }
            head = newNode;
            indexInHeadBlock = blockSize - 1;
        }
        head.elements[indexInHeadBlock] = element;
        size++;
        return true;
    }

    private boolean tryAddLast(E element) {
        indexInTailBlock++;
        if (indexInTailBlock == blockSize) {
            Node<E> newNode = new Node<>(blockSize);
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            indexInTailBlock = 0;
        }
        tail.elements[indexInTailBlock] = element;
        size++;
        return true;
    }

    private E tryRemoveFirst() {
        if (size == 0) {
            return null;
        }
        // Adjust for the circular nature of the deque
        int effectiveIndex = indexInHeadBlock;
        E element = (E) head.elements[effectiveIndex];
        head.elements[effectiveIndex] = null; // Help GC
        indexInHeadBlock++;
        if (indexInHeadBlock == blockSize) {
            if (head != tail) {
                head = head.next;
                head.prev = null;
            }
            indexInHeadBlock = 0;
        }
        size--;
        if (size == 0) {
            setToClearState();
        }
        return element;
    }

    private E tryRemoveLast() {
        if (size == 0) {
            return null;
        }

        E element = (E) tail.elements[indexInTailBlock];
        // releasing memory!
        tail.elements[indexInTailBlock] = null;
        indexInTailBlock--;
        size--;
        if (indexInTailBlock < 0) {
            // remove the last node
            if (head == tail) {
                if (size == 0) {
                    setToClearState();
                }
            } else {
                Node<E> prev = tail.prev;
                prev.next = null;
                tail = tail.prev;
                indexInTailBlock = blockSize - 1;
            }
        }

        return element;
    }

    private void throwIfEmpty() {
        if (size == 0)
            throw new NoSuchElementException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        if (elements.isEmpty()) {
            return true;
        }
        if (elements.size() == 1) {
            return contains(elements.iterator().next());
        }
        Set<Object> testSet = elements instanceof Set ? (Set)elements : new HashSet<>(elements);
        for (Object dequeElement : this) {
            if (testSet.contains(dequeElement)) {
                testSet.remove(dequeElement);
            }
        }
        return testSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        for (Object object : this) {
            if (object.equals(o)) {
                return true;
            }
        }
        return false;
    }

}