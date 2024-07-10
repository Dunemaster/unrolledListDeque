# Java implementation of an unrolled deque data structure

## Application

TODO:
 - work stealing algorithm,
 - comparison to array list deque
 - comparison to linked list deque
 - performance data

## Design and inspiration

The implementation is largely a port of Python deque implementation, which
can be found [here](https://github.com/python/cpython/blob/v3.11.4/Modules/_collectionsmodule.c)

## Limitations
- The library is intended to be used with Java 8 and higher.
- The implementation is not thread safe.
- Current version only supports operations on the front and back of the deque. 
Attempts to insert or remove elements from the middle of the deque will result in an exception.