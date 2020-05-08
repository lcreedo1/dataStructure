/**
 * Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * InsertionSort.java
 */

package hw3;

import hw2.Array;


/**
 * The Insertion Sort algorithm.
 * @param <T> Element type.
 */
public final class InsertionSort<T extends Comparable<T>>
    implements SortingAlgorithm<T> {


    @Override
    public void sort(Array<T> array) {

        int n = array.length();
        for (int i = 1; i < n; i++) {

            T key = array.get(i);
            int j = i - 1;
            while (j >= 0 && array.get(j).compareTo(key) > 0) {
                array.put(j + 1, array.get(j));
                j--;
            }

            array.put(j + 1, key);
        }
    }

    @Override
    public String name() {
        return "Insertion Sort";
    }
}
