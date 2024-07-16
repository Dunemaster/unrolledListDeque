package com.dunemaster.unrolleddeque;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnrolledLinkedListDequeTest {

    @Test
    public void testEmptyList() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        assertNull(list.pollFirst());
        assertNull(list.pollLast());
    }

    @Test
    public void testClearList() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(5);
        list.add(6);
        list.pollLast();
        list.pollLast();

        // Act
        list.clear();
        // Assert
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        assertNull(list.pollFirst());
        assertNull(list.pollLast());
        list.add(1);
        list.add(2);
        list.add(4);
        list.add(5);
        list.add(6);
        assertEquals(6, list.pollLast());
        assertEquals(5, list.pollLast());
    }

    @Test
    public void testAdd() {
        // test inside block size
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<Integer>(6);
        testAddAddLast(list, UnrolledLinkedListDeque::add);
        UnrolledLinkedListDeque<Integer> list2 = new UnrolledLinkedListDeque<Integer>(6);
        testAddAddLast(list2, UnrolledLinkedListDeque::addLast);
    }

    private void testAddAddLast(UnrolledLinkedListDeque<Integer> list,
                                BiConsumer<UnrolledLinkedListDeque<Integer>, Integer> action) {
        action.accept(list, 1);
        assertEquals(1, list.size());
        action.accept(list,2);
        assertEquals(2, list.size());
        action.accept(list,3);
        assertEquals(3, list.size());

        // test exceed (half)block size
        action.accept(list,4);
        assertEquals(4, list.size());
        action.accept(list,5);
        assertEquals(5, list.size());
    }


    @Test
    public void testAddAll() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);

        assertFalse(list.addAll(emptyList()));
        assertEquals(0, list.size());

        // test inside block size
        assertTrue(list.addAll(asList(1, 2)));
        assertEquals(2, list.size());

        // test exceed block size
        assertTrue(list.addAll(asList(3,4,5,6,7)));
        assertEquals(7, list.size());
    }

    /**
     * The test duplicates the test for the add() method, so we can change it if
     * the behavior of add and offerLast diverges
     */
    @Test
    public void testOfferLast() {
        // test inside block size
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        list.offerLast(1);
        assertEquals(1, list.size());
        list.offerLast(2);
        assertEquals(2, list.size());
        list.offerLast(3);
        assertEquals(3, list.size());
        list.offerLast(4);
        assertEquals(4, list.size());

        // test exceed block size
        list.offerLast(5);
        assertEquals(5, list.size());
        list.offerLast(6);
        assertEquals(6, list.size());
    }

    @Test
    public void testPeekLastMethods() {
        // test inside block size
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(6);
        list.add(1);
        list.add(2);
        list.add(3);

        assertEquals(3, list.getLast());
        assertEquals(3, list.peekLast());

        // test exceed block size
        list.add(4);
        assertEquals(4, list.getLast());
        assertEquals(4, list.peekLast());

        // testing again, making sure list did not change after calling peekLast
        assertEquals(4, list.getLast());
        assertEquals(4, list.peekLast());

        list.add(55);
        assertEquals(5, list.size());
        assertEquals(55, list.getLast());
        assertEquals(55, list.peekLast());

        list.add(66);
        assertEquals(6, list.size());
        assertEquals(66, list.getLast());
        assertEquals(66, list.peekLast());

        // push to the head of the list does not change the last element
        for (int i = 0; i < 12; i ++) {
            list.push(-i);
        }
        assertEquals(18, list.size());
        assertEquals(66, list.getLast());
        assertEquals(66, list.pollLast());
    }

    @Test
    public void testPeekLastEmptyList() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        assertThrows(NoSuchElementException.class, list::getLast);
        assertNull(list.pollLast());
    }

    @Test
    public void testRemoveLast() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(6);
        assertThrows(NoSuchElementException.class, list::removeLast);

        testRemoveLastPollLastInt(list, UnrolledLinkedListDeque::removeLast);
    }

    @Test
    public void testPollLast() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        assertNull(list.pollLast());

        testRemoveLastPollLastInt(list, UnrolledLinkedListDeque::pollLast);
    }

    private void testRemoveLastPollLastInt(UnrolledLinkedListDeque<Integer> list,
                                           Function<UnrolledLinkedListDeque<Integer>, Integer> action) {
        // test inside block size
        list.push(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        assertEquals(5, action.apply(list));
        assertEquals(4, list.size());

        assertEquals(4, action.apply(list));
        assertEquals(3, list.size());
        assertEquals(3, list.peekLast());

        // test cause block change
        assertEquals(3, action.apply(list));
        assertEquals(2, list.size());
        assertEquals(2, action.apply(list));
        assertEquals(1, list.size());
        assertEquals(1, action.apply(list));
        assertEquals(0, list.size());
        assertNull(list.pollLast());
        assertNull(list.peekFirst());
        assertTrue(list.isEmpty());
    }

    @Test
    public void testPush() {
        // test inside block size
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(6);
        list.push(1);
        assertEquals(1, list.size());
        list.push(2);
        assertEquals(2, list.size());
        list.push(3);
        assertEquals(3, list.size());

        // test exceed block size
        list.push(4);
        assertEquals(4, list.size());
        list.push(5);
        assertEquals(5, list.size());
        list.push(6);
        assertEquals(6, list.size());
        list.push(66);
        assertEquals(7, list.size());
    }

    /**
     * The test duplicates the test for the push() method, so we can change it if
     * the behavior of add and offerFirst diverges
     */
    @Test
    public void testOfferFirst() {
        // test inside block size
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(6);
        list.offerFirst(1);
        assertEquals(1, list.size());
        list.offerFirst(2);
        assertEquals(2, list.size());
        list.offerFirst(3);
        assertEquals(3, list.size());

        // test exceed block size
        list.offerFirst(4);
        assertEquals(4, list.size());
        list.offerFirst(5);
        assertEquals(5, list.size());
        list.offerFirst(6);
        assertEquals(6, list.size());
        list.offerFirst(66);
        assertEquals(7, list.size());
    }


    @Test
    public void testPeekFirstMethods() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<Integer>(4);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        assertEquals(1, list.getFirst());
        assertEquals(1, list.element());
        assertEquals(1, list.peek());
        assertEquals(1, list.peekFirst());
        // testing again, making sure list did not change after calling peekLast
        assertEquals(1, list.getFirst());
        assertEquals(1, list.peek());
        assertEquals(1, list.peekFirst());

        // lets peek at elements inserted to the head of the list
        list.push(0);
        list.push(-1);
        list.push(-2);
        assertEquals(-2, list.getFirst());
        assertEquals(-2, list.element());
        assertEquals(-2, list.peek());
        assertEquals(-2, list.peekFirst());
        list.push(-3);
        list.push(-4);
        list.push(-5);
        list.push(-6);
        assertEquals(-6, list.getFirst());
        assertEquals(-6, list.element());
        assertEquals(-6, list.peek());
        assertEquals(-6, list.peekFirst());
    }

    @Test
    public void testPeekFirstGetFistEmptyList() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        assertThrows(NoSuchElementException.class, list::getFirst);
        assertThrows(NoSuchElementException.class, list::element);
        assertNull(list.peekFirst());
        assertNull(list.peek());
    }

    @Test
    public void testRemoveFirstPop() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(6);
        assertThrows(NoSuchElementException.class, list::removeFirst);


        testRemoveFirstPollFirstInt(list, UnrolledLinkedListDeque::removeFirst);

        // remove first and pop are the same
        UnrolledLinkedListDeque<Integer> list2 = new UnrolledLinkedListDeque<>(6);
        assertThrows(NoSuchElementException.class, list2::pop);

        testRemoveFirstPollFirstInt(list2, UnrolledLinkedListDeque::pop);

        // remove and remove are the same
        UnrolledLinkedListDeque<Integer> list3 = new UnrolledLinkedListDeque<>(6);
        assertThrows(NoSuchElementException.class,  list3::remove );

        testRemoveFirstPollFirstInt(list3, UnrolledLinkedListDeque::remove);

    }

    @Test
    public void testPollPollFirst() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        assertNull(list.pollFirst());

        testRemoveFirstPollFirstInt(list, UnrolledLinkedListDeque::pollFirst);

        // Poll and pollFirst are the same
        UnrolledLinkedListDeque<Integer> list2 = new UnrolledLinkedListDeque<>(4);
        assertNull(list2.poll());

        testRemoveFirstPollFirstInt(list2, UnrolledLinkedListDeque::poll);
    }

    private void testRemoveFirstPollFirstInt(UnrolledLinkedListDeque<Integer> list,
                                            Function<UnrolledLinkedListDeque<Integer>, Integer> action) {
        // test inside block size
        list.push(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        assertEquals(1, action.apply(list));
        assertEquals(4, list.size());

        assertEquals(2, action.apply(list));
        assertEquals(3, list.size());
        assertEquals(5, list.peekLast());
        assertEquals(3, list.peekFirst());

        // test cause block change
        assertEquals(3, action.apply(list));
        assertEquals(2, list.size());
        assertEquals(4, action.apply(list));
        assertEquals(1, list.size());
        assertEquals(5, action.apply(list));
        assertEquals(0, list.size());
        assertNull( list.pollLast());
        assertNull( list.peekFirst());
        assertTrue(list.isEmpty());

        for (int i = 1; i <= 30; i++) {
            list.push(-i);
        }
        for (int i = 1; i <= 30; i++) {
            list.add(i);
        }
        assertEquals(60, list.size());
        for (int i = 30; i >= 1; i--) {
            assertEquals(-i, action.apply(list));
        }
        assertEquals(30, list.size());
        for (int i = 1; i <= 30; i++) {
            assertEquals(i, action.apply(list));
        }
        assertEquals(0, list.size());
    }

    @Test
    public void testAlternatingPollFirstAndLast() {
        for (Integer blockSize : Arrays.asList(4, 30, 250, 1000)) {
            UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(blockSize);

            for (Integer itemsCount : Arrays.asList(31, 244, 751)) {

                for (int i =  1; i <= itemsCount; i++) {
                    list.push(-i);
                }
                for (int i =  1; i <= itemsCount; i++) {
                    list.add(i);
                }
                assertEquals(itemsCount * 2, list.size());

                // Act
                for (int i =  1; i <= itemsCount; i++) {
                    Integer fromHead = list.pollFirst();
                    Integer fromTail = list.pollLast();
                    assertEquals(fromHead, -(fromTail));
                }
                assertEquals(0, list.size());
            }
        }
    }

    @Test
    public void testIterator_emptyList() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        Iterator<Integer> iterator = list.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testIterator() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);

        int itemsCount = 6;
        for (int i = 0; i < itemsCount; i++) {
            list.push(-i);
            list.add(i);
        }

        // Act-assert
        Iterator<Integer> iterator2 = list.iterator();
        for (int i = 0; i < itemsCount*2; i++) {
            assertTrue(iterator2.hasNext());
            iterator2.next();
        }

        assertFalse(iterator2.hasNext());
        assertThrows(NoSuchElementException.class, iterator2::next);
    }


    @Test
    public void test_iterator_grown_from_head() {
        // test grow only from head
        UnrolledLinkedListDeque<Integer> listFromHead = new UnrolledLinkedListDeque<>(4);
        int itemsCount = 6;

        for (int i =  0; i < itemsCount; i++) {
            listFromHead.push(i);
        }

        Iterator<Integer> iterator3 = listFromHead.iterator();
        for (int i =  0; i < itemsCount; i++) {
            assertTrue(iterator3.hasNext());
            Integer item = iterator3.next();
            assertEquals(itemsCount - i -1, item);
        }
        assertFalse(iterator3.hasNext());
        assertThrows(NoSuchElementException.class, iterator3::next);


        // Test partial head block
        listFromHead.removeFirst();
        listFromHead.removeFirst();
        Iterator<Integer> iterator4 = listFromHead.iterator();
        int itemsCountAfterRemoval = itemsCount - 2;
        for (int i =  0; i < itemsCountAfterRemoval; i++) {
            assertTrue(iterator4.hasNext());
            Integer item = iterator4.next();
            assertEquals(itemsCountAfterRemoval - i -1, item);
        }
        assertFalse(iterator4.hasNext());
        assertThrows(NoSuchElementException.class,  iterator4::next);
    }

    @Test
    public void testDescendingIterator_emptyList() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        Iterator<Integer> iterator = list.descendingIterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

    }


    @Test
    public void testDescendingIterator() {
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        int itemsCount = 6;
        for (int i =  0; i < itemsCount; i++) {
            list.push(-i);
            list.add(i);
        }

        // Act-assert
        Iterator<Integer> iterator2 = list.descendingIterator();
        for (int i =  0; i < itemsCount*2; i++) {
            assertTrue(iterator2.hasNext());
            iterator2.next();
        }
        assertFalse(iterator2.hasNext());
    }

    @Test
    public void testDescendingIterator_grown_from_tail() {
        int itemsCount = 6;
        // test grow only from tail
        UnrolledLinkedListDeque<Integer> listFromTail = new UnrolledLinkedListDeque<>(4);
        for (int i = 1; i <= itemsCount; i++) {
            listFromTail.add(i);
        }
        Iterator<Integer> iterator3= listFromTail.descendingIterator();
        for (int i =  1; i <= itemsCount; i++) {
            assertTrue(iterator3.hasNext());
            Integer item = iterator3.next();
            assertEquals(item, itemsCount - i + 1);
        }
        assertFalse(iterator3.hasNext());
    }

    @Test
    public void test_contains() {
        int itemsCount = 6;
        // test grow only from tail
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<Integer>(4);
        for (int i =  1; i <= itemsCount; i++) {
            list.push(-i);
            list.add(i);
        }

        // Act-assert
        assertTrue(list.contains(-1));
        assertTrue(list.contains(-6));
        assertTrue(list.contains(-3));
        assertTrue(list.contains(1));
        assertTrue(list.contains(6));
        assertTrue(list.contains(3));
        assertFalse(list.contains(0));
    }

    @Test
    public void test_containsAll() {
        int itemsCount = 6;
        // test grow only from tail
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        for (int i = 1; i <= itemsCount; i++) {
            list.push(-i);
            list.add(i);
        }

        // Act-assert
        assertTrue(list.containsAll(asList( -1, -3, 6, 3)));
        assertTrue(list.containsAll(singletonList(-1)));
        // Follows Kotlin conventions
        assertTrue(list.containsAll(emptyList()));
        assertFalse(list.containsAll(asList( -1, -3, 6, 78)));
    }

    @Test
    public void testToArray() {
        int itemsCount = 6;
        // test grow only from tail
        UnrolledLinkedListDeque<Integer> list = new UnrolledLinkedListDeque<>(4);
        for (int i = 0; i < itemsCount; i++) {
            list.add(i);
        }

        // Act-assert
        Assertions.assertArrayEquals(new Object[]{0, 1, 2, 3, 4, 5}, list.toArray());

    }

}