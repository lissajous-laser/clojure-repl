package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Greater than function.
 */
public class GreaterThan implements Function {

    public boolean isEvalutionNormal() {
        return true;
    }

    public Token getName() {
        return new Token(">");
    }

    /**
     * Determines if each successive element is smaller,
     * eg. an expression (> 3 2) or (> 3 2 1 0) is
     * equivalent to 3 > 2 or 3 > 2 > 1 > 0 and evaluates
     * to true. A single argument expression eg. (> 3)
     * evaluates to true.
     */
    public Token applyFn(TokensList tokens)
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
}
