package com.lissajouslaser;

/**
 * Utility class which defines basic comparator functions
 * in the langauge.
 */
public final class CoreFunctionsComparator {

    private CoreFunctionsComparator() {
    }

    /**
     * Determines if each successive element is greater,
     * eg. an expression (< 3 4) or (< 3 4 5 6) is
     * equivalent to 3 < 4 or 3 < 4 < 5 < 6 and evaluates
     * to true. A single argument expression eg. (< 3) 
     * evaluates to true.
     */
    public static Token lt(TokensList tokens)
            throws NumberFormatException, ArityException, ClassCastException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/<");
        }
        int num1 = Integer.MIN_VALUE;

        for (int i = 1; i < tokens.size(); i++) {
            int num2 = Integer.valueOf(((Token) tokens.get(i)).toString());

            if (num1 < num2) {
                num1 = num2;
            } else {
                num1 = Integer.MAX_VALUE; // fail case
            }
        }
        if (num1 == Integer.MAX_VALUE) {
            return new Token("false");
        }
        return new Token("true");
    }

    /**
     * Determines if each successive element is smaller,
     * eg. an expression (> 3 2) or (> 3 2 1 0) is
     * equivalent to 3 > 2 or 3 > 2 > 1 > 0 and evaluates
     * to true. A single argument expression eg. (> 3) 
     * evaluates to true.
     */
    public static Token gt(TokensList tokens)
            throws NumberFormatException, ArityException, ClassCastException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/>");
        }
        int num1 = Integer.MAX_VALUE;

        for (int i = 1; i < tokens.size(); i++) {
            int num2 = Integer.valueOf(((Token) tokens.get(i)).toString());

            if (num1 > num2) {
                num1 = num2;
            } else {
                num1 = Integer.MIN_VALUE; // fail case
            }
        }
        if (num1 == Integer.MIN_VALUE) {
            return new Token("false");
        }
        return new Token("true");
    }

    /**
     * Determines if each successive element is equal
     * to the prior ones, looks for value equality rather
     * than identity equality.
     * eg. an expression like (= false false false) or
     * (= (list 1 2) (list 1 2))
     * evaluates to true. A single arg expression (= 3)
     * evaluates to true.
     */
    public static Token eq(TokensList tokens) throws ArityException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/=");
        }
        if (tokens.size() == 2) {
            return new Token("true");
        }
        TokensListOrToken item1 = tokens.get(1);

        for (int i = 2; i < tokens.size(); i++) {
            TokensListOrToken item2 = tokens.get(i);

            if (item1.equals(item2)) {
                item1 = item2;
            } else {
                item1 = null; // fail case
            }
        }
        if (item1 == null) {
            return new Token("false");
        }
        return new Token("true");
    }
}
