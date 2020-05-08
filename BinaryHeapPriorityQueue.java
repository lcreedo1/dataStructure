/**
 * Liam Creedon, lcreedo1, lcreedo1@jhu.edu
 */

package hw6;

import exceptions.EmptyException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * Priority queue implemented as a binary heap.
 *
 * Use the ranked array representation of a binary heap!
 * Keep the arithmetic simple by sticking a null into slot 0 of the
 * ArrayList; wasting one reference is an okay price to pay for nicer
 * code.
 * @param <T> Element type.
 */
public class BinaryHeapPriorityQueue<T extends Comparable<? super T>>
    implements PriorityQueue<T> {

    /**
     * The default comparator uses the "natural" ordering.
     * @param <T> Element type.
     */
    private static class DefaultComparator<T extends Comparable<? super T>>
        implements Comparator<T> {
        /**
         * Compare method.
         * @param t1 first element to be compared.
         * @param t2 second element to be compared.
         * @return positive if t1 > t2, 0 if equal, negative if t1 < t2.
         */
        public int compare(T t1, T t2) {
            return t1.compareTo(t2);
        }
    }

    /** ArrayList to use as heap. */
    private ArrayList<T> list;
    /** Comparator defining ordering. */
    private Comparator<T> cmp;

    /**
     * A binary heap using the "natural" ordering of T.
     */
    public BinaryHeapPriorityQueue() {
        this(new DefaultComparator<>());
    }

    /**
     * A binary heap using the given comparator for T.
     * @param cmp Comparator to use.
     */
    public BinaryHeapPriorityQueue(Comparator<T> cmp) {
        this.cmp = cmp;
        this.list = new ArrayList<T>();
        this.list.add(0, null);
    }


    @Override
    public void insert(T t) {
        this.list.add(this.list.size(), t);
        bubbleUp(this.list.size() - 1, t);
    }

    /**
     * takes new input and moves up the heap to proper location.
     * @param j location in ArrayList.
     * @param t data at location j.
     */
    public void bubbleUp(int j, T t) {
        int pL = this.parent(j);
        T parent = this.list.get(pL);
        if (parent.compareTo(t) < 0) {
            swap(pL, j);
            bubbleUp(pL, t);
        }
    }

    /**
     * takes root and moves it down the heap to the proper location.
     * @param p location of root node.
     */
    public void bubbleDown(int p) {
        int cL = this.lchild(p);
        int cR = this.rchild(p);
        int big = p;

        if (cL < this.list.size() - 1 &&
            this.list.get(cL).compareTo(this.list.get(p)) > 0) {
            big = cL;
        }
        if (cR < this.list.size() - 1 &&
            this.list.get(cR).compareTo(this.list.get(big)) > 0) {
            big = cR;
        }

        if (big != p) {
            swap(p, big);
            bubbleDown(big);
        }

    }

    /**
     * Returns parent of current location in heap.
     * @param j location in ArrayList.
     * @return location of parent.
     */
    public int parent(int j) {
        if (j == 1) {
            return 1;
        }
        return j / 2;
    }

    /**
     * Returns left child of current location in heap.
     * @param j location in ArrayList.
     * @return location of left child.
     */
    public int lchild(int j) {
        return (2 * j);
    }

    /**
     * Returns right child of current location in heap.
     * @param j location in ArrayList.
     * @return location of right child.
     */
    public int rchild(int j) {
        return (2 * j + 1);
    }

    /**
     * Swaps two values in array.
     * @param t1 first value.
     * @param t2 second value.
     */
    public void swap(int t1, int t2) {
        Collections.swap(this.list, t1, t2);
    }

    @Override
    public void remove() throws EmptyException {
        if (this.empty()) {
            throw new EmptyException();
        }
        swap(1, this.list.size() - 1);
        this.list.remove(this.list.size() - 1);
        bubbleDown(1);
    }

    @Override
    public T best() throws EmptyException {
        if (this.empty()) {
            throw new EmptyException();
        }
        return this.list.get(1);
    }

    @Override
    public boolean empty() {
        return this.list.size() == 1;
    }

}
