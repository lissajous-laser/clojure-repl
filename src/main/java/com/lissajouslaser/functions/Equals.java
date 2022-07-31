package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;
import com.lissajouslaser.TokensListOrToken;

/**
 * Equals function.
 */
public class Equals implements Function {

    public Token getName() {
        return new Token("=");
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
    public Token applyFn(TokensList tokens) throws ArityException {
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
