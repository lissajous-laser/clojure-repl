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
    public static String add(String[] args) throws NumberFormatException {
        int sum = Arrays.stream(args)
                .mapToInt(x -> Integer.valueOf(x))
                .reduce(0, (x, y) -> x + y);
        return String.valueOf(sum);
    }

    /**
     * Subtract from first arg the rest of the args.
     */
    public static String sub(String[] args) throws NumberFormatException {
        int initial = Integer.valueOf(args[0]);

        // The args that will be subtracted from the
        // initial value.
        String[] subtractors = Arrays.copyOfRange(args, 1, args.length);

        int subtraction = Arrays.stream(subtractors)
                .mapToInt(x -> Integer.valueOf(x))
                .reduce(initial, (x, y) -> x - y);
        return String.valueOf(subtraction);
    }

    /**
     * Multiply args.
     */
    public static String mul(String[] args) throws NumberFormatException {
        int product = Arrays.stream(args)
                .mapToInt(x -> Integer.valueOf(x))
                .reduce(1, (x, y) -> x * y);
        return String.valueOf(product);
    }

    /**
     * Divide from first arg the rest of the args.
     */
    public static String div(String[] args)
            throws ArithmeticException, NumberFormatException {
        int initial = Integer.valueOf(args[0]);

        // The args that will be subtracted from the
        // initial value.
        String[] divisors = Arrays.copyOfRange(args, 1, args.length);

        int division = Arrays.stream(divisors)
                .mapToInt(x -> Integer.valueOf(x))
                .reduce(initial, (x, y) -> x / y);
        return String.valueOf(division);
    }

    /**
     * Calculates modulus of two numbers.
     */
    public static String mod(String[] args)
            throws ArithmeticException, NumberFormatException, ArityException {
        if (args.length != 2) {
            throw new ArityException("clojure.core/mod");
        }
        int modulus = Integer.valueOf(args[0]) % Integer.valueOf(args[1]);
        return String.valueOf(modulus);
    }
}
