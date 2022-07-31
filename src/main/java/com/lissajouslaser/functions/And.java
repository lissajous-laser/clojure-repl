package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * And function.
 */
public class And implements Function {

    public Token getName() {
        return new Token("and");
    }

    /**
     * Returns the first falsey value, if there
     * are no falsey values returns the last truthy value.
     * eg. (and nil false 3) returns nil,
     * (and true 3) returns 3.
     */
    public Token applyFn(TokensList tokens)
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
     * Test to see if a value is falsey.
     */
    static boolean isNilOrFalse(String val) {
        return "false".equals(val) || "nil".equals(val);
    }
}
