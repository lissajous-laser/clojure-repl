package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Modulus function.
 */
public class Mod implements Function {

    public Token getName() {
        return new Token("mod");
    }

    /**
     * Calculates modulus of two numbers.
     */
    public Token applyFn(TokensList tokens)
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
