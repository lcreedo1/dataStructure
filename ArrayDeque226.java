/**
 * Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * ArrayDeque226.java
 */

package hw4;

import exceptions.EmptyException;

/**
 * An implementation of Deque226 using an Array.
 * @param <T> The type of the queue
 */
public class ArrayDeque226<T> implements Deque226<T> {

    /** Length of deque. */
    private int len;

    /** Number of items in deque. */
    private int elements;

    /** Location of the front of the deque. */
    private int front;

    /** Deque. */
    private T[] deq;

    /**
     * Constructor to create a new ArrayDeque226.
     */
    public ArrayDeque226() {
        this.deq = (T[]) new Object[1];
        this.len = 1;
        this.front = 0;
        this.elements = 0;
    }

    @Override
    public boolean empty() {
        return elements == 0;
    }

    @Override
    public int length() {
        return elements;
    }

    @Override
    public T front() throws EmptyException {
        if (this.empty()) {
            throw new EmptyException();
        } else {
            return deq[front];
        }
    }

    @Override
    public T back() throws EmptyException {
        if (this.empty()) {
            throw new EmptyException();
        } else {
            int loc = front + elements;
            if (loc >= len) {
                loc -= len;
            }
            return deq[loc];
        }
    }

    /**
     * Method to resize the deque, by doubling.
     * @param start true if resizing from front, false if from back.
     */
    public void resize(boolean start) {
        T[] temp = (T[]) new Object[len * 2];
        if (start) {
            for (int i = 0; i < len - 1; i++) {
                int loc = i + front;
                if (loc >= len) {
                    loc -= len;
                }
                temp[i] = deq[loc];
            }

            front = 0;
        }
        else {
            for (int i = 0; i < len - 1; i++) {
                temp[i] = deq[i];
            }
        }
        deq = temp;
        len = len * 2;
    }

    @Override
    public void insertFront(T t) {
        elements++;
        if (elements > len) {
            this.resize(true);
            deq[front] = t;
        } else {
            front = len - 1;
            deq[front] = t;
        }
    }

    @Override
    public void insertBack(T t) {
        elements++;
        if (elements > len) {
            this.resize(false);
            deq[len] = t;
        } else {
            int loc = front + elements;
            if (loc >= len) {
                loc -= len;
            }
            deq[loc] = t;
        }
    }

    @Override
    public void removeFront() throws EmptyException {
        if (this.empty()) {
            throw new EmptyException();
        } else {
            int loc = front + elements;
            if (loc >= len) {
                loc -= len;
            }
            deq[loc] = null;
            elements -= 1;
        }
    }

    @Override
    public void removeBack() throws EmptyException {
        if (this.empty()) {
            throw new EmptyException();
        } else {
            int loc = front + elements;
            if (loc >= len) {
                loc -= len;
            }
            deq[loc] = null;
            elements -= 1;
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        for (int i = 0; i < elements; i++) {
            int loc = front + i;
            if (loc >= len) {
                loc -= len;
            }
            str.append(deq[loc]);
            str.append(", ");
        }
        str.append("]");
        String send = str.toString();
        return send;
    }
}
