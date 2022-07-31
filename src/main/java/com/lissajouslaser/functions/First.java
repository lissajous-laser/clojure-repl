package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.SyntaxException;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;
import com.lissajouslaser.TokensListOrToken;

/**
 * Clojure first function.
 */
public class First implements Function {

    public Token getName() {
        return new Token("first");
    }

    /**
     * Returns the first element of a list. Passing an empty list
     * to first returns nil.
     */
    public TokensListOrToken applyFn(TokensList tokens)
            throws SyntaxException, ArityException {

        if (tokens.size() != 2) {
            throw new ArityException("clojure.core/first");
        }
        // Check if second arg is a list.
        TokensListOrToken secondArg = tokens.get(1);
        if (secondArg instanceof TokensList
                && ((TokensList) secondArg).get(0) instanceof Token
                && ((Token) ((TokensList) secondArg).get(0)).toString().equals("list")) {

            TokensList list = (TokensList) secondArg;

            if (list.size() == 1) {
                return new Token("nil"); // for empty list
            }
            return list.get(1);
        }
        throw new SyntaxException("Illegal list passed to clojure.core/first");
    }
}
