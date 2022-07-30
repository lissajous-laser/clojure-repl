package com.lissajouslaser;

/**
 * Contains functions for checking the 'type' of a Token's
 * encapsulated value.
 */
final class CheckType {

    private CheckType() {}

    /**
     * Checks if you have a list.
     */
    static boolean isList(String expr) {
        return expr.trim().matches("\\(.*\\)");
    }

    /**
     * Check if you have a number.
     */
    static boolean isNumber(String expr) {
        return expr.trim().matches("-?\\d+");
    }

    /**
     * Check if you have a symbol in the valid format.
     */
    static boolean isValidSymbol(String expr) {
        boolean matchesRegex = expr.trim().matches("[A-Za-z_-][\\w-?]*");
        return matchesRegex && !isBool(expr);
    }

    /**
     * Check if you have a boolean. nil is included here for
     * convenience.
     */
    static boolean isBool(String expr) {
        return expr.trim().matches("true|false|nil");
    }

    /**
     * Check that parameters list in a function is valid.
     * Must be a un-nested list.
     */
    static boolean isParamList(String expr) {
        return expr.trim().matches("\\([^\\(\\)]*\\)");
    }
}

