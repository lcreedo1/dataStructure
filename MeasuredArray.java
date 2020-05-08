/**
 * Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * MeasuredArray.java
 */

package hw3;

import exceptions.IndexException;
import hw2.SimpleArray;

/**
 * An Array that is able to report the number of accesses and mutations,
 * as well as reset those statistics.
 * @param <T> The type of the array.
 */
public class MeasuredArray<T> extends SimpleArray<T> implements Measured<T> {
    /** Number of accesses to the array. */
    private int numAccess;
    /** Number of mutations to the array. */
    private int numMutation;
    /** Length of the array. */
    private int len;


    /**
     * Constructor for a MeasuredArray that calls the SimpleArray constructor.
     * @param n The size of the array.
     * @param t The initial value to set every object to in the array..
     */
    public MeasuredArray(int n, T t) {
        super(n, t);
        numAccess = 0;
        numMutation = 0;
        len = n;
    }

    @Override
    public int length() {
        this.numAccess++;
        return super.length();
    }

    @Override
    public T get(int i) {
        this.numAccess++;
        return super.get(i);
    }

    @Override
    public void put(int i, T t) throws IndexException {
        try {
            super.put(i, t);
            this.numMutation++;
        }
        catch (IndexException e) {
            throw new IndexException();
        }
    }

    @Override
    public void reset() {
        this.numMutation = 0;
        this.numAccess = 0;
    }

    @Override
    public int accesses() {
        return this.numAccess;
    }

    @Override
    public int mutations() {
        return this.numMutation;
    }

    @Override
    public int count(T t) {
        int inst = 0;
        for (int i = 0; i < this.len; i++) {
            if (get(i) == t) {
                inst++;
            }
        }
        return inst;
    }
}
