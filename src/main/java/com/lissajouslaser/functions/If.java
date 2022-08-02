package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.CheckType;
import com.lissajouslaser.ComplexEvaluation;
import com.lissajouslaser.Function;
import com.lissajouslaser.SyntaxException;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;
import com.lissajouslaser.TokensListOrToken;

/**
 * If function.
 */
public class If extends ComplexEvaluation implements Function {

    public If() {
        super();
    }

    public boolean isDefinitionCreator() {
        return false;
    }

    public boolean isEvaluationNormal() {
        return false;
    }

    public Token getName() {
        return new Token("if");
    }

    /**
     * Conditional if expression.
     * In Clojure, the test expression can return any value,
     * rather than just booleans. False and nil are falsey
     * values, and all others are truthy values. If the test
     * for an if expression without an else clause is false,
     * nil is returned.
     */
    public TokensListOrToken applyFn(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {
        final int sizeTwoArgs = 3;
        final int sizeThreeArgs = 4;
        final int testIdx = 1;
        final int trueCaseIdx = 2;
        final int falseCaseIdx = 3;

        if (tokens.size() < sizeTwoArgs
                || tokens.size() > sizeThreeArgs) {
            throw new ArityException("clojure.core/if");
        }
        Token testExpr = (Token) eval((Token) tokens.get(testIdx));
        if (CheckType.isLogicalFalse(testExpr.toString())) {
            if (tokens.size() == sizeThreeArgs) {
                return eval((Token) tokens.get(falseCaseIdx));
            } else {
                return new Token("nil");
            }
        } else {
            return eval((Token) tokens.get(trueCaseIdx));
        }
    }
}
