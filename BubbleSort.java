/**
 * Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * BubbleSort.java
 */

package hw3;

import hw2.Array;

/**
 * The Bubble Sort algorithm with the optimized "quick" break to exit
 * if the array is sorted.
 * @param <T> The type being sorted.
 */
public final class BubbleSort<T extends Comparable<T>>
    implements SortingAlgorithm<T> {

    @Override
    public void sort(Array<T> array) {
        int n = array.length();
        boolean sorted;
        for (int i = n - 1; i >= 0; i--) {
            sorted = true;
            for (int j = 0; j < i; j++) {
                if (array.get(j).compareTo(array.get(j + 1)) > 0) {
                    T temp = array.get(j);
                    array.put(j, array.get(j + 1));
                    array.put(j + 1, temp);
                    sorted = false;
                }
            }
            if (sorted) {
                i = -1;
            }
        }
    }

    @Override
    public String name() {
        return "Bubble Sort";
    }
}
