package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.CheckType;
import com.lissajouslaser.ComplexEvaluation;
import com.lissajouslaser.FunBodyValidator;
import com.lissajouslaser.Function;
import com.lissajouslaser.SyntaxException;
import com.lissajouslaser.Token;
import com.lissajouslaser.Tokeniser;
import com.lissajouslaser.TokensList;
import com.lissajouslaser.UserFunction;

/**
 * Fn function, Clojure anonymous function.
 */
public class Fn extends ComplexEvaluation implements Function {

    public boolean isDefinitionCreator() {
        return false;
    }

    public boolean isEvaluationNormal() {
        return false;
    }

    public Token getName() {
        return new Token("fn");
    }

    /**
     * Allows creation of anonymous functions. It returns a Function,
     * so the Function will reside in a ListOfTokens.
     */
    public Function applyFn(TokensList tokens)
        throws SyntaxException, ArityException, ClassCastException {
        final int validSize = 3;
        Token params = (Token) tokens.get(1);
        Token body = (Token) tokens.get(2);

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/fn");
        }
        // Name an anonymous function with a random serial from 0 to 999.
        final int multiplier = 1000;
        Token funName = new Token(
            "fn-" + Integer.toString(
                (int) (Math.random() * multiplier)
            )
        );
        // Check params is an un-nested list.
        if (!CheckType.isParamList(params.toString())) {
            throw new SyntaxException("Illegal parameter list passed to "
                    + "clojure.core/fn");
        }
        UserFunction fun = new UserFunction(
                funName,
                Tokeniser.run(params.toString()),
                body
        );
        FunBodyValidator validator = new
                FunBodyValidator(fun, getDefinitions());
        // throws exception if not valid
        validator.validate();

        return fun;
    }
}
