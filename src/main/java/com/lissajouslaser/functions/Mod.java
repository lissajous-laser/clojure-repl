package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Modulo function.
 */
public class Mod implements Function {

    public boolean isDefinitionCreator() {
        return false;
    }

    public boolean isEvaluationNormal() {
        return true;
    }

    public Token getName() {
        return new Token("mod");
    }

    /**
     * Calculates modulo of two numbers. Returns highest
     * number not exceding the divisor.
     */
    public Token applyFn(TokensList tokens)
            throws ArithmeticException, NumberFormatException, ArityException {

        final int validSize = 3;

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/mod");
        }
        int dividend = Integer.valueOf(((Token) tokens.get(1)).toString());
        int divisor = Integer.valueOf(((Token) tokens.get(2)).toString());

        int modulus = Math.floorMod(dividend, divisor);

        return new Token(String.valueOf(modulus));
    }
}
