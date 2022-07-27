package com.lissajouslaser;

import java.util.Arrays;

/**
 * Utility class which defines basic arithmetic functions
 * in the langauge.
 */
public final class CoreFunctionsArithmetic {

    private CoreFunctionsArithmetic() {}

    /**
     * Add arguments.
     */
    public static String add(String[] args) {
        try {
            int sum = Arrays.stream(args)
                    .mapToInt(x -> Integer.valueOf(x))
                    .reduce(0, (x, y) -> x + y);
            return String.valueOf(sum);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        }
    }

    /**
     * Subtract from first arg the rest of the args.
     */
    public static String sub(String[] args) {
        try {
            int initial = Integer.valueOf(args[0]);

            // The args that will be subtracted from the
            // initial value.
            String[] subtractors = Arrays.copyOfRange(args, 1, args.length);

            int subtraction = Arrays.stream(subtractors)
                    .mapToInt(x -> Integer.valueOf(x))
                    .reduce(initial, (x, y) -> x - y);
            return String.valueOf(subtraction);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        }
    }

    /**
     * Multiply args.
     */
    public static String mul(String[] args) {
        try {
            int product = Arrays.stream(args)
                    .mapToInt(x -> Integer.valueOf(x))
                    .reduce(1, (x, y) -> x * y);
            return String.valueOf(product);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        }
    }

    /**
     * Divide from first arg the rest of the args.
     */
    public static String div(String[] args) {
        try {
            int initial = Integer.valueOf(args[0]);

            // The args that will be subtracted from the
            // initial value.
            String[] divisors = Arrays.copyOfRange(args, 1, args.length);

            int division = Arrays.stream(divisors)
                    .mapToInt(x -> Integer.valueOf(x))
                    .reduce(initial, (x, y) -> x / y);
            return String.valueOf(division);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        } catch (ArithmeticException e) {
            return "Error - Cannot divide by zero";
        }
    }

    /**
     * Calculates modulus of two numbers.
     */
    public static String mod(String[] args) {
        if (args.length != 2) {
            return "Error - Wrong number of args passed to clojure.core/mod";
        }
        try {
            int modulus = Integer.valueOf(args[0]) % Integer.valueOf(args[1]);
            return String.valueOf(modulus);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        }
    }
}
