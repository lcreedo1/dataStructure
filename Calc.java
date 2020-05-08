/**
 * Liam Creedon
 * lcreedo1
 * lcreedo1@jhu.edu
 * Calc.java
 */


package hw4;

import java.util.Scanner;

import exceptions.EmptyException;

/** A program for an RPN calculator that uses a stack. */
public final class Calc {

    /** Stack to be used for calculator. */
    public static Stack<String> stack;

    /** Hush checkstyle. */
    private Calc() {}

    // Elements being popped still when operations are invalid based
    // on number of elements

    /** Helper method for sums. */
    private static void add() {
        try {
            int a = Integer.parseInt(stack.top());
            stack.pop();
            int b;
            if (!stack.empty()) {
                b = Integer.parseInt(stack.top());
                stack.pop();
            } else {
                stack.push(Integer.toString(a));
                throw new EmptyException();
            }
            String sum = Integer.toString(a + b);
            stack.push(sum);
        }
        catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments");
        }
    }

    /** Helper method for differences. */
    private static void sub() {
        try {
            int a = Integer.parseInt(stack.top());
            stack.pop();
            int b;
            if (!stack.empty()) {
                b = Integer.parseInt(stack.top());
                stack.pop();
            } else {
                stack.push(Integer.toString(a));
                throw new EmptyException();
            }
            String dif = Integer.toString(b - a);
            stack.push(dif);
        }
        catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments");
        }
    }

    /** Helper method for products. */
    private static void mult() {
        try {
            int a = Integer.parseInt(stack.top());
            stack.pop();
            int b;
            if (!stack.empty()) {
                b = Integer.parseInt(stack.top());
                stack.pop();
            } else {
                stack.push(Integer.toString(a));
                throw new EmptyException();
            }
            String pro = Integer.toString(a * b);
            stack.push(pro);
        }
        catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments");
        }
    }

    /** Helper method for quotients. */
    private static void div() {
        try {
            int a = Integer.parseInt(stack.top());
            stack.pop();
            int b;
            if (!stack.empty()) {
                b = Integer.parseInt(stack.top());
                stack.pop();
            } else {
                stack.push(Integer.toString(a));
                throw new EmptyException();
            }
            String quo = Integer.toString(b / a);
            stack.push(quo);
        }
        catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments");
        }
    }

    /** Helper method for remainders. */
    private static void mod() {
        try {
            int a = Integer.parseInt(stack.top());
            stack.pop();
            int b;
            if (!stack.empty()) {
                b = Integer.parseInt(stack.top());
                stack.pop();
            } else {
                stack.push(Integer.toString(a));
                throw new EmptyException();
            }
            String rem = Integer.toString(b % a);
            stack.push(rem);
        }
        catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments");
        }
    }

    /**
     * Helper method to validate non-operand user input.
     * @param num Input from keyboard.
     */
    private static void validString(String num) {
        try {
            Integer.parseInt(num);
            stack.push(num);
        }
        catch (NumberFormatException e) {
            System.out.println("ERROR: bad token");
        }
    }

    /**
     * Helper method to select function.
     * @param in Input from keyboard.
     * @return Bool to determine EOF, only true if in = !.
     */
    private static boolean input(String in) {
        boolean stop = false;
        switch (in) {
            case "?":
                System.out.println(stack.toString());
                break;

            case ".":
                try {
                    String top = stack.top();
                    stack.pop();
                    System.out.println(top);
                }
                catch (EmptyException e) {
                    System.out.println("ERROR: not enough arguments");
                }
                break;

            case "!":
                stop = true;
                break;

            case "+":
                add();
                break;

            case "-":
                sub();
                break;

            case "*":
                mult();
                break;

            case "/":
                div();
                break;

            case "%":
                mod();
                break;

            default:
                validString(in);
        }
        return stop;
    }

    /**
     * The main function.
     * @param args Not used.
     */
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        stack = new ArrayStack<String>();
        String a;
        boolean exit = false;

        while (!exit && kb.hasNext()) {
            a = kb.next();
            exit = input(a);
        }
        kb.close();
    }
}
