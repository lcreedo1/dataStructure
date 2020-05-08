/* Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * FlexibleCounter.java
 */

package hw1;

/** Class for a counter with flexible starting and incrementing values. */
public class FlexibleCounter implements ResetableCounter {
    /** current value of the counter. */
    private int val;

    /** increment value of the counter. */
    private int inc;

    /** initial value of the counter. */
    private int init;

    /**
     * Construct a new FlexibleCounter.
     * @param initialValue The value to start at.
     * @param incrementValue The value to increment the counter by.
     * @throws IllegalArgumentException If incrementValue is negative.
     */
    public FlexibleCounter(int initialValue, int incrementValue) {
        this.val = initialValue;
        this.init = initialValue;
        if (incrementValue < 1) {
            throw new IllegalArgumentException("Increment value" +
            " must be positive");
        } else {
            this.inc = incrementValue;
        }
    }

    @Override
    public void reset() {
        this.val = this.init;
    }

    @Override
    public int value() {
        return this.val;
    }

    @Override
    public void up() {
        this.val = this.val + this.inc;
    }

    @Override
    public void down() {
        this.val = this.val - this.inc;
    }
}
