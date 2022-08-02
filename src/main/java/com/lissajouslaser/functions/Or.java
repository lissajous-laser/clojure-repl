package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.CheckType;
import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * Or function.
 */
public class Or implements Function {

    public boolean isDefinitionCreator() {
        return false;
    }

    public boolean isEvaluationNormal() {
        return true;
    }

    public Token getName() {
        return new Token("or");
    }

    /**
     * Returns the first truthy value, if there
     * are no truthy values returns the last falsey value.
     * eg. (or false nil 3) returns nil,
     * (or 3 true) returns 3.
     */
    public Token applyFn(TokensList tokens)
            throws ArityException, ClassCastException {
        if (tokens.size() < 2) {
            throw new ArityException("clojure.core/or");
        }
        if (tokens.size() == 2) {
            return (Token) tokens.get(1);
        }
        Token currentToken = null;

        for (int i = 1; i < tokens.size(); i++) {
            currentToken = (Token) tokens.get(i);

            if (!CheckType.isLogicalFalse(currentToken.toString())) {
                return currentToken;
            }
        }
        return currentToken;
    }
}
