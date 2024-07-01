import com.dunemaster.unrolleddeque.UnrolledLinkedListDeque
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UnrolledLinkedListDequeTest {

    @Test
    fun testEmptyList() {
        val list = UnrolledLinkedListDeque<Int>()
        assertEquals(0, list.size)
        assertTrue(list.isEmpty())
        assertNull(list.pollFirst())
        assertNull(list.pollLast())
    }

    @Test
    fun testClearList() {
        val list = UnrolledLinkedListDeque<Int>(4)
        list.add(1)
        list.add(2)
        list.add(4)
        list.add(5)
        list.add(6)
        list.pollLast()
        list.pollLast()

        // Act
        list.clear()

        // Assert
        assertEquals(0, list.size)
        assertTrue(list.isEmpty())
        assertNull(list.pollFirst())
        assertNull(list.pollLast())
        list.add(1)
        list.add(2)
        list.add(4)
        list.add(5)
        list.add(6)
        assertEquals(6, list.pollLast())
        assertEquals(5, list.peekLast())
    }

    @Test
    fun testAdd() {
        // test inside block size
        val list = UnrolledLinkedListDeque<Int>(6)
        testAddAddLast(list) { l, x -> l.add(x) }
        val list2 = UnrolledLinkedListDeque<Int>(6)
        testAddAddLast(list2) { l, x -> l.addLast(x);  true }
    }

    private fun testAddAddLast(list: UnrolledLinkedListDeque<Int>,
                               action : Function2<UnrolledLinkedListDeque<Int>, Int, Boolean>) {
        action(list, 1)
        assertEquals(1, list.size)
        action(list,2)
        assertEquals(2, list.size)
        action(list,3)
        assertEquals(3, list.size)

        // test exceed (half)block size
        action(list,4)
        assertEquals(4, list.size)
        action(list,5)
        assertEquals(5, list.size)
    }


    @Test
    fun testAddAll() {
        val list = UnrolledLinkedListDeque<Int>(4)

        assertFalse(list.addAll(arrayListOf()))
        assertEquals(0, list.size)

        // test inside block size
        assertTrue(list.addAll(arrayListOf(1, 2)))
        assertEquals(2, list.size)

        // test exceed block size
        assertTrue(list.addAll(arrayListOf(3,4,5,6,7)))
        assertEquals(7, list.size)
    }

    /**
     * The test duplicates the test for the add() method, so we can change it if
     * the behavior of add and offerLast diverges
     */
    @Test
    fun testOfferLast() {
        // test inside block size
        val list = UnrolledLinkedListDeque<Int>(4)
        list.offerLast(1)
        assertEquals(1, list.size)
        list.offerLast(2)
        assertEquals(2, list.size)
        list.offerLast(3)
        assertEquals(3, list.size)
        list.offerLast(4)
        assertEquals(4, list.size)

        // test exceed block size
        list.offerLast(5)
        assertEquals(5, list.size)
        list.offerLast(6)
        assertEquals(6, list.size)
    }

    @Test
    fun testPeekLastMethods() {
        // test inside block size
        val list = UnrolledLinkedListDeque<Int>(6)
        list.add(1)
        list.add(2)
        list.add(3)

        assertEquals(3, list.last)
        assertEquals(3, list.peekLast())

        // test exceed block size
        list.add(4)
        assertEquals(4, list.last)
        assertEquals(4, list.peekLast())

        // testing again, making sure list did not change after calling peekLast
        assertEquals(4, list.last)
        assertEquals(4, list.peekLast())

        list.add(55)
        assertEquals(5, list.size)
        assertEquals(55, list.last)
        assertEquals(55, list.peekLast())

        list.add(66)
        assertEquals(6, list.size)
        assertEquals(66, list.last)
        assertEquals(66, list.peekLast())

        // push to the head of the list does not change the last element
        for (i in 1..12) {
            list.push(-i)
        }
        assertEquals(18, list.size)
        assertEquals(66, list.last)
        assertEquals(66, list.peekLast())
    }

    @Test
    fun testPeekLastEmptyList() {
        val list = UnrolledLinkedListDeque<Int>(4)
        assertThrows(NoSuchElementException::class.java) { list.last }
        assertNull(list.peekLast())
    }

    @Test
    fun testRemoveLast() {
        val list = UnrolledLinkedListDeque<Int>(6)
        assertThrows(NoSuchElementException::class.java) { list.removeLast() }

        testRemoveLastPollLastInt(list) { list.removeLast() }
    }

    @Test
    fun testPollLast() {
        val list = UnrolledLinkedListDeque<Int>(4)
        assertNull(list.pollLast())

        testRemoveLastPollLastInt(list) { list.pollLast() }
    }

    private fun testRemoveLastPollLastInt(list: UnrolledLinkedListDeque<Int>,
                                          action : Function1<UnrolledLinkedListDeque<Int>, Int?> ) {
        // test inside block size
        list.push(1)
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)

        assertEquals(5, action(list))
        assertEquals(4, list.size)

        assertEquals(4, action(list))
        assertEquals(3, list.size)
        assertEquals(3, list.peekLast())

        // test cause block change
        assertEquals(3, action(list))
        assertEquals(2, list.size)
        assertEquals(2, action(list))
        assertEquals(1, list.size)
        assertEquals(1, action(list))
        assertEquals(0, list.size)
        assertEquals(null, list.peekLast())
        assertEquals(null, list.peekFirst())
        assertTrue(list.isEmpty())
    }

    @Test
    fun testPush() {
        // test inside block size
        val list = UnrolledLinkedListDeque<Int>(6)
        list.push(1)
        assertEquals(1, list.size)
        list.push(2)
        assertEquals(2, list.size)
        list.push(3)
        assertEquals(3, list.size)

        // test exceed block size
        list.push(4)
        assertEquals(4, list.size)
        list.push(5)
        assertEquals(5, list.size)
        list.push(6)
        assertEquals(6, list.size)
        list.push(66)
        assertEquals(7, list.size)
    }

    /**
     * The test duplicates the test for the push() method, so we can change it if
     * the behavior of add and offerFirst diverges
     */
    @Test
    fun testOfferFirst() {
        // test inside block size
        val list = UnrolledLinkedListDeque<Int>(6)
        list.offerFirst(1)
        assertEquals(1, list.size)
        list.offerFirst(2)
        assertEquals(2, list.size)
        list.offerFirst(3)
        assertEquals(3, list.size)

        // test exceed block size
        list.offerFirst(4)
        assertEquals(4, list.size)
        list.offerFirst(5)
        assertEquals(5, list.size)
        list.offerFirst(6)
        assertEquals(6, list.size)
        list.offerFirst(66)
        assertEquals(7, list.size)
    }


    @Test
    fun testPeekFirstMethods() {
        val list = UnrolledLinkedListDeque<Int>(4)
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)

        assertEquals(1, list.first)
        assertEquals(1, list.element())
        assertEquals(1, list.peek())
        assertEquals(1, list.peekFirst())
        // testing again, making sure list did not change after calling peekLast
        assertEquals(1, list.first)
        assertEquals(1, list.peek())
        assertEquals(1, list.peekFirst())

        // lets peek at elements inserted to the head of the list
        list.push(0)
        list.push(-1)
        list.push(-2)
        assertEquals(-2, list.first)
        assertEquals(-2, list.element())
        assertEquals(-2, list.peek())
        assertEquals(-2, list.peekFirst())
        list.push(-3)
        list.push(-4)
        list.push(-5)
        list.push(-6)
        assertEquals(-6, list.first)
        assertEquals(-6, list.element())
        assertEquals(-6, list.peek())
        assertEquals(-6, list.peekFirst())
    }

    @Test
    fun testPeekFirstGetFistEmptyList() {
        val list = UnrolledLinkedListDeque<Int>(4)
        assertThrows(NoSuchElementException::class.java) { list.getFirst() }
        assertThrows(NoSuchElementException::class.java) { list.element() }
        assertNull(list.peekFirst())
        assertNull(list.peek())
    }

    @Test
    fun testRemoveFirstPop() {
        val list = UnrolledLinkedListDeque<Int>(6)
        assertThrows(NoSuchElementException::class.java) { list.removeFirst() }

        testRemoveFirstPollFirstInt(list) { list.removeFirst() }

        // remove first and pop are the same
        val list2 = UnrolledLinkedListDeque<Int>(6)
        assertThrows(NoSuchElementException::class.java) { list2.pop() }

        testRemoveFirstPollFirstInt(list2) { list2.pop() }

    }

    @Test
    fun testPollPollFirst() {
        val list = UnrolledLinkedListDeque<Int>(4)
        assertNull(list.pollFirst())

        testRemoveFirstPollFirstInt(list) { list.pollFirst() }

        // Poll and pollFirst are the same
        val list2 = UnrolledLinkedListDeque<Int>(4)
        assertNull(list2.poll())

        testRemoveFirstPollFirstInt(list2) { list2.poll() }
    }

    private fun testRemoveFirstPollFirstInt(list: UnrolledLinkedListDeque<Int>,
                                          action : Function1<UnrolledLinkedListDeque<Int>, Int?> ) {
        // test inside block size
        list.push(1)
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)

        assertEquals(1, action(list))
        assertEquals(4, list.size)

        assertEquals(2, action(list))
        assertEquals(3, list.size)
        assertEquals(5, list.peekLast())
        assertEquals(3, list.peekFirst())

        // test cause block change
        assertEquals(3, action(list))
        assertEquals(2, list.size)
        assertEquals(4, action(list))
        assertEquals(1, list.size)
        assertEquals(5, action(list))
        assertEquals(0, list.size)
        assertEquals(null, list.peekLast())
        assertEquals(null, list.peekFirst())
        assertTrue(list.isEmpty())

        for (i in 1..30) {
            list.push(-i)
        }
        for (i in 1..30) {
            list.add(i)
        }
        assertEquals(60, list.size)
        for (i in 30 downTo 1) {
            assertEquals(-i, action(list))
        }
        assertEquals(30, list.size)
        for (i in 1 .. 30) {
            assertEquals(i, action(list))
        }
        assertEquals(0, list.size)
    }

    @Test
    fun testAlternatingPollFirstAndLast() {
        for (blockSize in intArrayOf(4, 30, 250, 1000)) {
            val list = UnrolledLinkedListDeque<Int>(blockSize)

            for (itemsCount in intArrayOf(31, 244, 751)) {

                for (i in 1..itemsCount) {
                    list.push(-i)
                }
                for (i in 1..itemsCount) {
                    list.add(i)
                }
                assertEquals(itemsCount * 2, list.size)

                // Act
                for (i in 1..itemsCount) {
                    val fromHead = list.pollFirst()
                    val fromTail = list.pollLast()
                    assertEquals(fromHead, -(fromTail)!!)
                }
                assertEquals(0, list.size)
            }
        }
    }

    @Test
    fun testIterator_emptyList() {
        val list = UnrolledLinkedListDeque<Int>(4)
        val iterator = list.iterator()
        assertFalse(iterator.hasNext())
        assertThrows(NoSuchElementException::class.java) { iterator.next() }
    }

    @Test
    fun testIterator() {
        val list = UnrolledLinkedListDeque<Int>(4)

        val itemsCount = 6
        for (i in 1..itemsCount) {
            list.push(-i)
            list.add(i)
        }

        // Act-assert
        val iterator2 = list.iterator()
        for (i in 1..itemsCount*2) {
            assertTrue(iterator2.hasNext())
            iterator2.next()
        }

        assertFalse(iterator2.hasNext())
        assertThrows(NoSuchElementException::class.java) { iterator2.next() }
    }


    @Test
    fun test_iterator_grown_from_head() {
        // test grow only from head
        val listFromHead = UnrolledLinkedListDeque<Int>(4)
        val itemsCount = 6

        for (i in 1..itemsCount) {
            listFromHead.push(i)
        }

        val iterator3 = listFromHead.iterator()
        for (i in 1..itemsCount) {
            assertTrue(iterator3.hasNext())
            val item = iterator3.next()
            assertEquals(item, itemsCount - i + 1)
        }
        assertFalse(iterator3.hasNext())
        assertThrows(NoSuchElementException::class.java) { iterator3.next() }


        // Test partial head block
        listFromHead.removeFirst()
        listFromHead.removeFirst()
        val iterator4 = listFromHead.iterator()
        val itemsCountAfterRemoval = itemsCount - 2
        for (i in 1..itemsCountAfterRemoval) {
            assertTrue(iterator4.hasNext())
            val item = iterator4.next()
            assertEquals(item, itemsCountAfterRemoval - i + 1)
        }
        assertFalse(iterator4.hasNext())
        assertThrows(NoSuchElementException::class.java) { iterator4.next() }
    }

    @Test
    fun testDescendingIterator_emptyList() {
        val list = UnrolledLinkedListDeque<Int>(4)
        val iterator = list.descendingIterator()
        assertFalse(iterator.hasNext())
        assertThrows(NoSuchElementException::class.java) { iterator.next() }
    }


    @Test
    fun testDescendingIterator() {
        val list = UnrolledLinkedListDeque<Int>(4)
        val itemsCount = 6
        for (i in 1..itemsCount) {
            list.push(-i)
            list.add(i)
        }

        // Act-assert
        val iterator2 = list.descendingIterator()
        for (i in 1..itemsCount*2) {
            assertTrue(iterator2.hasNext())
            iterator2.next()
        }
        assertFalse(iterator2.hasNext())
    }

    @Test
    fun testDescendingIterator_grown_from_tail() {
        val itemsCount = 6
        // test grow only from tail
        val listFromTail = UnrolledLinkedListDeque<Int>(4)
        for (i in 1..itemsCount) {
            listFromTail.add(i)
        }
        val iterator3= listFromTail.descendingIterator()
        for (i in 1..itemsCount) {
            assertTrue(iterator3.hasNext())
            val item = iterator3.next()
            assertEquals(item, itemsCount - i + 1)
        }
        assertFalse(iterator3.hasNext())
    }

    @Test
    fun test_contains() {
        val itemsCount = 6
        // test grow only from tail
        val list = UnrolledLinkedListDeque<Int>(4)
        for (i in 1..itemsCount) {
            list.push(-i)
            list.add(i)
        }

        // Act-assert
        assertTrue(list.contains(-1))
        assertTrue(list.contains(-6))
        assertTrue(list.contains(-3))
        assertTrue(list.contains(1))
        assertTrue(list.contains(6))
        assertTrue(list.contains(3))
        assertFalse(list.contains(0))
    }

}