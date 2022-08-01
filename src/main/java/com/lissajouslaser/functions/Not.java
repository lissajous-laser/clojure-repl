package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.CheckType;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Not function.
 */
public class Not implements Function {

    public boolean isEvalutionNormal() {
        return true;
    }

    public Token getName() {
        return new Token("not");
    }

    /**
     * Gives the logical opposite of a value.
     * Passing nil or false returns true. Passing anything
     * else returns false.
     */
    public Token applyFn(TokensList tokens)
            throws ArityException, ClassCastException {
        if (tokens.size() != 2) {
            throw new ArityException("clojure.core/not");
        } else {
            String value = ((Token) tokens.get(1)).toString();

            return new Token(Boolean.toString(CheckType.isLogicalFalse(value)));
        }
    }
}
