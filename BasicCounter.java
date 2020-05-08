/* Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * BasicCounter.java
 */

package hw1;

/** A counter that increments and decrements by 1. */
public class BasicCounter implements ResetableCounter {

    /** current value of the counter. */
    private int val;

    /** Construct a new BasicCounter. */
    public BasicCounter() {
        this.val = 0;
    }

    @Override
    public void reset() {
        this.val = 0;
    }

    @Override
    public int value() {
        return this.val;
    }

    @Override
    public void up() {
        this.val++;
    }

    @Override
    public void down() {
        this.val--;
    }
}
