package com.lissajouslaser;

import java.util.Arrays;

/**
 * Utility class which defines basic functions
 * that operate on boolean values. In clojure,
 * all values that are not false or nil are
 * considered truthy.
 */
public final class CoreFunctionsBoolean {

    private CoreFunctionsBoolean() {}

    /**
     * And operator.
     */
    public static String and(String[] args) {
        String bool = Arrays.stream(args)
                .reduce("true", (x, y) ->
                !isNilOrFalse(x) && !isNilOrFalse(y) ? "true" : "false");
        return bool;
    }

    /**
     * Or operator.
     */
    public static String or(String[] args) {
        String bool = Arrays.stream(args)
                .reduce("false", (x, y) ->
                !isNilOrFalse(x) || !isNilOrFalse(y) ? "true" : "false");
        return bool;
    }

    /**
     * Not operator, gives the logical opposite of a value.
     */
    public static String not(String[] args) {
        if (args.length != 1) {
            return "Error - wrong number of args passed to clojure.core/def";
        } else {
            return Boolean.toString(isNilOrFalse(args[0]));
        }
    }

    /**
     * Test to see if a value is falsey.
     */
    static boolean isNilOrFalse(String val) {
        return "false".equals(val) || "nil".equals(val);
    }
}
