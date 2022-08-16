package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.Function;
import com.lissajouslaser.SyntaxException;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Clojure empty? function.
 */
public class Empty implements Function {

    public boolean isDefinitionCreator() {
        return false;
    }

    public boolean isEvaluationNormal() {
        return true;
    }

    public Token getName() {
        return new Token("empty?");
    }

    /**
     * Returns a 'boolean' indicating if the list passed to it
     * is empty empty or not.
     */
    public Token applyFn(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {

        if (tokens.size() != 2) {
            throw new ArityException("clojure.core/empty?");
        }
        // Check if second arg is a list.
        TokensList secondArg = (TokensList) tokens.get(1);
        if (((Token) secondArg.get(0)).toString().equals("list")) {

            if (secondArg.size() > 1) {
                return new Token("false");
            }
            return new Token("true");
        }
        throw new SyntaxException(
                "Invalid argument passed to clojure.core/empty?:" + secondArg
        );
    }
}
