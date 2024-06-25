package com.dunemaster.unrolleddeque


const val DEFAULT_BLOCK_SIZE : Int = 128

/**
 * This is an implementation of an unrolled linked list deque, where data is stored in blocks,
 * which gives better cache locality and fewer allocations than a regular linked list deque.
 *
 * The idea of having two indexes, one for the left and one for the right, is to allow
 * for efficient insertion and removal at both ends of the list. It is borrowed from the Python
 * implementation of deque.
 *
 * Currently, capacity restrictions are not supported
 */
class UnrolledLinkedListDeque<E>(
    private val blockSize : Int  = DEFAULT_BLOCK_SIZE,
    private val center : Int = blockSize / 2 - 1,
                          ) :  java.util.Deque<E> {

    init {
        require(blockSize > 0) { "blockSize must be positive" }
        require(blockSize % 2 == 0) { "blockSize must be even" }
    }

    constructor(blockSize: Int) : this(blockSize, blockSize / 2 - 1) {

    }

    private var indexInHeadBlock : Int = center + 1
    private var indexInTailBlock : Int = center

    private class Node<E>(blockSize: Int) {
        val elements = arrayOfNulls<Any>(blockSize)

        var prev: Node<E>? = null

        var next: Node<E>? = null
    }

    private var head: Node<E>? = null

    private var tail: Node<E>? = null

    override var size: Int = 0
        private set

    override fun addAll(elements: Collection<E>): Boolean {
        var anyAdded = false
        for (element in elements) {
            val added = tryAddLast(element)
            anyAdded = anyAdded || added
        }
        return anyAdded
    }

    override fun clear() {
        setToClearState()
    }

    override fun iterator(): MutableIterator<E> {
        TODO("Not yet implemented")
    }

    override fun descendingIterator(): MutableIterator<E> {
        TODO("Not yet implemented")
    }

    override fun remove(): E = removeFirst()

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: E): Boolean {
        TODO("Not yet implemented")
    }



    override fun poll(): E? = pollFirst()

    override fun element(): E  = getFirst()

    override fun peek(): E? =  peekFirst()

    override fun removeFirst(): E {
        throwIfEmpty()
        return tryRemoveFirst()!!
    }

    override fun removeLast(): E {
        throwIfEmpty()
        return tryRemoveLast()!!
    }

    override fun pollFirst(): E? = tryRemoveFirst()

    override fun pollLast(): E? {
        return tryRemoveLast()
    }

    override fun getFirst(): E {
        throwIfEmpty()
        return tryGetFirst()!!
    }

    override fun getLast(): E {
        return tryGetLast() ?: throw NoSuchElementException()
    }

    override fun peekFirst(): E?  =  tryGetFirst()

    override fun peekLast(): E? {
        return tryGetLast()
    }

    override fun pop(): E = removeFirst()


    override fun push(element: E) = addFirst(element)

    override fun offerLast(element: E): Boolean {
        tryAddLast(element)
        return true
    }

    override fun offerFirst(element: E): Boolean {
        return tryAddFirst(element)
    }

    override fun addLast(element: E) {
        tryAddLast(element)
    }

    override fun addFirst(element: E) {
       if (!tryAddFirst(element))
           throw IllegalStateException()
    }

    override fun offer(element: E): Boolean {
        tryAddLast(element)
        return true
    }
    override fun add(element: E) = tryAddLast(element)

    override fun retainAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    override fun remove(element: E): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeFirstOccurrence(element: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeLastOccurrence(element: Any?): Boolean {
        TODO("Not yet implemented")
    }

    private fun tryAddLast(element: E): Boolean {
        indexInTailBlock++
        if (tail == null) {
            head = Node(blockSize)
            tail = head
        } else if (indexInTailBlock == blockSize) {
            val newNode = Node<E>(blockSize)
            tail!!.next = newNode
            newNode.prev = tail
            tail = newNode
            indexInTailBlock = 0
        }
        tail!!.elements[indexInTailBlock] = element
        size++
        return true
    }

    private fun tryGetLast() : E? {
        if (tail == null) {
            return null
        }
        return tail!!.elements.get(indexInTailBlock) as E
    }

    private fun tryRemoveLast(): E? {
        if (tail == null) {
            return null
        }
        val element = tail!!.elements[indexInTailBlock]
        // releasing memory! //TODO: test
        tail!!.elements[indexInTailBlock] = null
        indexInTailBlock--
        size--
        if (indexInTailBlock < 0) {
            // remove the last node
            if (head == tail) {
                if (size == 0) {
                    setToClearState()
                }
            } else {
                val prev = tail!!.prev!!
                prev.next = null
                tail = tail!!.prev
                indexInTailBlock = blockSize - 1;
            }
        }
        return element as E

    }

    private fun tryAddFirst(element: E): Boolean {
        indexInHeadBlock--
        if (head == null) {
            head = Node(blockSize)
            tail = head
        } else if (indexInHeadBlock < 0) {
            val newNode = Node<E>(blockSize)
            head!!.prev = newNode
            newNode.next = head
            head = newNode
            indexInHeadBlock = blockSize - 1
        }
        head!!.elements.set(indexInHeadBlock, element)
        size++
        return true
    }

    private fun tryGetFirst(): E? {
        if (head == null) {
            return null
        }
        return head!!.elements.get(indexInHeadBlock) as E
    }

    private fun tryRemoveFirst(): E? {
        if (head == null) {
            return null
        }
        val element = head!!.elements[indexInHeadBlock]
        // releasing memory!
        head!!.elements[indexInHeadBlock] = null
        indexInHeadBlock++
        size--
        if (indexInHeadBlock == blockSize) {
            // remove the first node
            if (head == tail) {
                if (size == 0) {
                    setToClearState()
                }
            } else {
                val next = head!!.next
                next!!.prev = null;
                head = next
                indexInHeadBlock = 0;
            }
        }
        return element as E
    }

    private inline fun throwIfEmpty() {
        if (size == 0) {
            throw NoSuchElementException()
        }
    }

    private fun setToClearState() {
        head = null
        tail = null
        size = 0;

        indexInHeadBlock = center + 1
        indexInTailBlock = center
    }

}