/**
 * Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * LinkedList.java
 */

package hw5;

import exceptions.EmptyException;
import exceptions.PositionException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic position-based list, implemented with a sentinel-based
 * doubly linked list. This required adding dummy first and last nodes
 * to the implementation, "guarding" the beginning and end. The data in
 * those nodes should be null, but their purpose is to eliminate the need
 * for special cases, particularly for the insert and remove methods.
 *
 * This is a flawed solution to the class exercise from 2/27!
 * (same as the one posted on Piazza)
 *
 * Your first TODO is to find and fix the errors in this code.
 * There are at least two flawed methods. You might be able to find
 * them by inspection, but you won't be sure without thorough tests.
 *
 * @param <T> Element type.
 */
public class LinkedList<T> implements List<T> {

    private static final class Node<T> implements Position<T> {
        // The usual doubly-linked list stuff.
        Node<T> next;   // reference to the Node after this
        Node<T> prev;   // reference to the Node before this
        T data;

        // List that created this node, to validate positions.
        List<T> owner;

        @Override
        public T get() {
            return this.data;
        }

        @Override
        public void put(T t) {
            this.data = t;
        }
    }

    /** This iterator can be used to create either a forward
        iterator, or a backwards one.
    */
    private final class ListIterator implements Iterator<T> {
        Node<T> current;
        boolean forward;

        ListIterator(boolean f) {
            this.forward = f;
            if (this.forward) {
                this.current = LinkedList.this.sentinelHead.next;
            } else {
                this.current = LinkedList.this.sentinelTail.prev;
            }
        }

        @Override
        public T next() throws NoSuchElementException {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T t = this.current.get();
            if (this.forward) {
                this.current = this.current.next;
            } else {
                this.current = this.current.prev;
            }
            return t;
        }

        @Override
        public boolean hasNext() {
            return this.current != LinkedList.this.sentinelTail;
        }
    }

    /* ** LinkedList instance variables are declared here! ** */

    private Node<T> sentinelHead;
    private Node<T> sentinelTail;
    private int length;             // how many nodes in the list

    /**
     * Create an empty list.
     */
    public LinkedList() {
        this.sentinelHead = new Node<>();
        this.sentinelTail = new Node<>();
        this.sentinelHead.owner = this;
        this.sentinelTail.owner = this;
        this.sentinelTail.prev = this.sentinelHead;
        this.sentinelHead.next = this.sentinelTail;
        this.length = 0;
    }

    // Convert a position back into a node. Guards against null positions,
    // positions from other data structures, and positions that belong to
    // other LinkedList objects. That about covers it?
    private Node<T> convert(Position<T> p) throws PositionException {
        try {
            Node<T> n = (Node<T>) p;
            if (n.owner != this) {
                throw new PositionException();
            }
            return n;
        } catch (NullPointerException | ClassCastException e) {
            throw new PositionException();
        }
    }

    @Override
    public boolean empty() {
        return this.length == 0;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public boolean first(Position<T> p) throws PositionException {
        Node<T> n = this.convert(p);
        return this.sentinelHead.next == n;
    }

    @Override
    public boolean last(Position<T> p) throws PositionException {
        Node<T> n = this.convert(p);
        return this.sentinelTail.prev == n;
    }

    @Override
    public Position<T> front() throws EmptyException {
        if (this.length == 0) {
            throw new EmptyException();
        }
        return this.sentinelHead.next;
    }

    @Override
    public Position<T> back() throws EmptyException {
        if (this.empty()) {
            throw new EmptyException();
        }
        return this.sentinelTail.prev;
    }

    @Override
    public Position<T> next(Position<T> p) throws PositionException {
        if (this.last(p)) {
            throw new PositionException();
        }
        return this.convert(p).next;
    }

    @Override
    public Position<T> previous(Position<T> p) throws PositionException {
        if (this.last(p)) {
            throw new PositionException();
        }
        return this.convert(p).prev;
    }

    @Override
    public Position<T> insertFront(T t) {
        return this.insertAfter(this.sentinelHead, t);
    }

    @Override
    public Position<T> insertBack(T t) {
        return this.insertBefore(this.sentinelTail, t);
    }

    @Override
    public void removeFront() throws EmptyException {
        this.remove(this.front());
    }

    @Override
    public void removeBack() throws EmptyException {
        this.remove(this.back());
    }

    @Override
    public void remove(Position<T> p) throws PositionException {
        Node<T> n = this.convert(p);
        n.owner = null;
        n.prev.next = n.next;
        n.next.prev = n.prev;
        this.length--;
    }

    @Override
    public Position<T> insertBefore(Position<T> p, T t)
            throws PositionException {
        Node<T> current = this.convert(p);
        Node<T> n = new Node<T>();
        n.owner = this;
        n.data = t;

        n.prev = current.prev;
        current.prev.next = n;
        n.next = current;
        current.prev = n;
        this.length++;
        return n; //are we returning a node or a position?
    }

    @Override
    public Position<T> insertAfter(Position<T> p, T t)
            throws PositionException {
        Node<T> current = this.convert(p);
        Node<T> n = new Node<T>();
        n.owner = this;
        n.data = t;

        n.next = current.next;
        current.next.prev = n;
        n.prev = current;
        current.next = n;
        this.length++;
        return n;
    }

    @Override
    public Iterator<T> forward() {
        return new ListIterator(true);
    }

    @Override
    public Iterator<T> backward() {
        return new ListIterator(false);
    }

    @Override
    public Iterator<T> iterator() {
        return this.forward();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (Node<T> n = this.sentinelHead.next; n != this.sentinelTail; n = n.next) {
            s.append(n.data);
            if (n.next != this.sentinelTail) {
                s.append(", ");
            }
        }
        s.append("]");
        return s.toString();
    }
}
