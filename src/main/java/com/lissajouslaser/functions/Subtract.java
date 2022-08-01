package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Subtract function.
 */
public class Subtract implements Function {

    public boolean isEvalutionNormal() {
        return true;
    }

    public Token getName() {
        return new Token("-");
    }

    /**
     * Subtract from first argument the rest of the arguments.
     * Unary operation is 0 - argument.
     */
    public Token applyFn(TokensList tokens)
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
}
