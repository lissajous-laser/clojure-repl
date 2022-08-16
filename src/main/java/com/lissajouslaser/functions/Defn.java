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
 * Define functions function.
 */
public class Defn extends ComplexEvaluation
        implements Function {

    public boolean isDefinitionCreator() {
        return true;
    }

    public boolean isEvaluationNormal() {
        return false;
    }

    public Token getName() {
        return new Token("defn");
    }

    /**
     * Allows creation of named functions.
     */
    public Token applyFn(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {
        final int validSize = 4;
        final int bodyIdx = 3;
        Token funName = (Token) tokens.get(1);
        Token params = (Token) tokens.get(2);
        Token body = (Token) tokens.get(bodyIdx);

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/defn");
        }
        if (!CheckType.isValidSymbol(funName.toString())) {
            throw new SyntaxException("Illegal name passed to "
                    + "clojure.core/defn");
        }
        // Check params is an un-nested list.
        if (!CheckType.isParamList(params.toString())) {
            throw new SyntaxException("Illegal parameter list passed to "
                    + "clojure.core/defn");
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

        getDefinitions().put((Token) tokens.get(1), fun);

        // Returns function name.
        return funName;
    }
}
