package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.CheckType;
import com.lissajouslaser.ComplexEvaluation;
import com.lissajouslaser.Define;
import com.lissajouslaser.Function;
import com.lissajouslaser.SyntaxException;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;
import com.lissajouslaser.TokensListOrToken;

/**
 * Define values function.
 */
public class Def extends ComplexEvaluation
    implements Function, Define {

    public boolean isEvalutionNormal() {
        return false;
    }

    public Token getName() {
        return new Token("def");
    }

    /**
     * Allows you to define values.
     */
    public Token applyFn(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {
        final int validSize = 3;

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/def");
        }
        Token valueName = (Token) tokens.get(1);
        TokensListOrToken evaluatedExpr = eval((Token) tokens.get(2));

        if (CheckType.isValidSymbol(valueName.toString())) {
            getDefinitions().put(valueName, evaluatedExpr);
            // Returns value name.
            return valueName;
        }
        throw new SyntaxException("Illegal name passed to clojure.core/def");
    }
}
