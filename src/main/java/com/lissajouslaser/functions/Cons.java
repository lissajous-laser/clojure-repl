package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.SyntaxException;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Clojure cons function, for 'construct'.
 */
public class Cons implements Function {

    public boolean isEvalutionNormal() {
        return true;
    }

    public Token getName() {
        return new Token("cons");
    }

    /**
     * Adds an element at the front of a list.
     */
    public TokensList applyFn(TokensList tokens)
            throws SyntaxException, ArityException {

        final int validSize = 3;

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/cons");
        }
        // Check third arg is a list.
        TokensList thirdArg = (TokensList) tokens.get(2);
        if (((Token) thirdArg.get(0)).toString().equals("list")) {

            TokensList list = new TokensList(thirdArg);
            list.add(1, tokens.get(1));

            return list;
        }
        throw new SyntaxException("Illegal list passed to clojure.core/cons");
    }
}