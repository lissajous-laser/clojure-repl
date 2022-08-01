package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Multiply function.
 */
public class Multiply implements Function {

    public boolean isEvalutionNormal() {
        return true;
    }

    public Token getName() {
        return new Token("*");
    }

    /**
     * Multiply arguments. Unary operation is 1 * argument.
     */
    public Token applyFn(TokensList tokens)
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
}
