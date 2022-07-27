package com.lissajouslaser;

import java.util.Arrays;

/**
 * Utility class which defines basic comparator functions
 * in the langauge.
 */
public final class CoreFunctionsComparator {

    private CoreFunctionsComparator() {}

    /**
     * Determines if each successive element is greater,
     * eg. an expression (< 3 4) or (< 3 4 5 6) is
     * equivalent to 3 < 4 or 3 < 4 < 5 < 6 and evaluates
     * to true. A single arg expression (< 3) evaluates
     * to true.
     */
    public static String lt(String[] args) throws NumberFormatException {
        int result = Arrays.stream(args)
                .mapToInt(x -> Integer.valueOf(x))
                .reduce(Integer.MIN_VALUE, (x, y) -> x < y ? y : Integer.MAX_VALUE);
        if (result == Integer.MAX_VALUE) {
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * Determines if each successive element is smaller,
     * eg. an expression (> 3 2) or (> 3 2 1 0) is
     * equivalent to 3 > 2 or 3 > 2 > 1 > 0 and evaluates
     * to true. A single arg expression (> 3) evaluates
     * to true.
     */
    public static String gt(String[] args) throws NumberFormatException {
        int result = Arrays.stream(args)
                .mapToInt(x -> Integer.valueOf(x))
                .reduce(Integer.MAX_VALUE, (x, y) -> x > y ? y : Integer.MIN_VALUE);
        if (result == Integer.MIN_VALUE) {
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * Determines if each successive element is equal,
     * eg. an expression (= 3 3) or (= false false false)
     * evaluates to true. A single arg expression (= 3)
     * evaluates to true.
     */
    public static String eq(String[] args) {
        String init = args[0];

        String result = Arrays.stream(args)
                .reduce(init, (x, y) -> x.equals(y) ? y : "¡!¡!");
        if ("¡!¡!".equals(result)) {
            return "false";
        } else {
            return "true";
        }
    }
}
