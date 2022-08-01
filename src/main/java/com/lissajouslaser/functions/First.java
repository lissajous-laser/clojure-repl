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

    public boolean isEvalutionNormal() {
        return true;
    }

    public Token getName() {
        return new Token("first");
    }

    /**
     * Returns the first element of a list. Passing an empty list
     * to first returns nil.
     */
    public TokensListOrToken applyFn(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {

        if (tokens.size() != 2) {
            throw new ArityException("clojure.core/first");
        }
        // Check if second arg is a list.
        TokensList secondArg = (TokensList) tokens.get(1);
        if (((Token) secondArg.get(0)).toString().equals("list")) {

            TokensList list = secondArg;

            if (list.size() == 1) {
                return new Token("nil"); // for empty list
            }
            return list.get(1);
        }
        throw new SyntaxException("Illegal list passed to clojure.core/first" + tokens);
    }
}

