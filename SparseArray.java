/* Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * SparseArray.java
 */

package hw2;

import exceptions.IndexException;
import exceptions.LengthException;
import java.util.Iterator;


/**
 * This is an array object that implements a linked list.
 * The array has a default value specified by the user.
 * The linked list is made of nodes that hold data and a position in the array.
 * The remainder of the array is filled with the default value.
 * @param <T> Element type.
 */
public class SparseArray<T> implements Array<T> {

    /**
     * Node object that holds data of whatever type specified by the user.
     * Also holds the node's position in the array.
     * @param <T> element type.
     */
    private static class Node<T> {
        /** Data to be stored in the node. */
        T data;
        /** Position of the node in the linked list. */
        int position;
        /** Pointer to the next node in the list. */
        Node<T> next;

        /** Constructor for nodes.
         * @param t data type.
         * @param pos position of node in linked list.
         */
        Node(T t, int pos) {
            data = t;
            position = pos;
            next = null;
        }
    }


    /**
     * Iterator for a SparseArray.
     */
    private class SparseArrayIterator implements Iterator<T> {

        /** Pointer to the current node. */
        Node<T> cur;

        /** Constructor for SparseArrayIterator. */
        SparseArrayIterator() {
            this.cur = SparseArray.this.sparse;
        }

        @Override
        public boolean hasNext() {
            return this.cur != null;
        }

        @Override
        public T next() {
            T t = this.cur.data;
            this.cur = this.cur.next;
            return t;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /** Length of the array. */
    private int len;
    /** Default type of array. */
    private T def;
    /** SparseArray to be implemented. */
    private Node<T> sparse;


    /**
     * An array that is meant to be filled primarily with a default value
     * that is not going to change - with the benefit of that default
     * value not being stored numerous times as opposed to once.
     * @param length The number of indexes the array should have.
     * @param defaultValue The default value for the array.
     * @throws LengthException If length is less than or equal to 0.
    */
    public SparseArray(int length, T defaultValue) throws LengthException {
        if (length <= 0) {
            throw new LengthException();
        }

        this.len = length;
        this.def = defaultValue;
    }

    @Override
    public int length() {
        return this.len;
    }



    @Override
    public T get(int i) throws IndexException {
        if (i < 0 || i >= this.len) {
            throw new IndexException();
        }
        Node<T> current = this.sparse;

        int j = 0;
        while (current != null && j < i) {
            current = current.next;
            j = j + 1;
            if (j == i) {
                return current.data;
            }
        }

        return this.def;
    }

    @Override
    public void put(int i, T t) throws IndexException {
        if (i <= 0 || i >= this.len) {
            throw new IndexException();
        }
        // creates node w position and data
        Node<T> temp = new Node<T>(t, i);
        Node<T> current = this.sparse;

        while (current.next != null) {
            if (current.next.position < i) {
                current = current.next;
            }
            else if (current.next.position == i) {
                current.next.data = temp.data;
            }
            else {
                temp.next = current.next;
                current.next = temp;
                current = current.next;
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new SparseArrayIterator();
    }
}
