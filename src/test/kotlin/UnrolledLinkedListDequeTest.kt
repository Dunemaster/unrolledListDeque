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
        val list = UnrolledLinkedListDeque<Int>(3)
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
        list.add(1)
        assertEquals(1, list.size)
        list.add(2)
        assertEquals(2, list.size)
        list.add(3)
        assertEquals(3, list.size)

        // test exceed (half)block size
        list.add(4)
        assertEquals(4, list.size)
        list.add(5)
        assertEquals(5, list.size)
    }

    /**
     * The test duplicates the test for the add() method, so we can change it if
     * the behavior of add and offerLast diverges
     */
    @Test
    fun testOfferLast() {
        // test inside block size
        val list = UnrolledLinkedListDeque<Int>(3)
        list.offerLast(1)
        assertEquals(1, list.size)
        list.offerLast(2)
        assertEquals(2, list.size)
        list.offerLast(3)
        assertEquals(3, list.size)

        // test exceed block size
        list.offerLast(4)
        assertEquals(4, list.size)
        list.offerLast(5)
        assertEquals(5, list.size)
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
        val list = UnrolledLinkedListDeque<Int>(3)
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
        val list = UnrolledLinkedListDeque<Int>(3)
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

    @Test
    fun testPeekFirstMethods() {
        val list = UnrolledLinkedListDeque<Int>(4)
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)

        assertEquals(1, list.first)
        assertEquals(1, list.peekFirst())
        // testing again, making sure list did not change after calling peekLast
        assertEquals(1, list.first)
        assertEquals(1, list.peekFirst())

        // lets peek at elements inserted to the head of the list
        list.push(0)
        list.push(-1)
        list.push(-2)
        assertEquals(-2, list.first)
        assertEquals(-2, list.peekFirst())
        list.push(-3)
        list.push(-4)
        list.push(-5)
        list.push(-6)
        assertEquals(-6, list.first)
        assertEquals(-6, list.peekFirst())


    }

    @Test
    fun testPeekFirstEmptyList() {
        val list = UnrolledLinkedListDeque<Int>(3)
        assertThrows(NoSuchElementException::class.java) { list.first }
        assertNull(list.peekFirst())
    }

    @Test
    fun testRemoveFirst() {
        val list = UnrolledLinkedListDeque<Int>(6)
        assertThrows(NoSuchElementException::class.java) { list.removeFirst() }

        testRemoveFirstPollFirstInt(list) { list.removeFirst() }

    }

    @Test
    fun testPollFirst() {
        val list = UnrolledLinkedListDeque<Int>(3)
        assertNull(list.pollLast())

        testRemoveFirstPollFirstInt(list) { list.pollFirst() }
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
    }

}