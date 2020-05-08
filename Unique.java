/** Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * Unique.java
 */

package hw1;

/** A class with a main method for printing out unique numbers. */
public final class Unique {

    /** Make checkstyle happy. */
    private Unique() {
        throw new AssertionError("Can not instantiate class Unique\n");
    }

    /**
     * A main method to print the unique numerical command line arguments.
     * @param args The string array of arguments in the command line.
     */
    public static void main(String[] args) {
        String num = "";
        int len = args.length;
        /* Try and catch block to confirm command
         * arguments are integer type
         */
        try {
            for (int j = 0; j < len; j++) {
                int arg = Integer.parseInt(args[j]);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Command line arguments must" +
              " be integer type\n");
        }

        /* Parses args and prints non repeated numbers */
        for (int i = 0; i < len; i++) {
            num = args[i];
            boolean repeat = false;
            for (int j = i - 1; j >= 0; j--) {
                if (num.equals(args[j])) {
                    repeat = true;
                }
            }
            if (!repeat) {
                System.out.println(num);
            }
        }
    }
}
