package com.dunemaster.unrolledlist
const val DEFAULT_BLOCK_SIZE : Int = 128;

/**
 * Currently, capacity restrictions are not supported
 */
class UnrolledLinkList<E>(val blockSize : Int  = DEFAULT_BLOCK_SIZE) : RandomAccess,
    java.util.AbstractList<E>(), java.util.Deque<E> {

    private class Node<E>(blockSize : Int ) {
        fun addLast(element: E) {
            elements[numElements] = element
            numElements++
        }

        fun tryGetLast(): E? {
            if (numElements == 0) {
                return null
            }
            return elements[numElements - 1] as E
        }

        fun tryRemoveLast() : E? {
            if (numElements == 0) {
                return null
            } else {
                val e = elements[numElements-1] as E
                numElements--
                return e
            }
        }

        fun tryGetFirst(): E? {
            if (numElements == 0) {
                return null
            } else {
                return  elements[numElements-1] as E
            }
        }

        val elements = arrayOfNulls<Any>(blockSize)
        var numElements = 0
        var next: Node<E>? = null
    }

    private var head: Node<E> = Node(blockSize)

    private var tail: Node<E> = head
    override fun remove(): E = removeFirst()

    override fun poll(): E = pollFirst()

    override fun element(): E  = getFirst()

    override fun peek(): E? =  peekFirst()
    override fun removeFirst(): E {
        throwIfEmpty()
        TODO("Not yet implemented")
    }

    override fun removeLast(): E {
        throwIfEmpty()
        return tryRemoveLast()!!
    }


    override fun pollFirst(): E {
        TODO("Not yet implemented")
    }

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

    override fun peekFirst(): E? {
        if (size == 0) {
            return null
        }
        return tryGetFirst()
    }

    override fun peekLast(): E? {
        return tryGetLast()
    }

    override fun removeFirstOccurrence(element: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeLastOccurrence(element: Any?): Boolean {
        TODO("Not yet implemented")
    }

    override fun pop(): E = removeFirst()

    override fun descendingIterator(): MutableIterator<E> {
        TODO("Not yet implemented")
    }

    override fun push(element: E) {
        tryAddLast(element)
    }

    override fun offerLast(element: E): Boolean {
        tryAddLast(element)
        return true
    }

    override fun offerFirst(element: E): Boolean {
        TODO("Not yet implemented")
    }

    override fun addLast(element: E) {
        tryAddLast(element)
    }

    override fun addFirst(element: E) {
        TODO("Not yet implemented")
    }

    override fun offer(element: E): Boolean {
        tryAddLast(element)
        return true
    }

    override var size: Int = 0
        private set

    override fun get(index: Int): E {
        checkIndex(index)
        // TODO!!!
        return if (index == size)  getLast() else throw IndexOutOfBoundsException("Not yet implemented")
    }

    override fun add(element: E) = tryAddLast(element)

    override fun add(index: Int, element: E) {
        checkIndex(index)
        // TODO!!!
         if (index == size)  tryAddLast(element) else throw IndexOutOfBoundsException("Not yet implemented")
    }

    private fun tryAddLast(element: E): Boolean {
        if (tail.numElements >= blockSize) {
            val newNode = Node<E>(blockSize)
            tail.next = newNode
            tail = newNode
        }
        tail.addLast(element)
        size++
        return true
    }

    private fun tryGetLast() : E? {
        return tail.tryGetLast()
    }

    private fun tryRemoveLast(): E? {
        val e = tail.tryRemoveLast()
        if (e != null) {
            size--
            if (tail.numElements == 0) {
                // remove the last node
                removeLastNodeIfNeeded()
            }
        }
        return e
    }

    private fun removeLastNodeIfNeeded() {
        if (tail == head)
            return
        var node = head
        while (node.next != tail) {
            node = node.next!!
        }
        tail = node
    }

    private fun tryGetFirst(): E?  = head.tryGetFirst()

    private inline fun throwIfEmpty() {
        if (size == 0) {
            throw NoSuchElementException()
        }
    }

    private inline fun checkIndex(index: Int) {
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException()
        }
    }


}