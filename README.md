# Java implementation of an unrolled deque data structure

[Maven Central](https://mvnrepository.com/artifact/io.github.dunemaster/unrolleddeque/)

## Application

The primary application of the unrolled deque is in job scheduling. 
The deque serves as a  work queue, supplied by multiple suppliers and consumed by multiple consumers. 

The widely used Java alternatives for an unrolled deque are `LinkedList` and `ArrayDeque`.

Compared to `LinkedList`, unrolled deque has:
* better cache locality, meaning almost every operation (access, addition, removal) is faster.
* much lower memory overhead.
* much lower allocation rate, when the deque is used as a work queue and elements are frequently added and removed.

It is easy to notice, that out of the box `java.util.ArrayDeque` has all the same advantages as unrolled deque, 
and performance measurements show that `java.util.ArrayDeque` is somewhat faster than unrolled deque in most cases.

So when should one prefer `UnrolledLinkedListDeque` over `java.util.ArrayDeque`?

`UnrolledLinkedListDeque` has the following advantages over `java.util.ArrayDeque`:

* `UnrolledLinkedListDeque` can shrink its size when the number of elements decreases 
(compared to `ArrayDeque` which always keeps the maximum capacity to which it has grown).
* `ArrayDeque` expands by growing its internal storage by 50%, which means that in the worst case it can consume 2.5 times as much memory it needs,
which can require large continuous memory chunks and more frequent garbage collection.


## Design and inspiration

The implementation is largely a port of Python deque implementation, which
can be found [here](https://github.com/python/cpython/blob/v3.11.4/Modules/_collectionsmodule.c)

## Limitations
- The library is intended to be used with Java 8 and higher.
- The implementation is not thread safe.
- Current version only supports operations on the front and back of the deque. 
Attempts to insert or remove elements from the middle of the deque will result in an exception.