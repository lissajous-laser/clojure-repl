package com.lissajouslaser;

/**
 * Utility class which defines basic arithmetic functions
 * in the langauge.
 */
public final class CoreFunctionsArithmetic {

    private CoreFunctionsArithmetic() {
    }

    /**
     * Add arguments. Unary operation is 0 + argument.
     */
    public static Token add(TokensList tokens)
            throws NumberFormatException, ArityException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/+");
        }
        int sum = 0;

        for (int i = 1; i < tokens.size(); i++) {
            sum += Integer.valueOf(((Token) tokens.get(i)).toString());
        }
        return new Token(String.valueOf(sum));
    }

    /**
     * Subtract from first argument the rest of the arguments.
     * Unary operation is 0 - argument.
     */
    public static Token sub(TokensList tokens)
            throws NumberFormatException, ArityException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/-");
        }
        int subtraction;
        int initial = Integer.valueOf(((Token) tokens.get(1)).toString());
        // If there is a single number argument n, the function
        // evaluates 0 - n.
        if (tokens.size() == 2) {
            subtraction = -initial;
        } else {
            subtraction = initial;

            for (int i = 2; i < tokens.size(); i++) {
                subtraction -= Integer.valueOf(((Token) tokens.get(i)).toString());
            }
        }
        return new Token(String.valueOf(subtraction));

    }

    /**
     * Multiply arguments. Unary operation is 1 * argument.
     */
    public static Token mul(TokensList tokens)
            throws NumberFormatException, ArityException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/*");
        }
        int product = 1;

        for (int i = 1; i < tokens.size(); i++) {
            product *= Integer.valueOf(((Token) tokens.get(i)).toString());
        }
        return new Token(String.valueOf(product));
    }

    /**
     * Divide from first argument the rest of the arguments.
     * Unary operation is 1 / argument.
     */
    public static Token div(TokensList tokens)
            throws ArithmeticException, NumberFormatException, ArityException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core//");
        }
        int division;
        int initial = Integer.valueOf(((Token) tokens.get(1)).toString());
        // If there is a single number argument n, the function
        // evaluates 1 / n.
        if (tokens.size() == 2) {
            division = 1 / initial;
        } else {
            division = initial;

            for (int i = 2; i < tokens.size(); i++) {
                division /= Integer.valueOf(((Token) tokens.get(i)).toString());
            }
        }
        return new Token(String.valueOf(division));
    }

    /**
     * Calculates modulus of two numbers.
     */
    public static Token mod(TokensList tokens)
            throws ArithmeticException, NumberFormatException, ArityException {

        final int validSize = 3;

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/mod");
        }
        int modDividend = Integer.valueOf(((Token) tokens.get(1)).toString());
        int modDivisor = Integer.valueOf(((Token) tokens.get(2)).toString());
        int modulus = modDividend % modDivisor;
        return new Token(String.valueOf(modulus));
    }
}
