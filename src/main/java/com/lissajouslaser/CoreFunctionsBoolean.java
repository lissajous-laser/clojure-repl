package com.lissajouslaser;

/**
 * Utility class which defines basic functions
 * that operate on boolean values. In clojure,
 * all values that are not false or nil are
 * considered truthy. False and nil are falsey.
 */
public final class CoreFunctionsBoolean {

    private CoreFunctionsBoolean() {
    }

    /**
     * And operator. Returns the first falsey value, if there
     * are no falsey values returns the last truthy value.
     * eg. (and nil false 3) returns nil,
     * (and true 3) returns 3.
     */
    public static Token and(TokensList tokens)
            throws ArityException, ClassCastException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/and");
        }
        if (tokens.size() == 2) {
            return (Token) tokens.get(1);
        }
        Token currentToken = null;

        for (int i = 1; i < tokens.size(); i++) {
            currentToken = (Token) tokens.get(i);

            if (isNilOrFalse(currentToken.toString())) {
                return currentToken;
            }
        }
        return currentToken;
    }

    /**
     * Or operator. Returns the first truthy value, if there
     * are no truthy values returns the last falsey value.
     * eg. (or false nil 3) returns nil,
     * (or 3 true) returns 3.
     */
    public static Token or(TokensList tokens)
            throws ArityException, ClassCastException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/or");
        }
        if (tokens.size() == 2) {
            return (Token) tokens.get(1);
        }
        Token currentToken = null;

        for (int i = 1; i < tokens.size(); i++) {
            currentToken = (Token) tokens.get(i);

            if (!isNilOrFalse(currentToken.toString())) {
                return currentToken;
            }
        }
        return currentToken;
    }

    /**
     * Not operator, gives the logical opposite of a value.
     * Passing nil or false returns true. Passing anything
     * else returns false.
     */
    public static Token not(TokensList tokens)
            throws ArityException, ClassCastException {
        if (tokens.size() != 2) {
            throw new ArityException("clojure.core/not");
        } else {
            String value = ((Token) tokens.get(1)).toString();

            return new Token(Boolean.toString(isNilOrFalse(value)));
        }
    }

    /**
     * Test to see if a value is falsey.
     */
    static boolean isNilOrFalse(String val) {
        return "false".equals(val) || "nil".equals(val);
    }
}
