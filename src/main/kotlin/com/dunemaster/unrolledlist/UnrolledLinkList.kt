package com.dunemaster.unrolledlist


const val DEFAULT_BLOCK_SIZE : Int = 128

/**
 * This is an implementation of an unrolled linked list, where data is stored in blocks,
 * which gives better cache locality and fewer allocations than a regular linked list.
 *
 * The idea of having two indexes, one for the left and one for the right, is to allow
 * for efficient insertion and removal at both ends of the list. It is borrowed from the Python
 * implementation of deque.
 *
 * Currently, capacity restrictions are not supported
 */
class UnrolledLinkList<E>(val blockSize : Int  = DEFAULT_BLOCK_SIZE,
                          val headTailInsertionsRatio : Double = 0.5,
                          private val center : Int = blockSize / 2,
                          ) : RandomAccess,
    java.util.AbstractList<E>(), java.util.Deque<E> {

    private var indexInHeadBlock : Int = center + 1
    private var indexInTailBlock : Int = center

    private class Node<E>(blockSize: Int) {
//        fun tryRemoveLast() : E? {
//            if (rightIndex == 0) {
//                return null
//            } else {
//                val e = elements[rightIndex-1] as E
//                rightIndex--
//                return e
//            }
//        }
//
//        fun tryGetFirst(): E? {
//            if (rightIndex == 0) {
//                return null
//            } else {
//                return  elements[rightIndex-1] as E
//            }
//        }

        val elements = arrayOfNulls<Any>(blockSize)

        var prev: Node<E>? = null

        var next: Node<E>? = null
    }

    private var head: Node<E>? = null

    private var tail: Node<E>? = null
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
        indexInTailBlock++
        if (tail == null) {
            head = Node(blockSize)
            tail = head
        } else if (indexInTailBlock >= blockSize) {
            val newNode = Node<E>(blockSize)
            tail!!.next = newNode
            newNode.prev = tail
            tail = newNode
            indexInTailBlock = center + 1
        }
        tail!!.elements.set(indexInTailBlock, element)
        size++
        modCount++;
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
        indexInTailBlock--
        size--
        if (indexInTailBlock == center) {
            // remove the last node
            if (head == tail) {
                head = null
                tail = null
            } else {
                val prev = tail!!.prev!!
                prev.next = null;
                tail = tail!!.prev
                indexInTailBlock = blockSize - 1;
            }
        }
        return element as E

    }



    private fun tryAddFirst(element: E): Boolean {
        throw NotImplementedError()
    }

    private fun tryGetFirst(): E?  =TODO()
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