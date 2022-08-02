package com.lissajouslaser;

/**
 * Contains functions for checking the 'type' of a Token's
 * encapsulated value.
 */
public final class CheckType {

    private CheckType() {}

    /**
     * Checks if you have a list.
     */
    public static boolean isList(String expr) {
        return expr.matches("\\(.*\\)");
    }

    /**
     * Check if you have a number.
     */
    public static boolean isNumber(String expr) {
        return expr.matches("-?\\d+");
    }

    /**
     * Check if you have a symbol in the valid format.
     */
    public static boolean isValidSymbol(String expr) {
        boolean matchesRegex = expr.matches("[A-Za-z_\\-*+/=<>][\\w\\-?]*");
        return matchesRegex && !isBool(expr);
    }

    /**
     * Check if you have a boolean. nil is included here for
     * convenience.
     */
    public static boolean isBool(String expr) {
        return "false".equals(expr)
                || "nil".equals(expr)
                || "true".equals(expr);
    }

    /**
     * Check that parameters list in a function is valid.
     * Must be a un-nested list.
     */
    public static boolean isParamList(String expr) {
        return expr.matches("[\\(\\[][^\\(\\)]*[\\)\\]]");
    }

    /**
     * Test to see if a value is falsey.
     */
    public static boolean isLogicalFalse(String expr) {
        return "false".equals(expr) || "nil".equals(expr);
    }
}

