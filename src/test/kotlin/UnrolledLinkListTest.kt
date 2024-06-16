import com.dunemaster.unrolledlist.UnrolledLinkList
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UnrolledLinkListTest {

    @Test
    fun testEmptyList() {
        val list = UnrolledLinkList<Int>()
        assertEquals(0, list.size)
        assertTrue(list.isEmpty())
    }

    @Test
    fun testAdd() {
        // test inside block size
        val list = UnrolledLinkList<Int>(6)
        list.add(1)
        assertEquals(1, list.size)
        list.add(2)
        assertEquals(2, list.size)
        list.add(3)
        assertEquals(3, list.size)

        // test exceed block size
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
        val list = UnrolledLinkList<Int>(3)
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
    fun testAddAtIndex() {
        // test inside block size
        val list = UnrolledLinkList<Int>(4)
        list.add(0, 1)
        assertEquals(1, list.size)
        // adding at tail (for non-empty list
        list.add(1, 2)
        assertEquals(2, list.size)
        // shifting tail
        list.add(1, 21)
        assertEquals(3, list.size)
        // shifting non-tail
        list.add(1, 31)
        assertEquals(4, list.size)

        // test exceed block size
        //TODO
    }

    @Test
    fun testPeekLastMethods() {
        // test inside block size
        val list = UnrolledLinkList<Int>(6)
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
    }

    @Test
    fun testPeekLastEmptyList() {
        val list = UnrolledLinkList<Int>(3)
        assertThrows(NoSuchElementException::class.java) { list.last }
        assertNull(list.peekLast())
    }

    @Test
    fun testRemoveLast() {
        val list = UnrolledLinkList<Int>(6)
        assertThrows(NoSuchElementException::class.java) { list.removeLast() }

        testRemoveLastPollLastInt(list) { list.removeLast() }

    }

    @Test
    fun testPollLast() {
        val list = UnrolledLinkList<Int>(3)
        assertNull(list.pollLast())

        testRemoveLastPollLastInt(list) { list.pollLast() }
    }

    private fun testRemoveLastPollLastInt(list: UnrolledLinkList<Int>,
                                          action : Function1<UnrolledLinkList<Int>, Int?> ) {
        // test inside block size
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)

        assertEquals(5, action(list))
        assertEquals(4, list.size)
        assertEquals(4, action(list))
        assertEquals(3, list.size)

        // test cause block change
        assertEquals(3, action(list))
        assertEquals(2, list.size)
        assertEquals(2, action(list))
        assertEquals(1, list.size)
        assertEquals(1, action(list))
        assertEquals(0, list.size)
    }

    @Test
    fun testPush() {
        // test inside block size
        val list = UnrolledLinkList<Int>(3)
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
    }

    @Test
    fun testPeekFirstMethods() {
        val list = UnrolledLinkList<Int>(3)
        list.add(1)
        list.add(2)
        list.add(3)

        assertEquals(3, list.first)
        assertEquals(3, list.peekFirst())
        // testing again, making sure list did not change after calling peekLast
        assertEquals(3, list.first)
        assertEquals(3, list.peekFirst())
    }

    @Test
    fun testPeekFirstEmptyList() {
        val list = UnrolledLinkList<Int>(3)
        assertThrows(NoSuchElementException::class.java) { list.first }
        assertNull(list.peekFirst())
    }

}