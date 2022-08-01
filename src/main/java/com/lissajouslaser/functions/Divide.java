package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Divide function.
 */
public class Divide implements Function {

    public boolean isEvalutionNormal() {
        return true;
    }

    public Token getName() {
        return new Token("/");
    }

    /**
     * Divide from first argument the rest of the arguments.
     * Unary operation is 1 / argument.
     */
    public Token applyFn(TokensList tokens)
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
}
