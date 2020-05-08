/* Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * TenCounter.java
 */

package hw1;

/** A counter for powers of 10. */
public class TenCounter implements ResetableCounter {

    /** current value of the counter. */
    private int val;

    /** Construct a new TenCounter. */
    public TenCounter() {
        this.val = 1;
    }

    @Override
    public void reset() {
        this.val = 1;
    }

    @Override
    public int value() {
        return this.val;
    }

    @Override
    public void up() {
        this.val = this.val * 10;
    }

    @Override
    public void down() {
        if (this.val / 10 < 1) {
            this.val = 1;
        }
        else {
            this.val = this.val / 10;
        }
    }
}
