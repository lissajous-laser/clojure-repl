package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Subtract function.
 */
public class Add implements Function {

    public Token getName() {
        return new Token("+");
    }

    /**
     * Add arguments. Unary operation is 0 + argument.
     */
    public Token applyFn(TokensList tokens)
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
}
