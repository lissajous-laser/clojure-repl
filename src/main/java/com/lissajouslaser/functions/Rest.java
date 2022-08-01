package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.SyntaxException;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Clojure rest function.
 */
public class Rest implements Function {

    public boolean isEvalutionNormal() {
        return true;
    }

    public Token getName() {
        return new Token("rest");
    }

    /**
     * Returns a list without its first element. Passing an
     * empty list to rest returns an empty list.
     */
    public TokensList applyFn(TokensList tokens)
            throws SyntaxException, ArityException {
        // The only arg passed to first should be a single list.
        // First check there is one arg.
        if (tokens.size() != 2) {
            throw new ArityException("clojure.core/rest");
        }
        // Check arg is a list.
        TokensList secondArg = (TokensList) tokens.get(1);
        if (((Token) secondArg.get(0)).toString().equals("list")) {

            TokensList list = new TokensList(secondArg);

            if (list.size() > 1) {
                list.remove(1);
            }
            return list;
        }
        throw new SyntaxException("Illegal list passed to clojure.core/rest");
    }
}
