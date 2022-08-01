package com.lissajouslaser.functions;

import com.lissajouslaser.ArityException;
import com.lissajouslaser.CheckType;
import com.lissajouslaser.ComplexEvaluation;
import com.lissajouslaser.Define;
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
        implements Function, Define {

    public boolean isEvalutionNormal() {
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
        Token fnName = (Token) tokens.get(1);
        Token params = (Token) tokens.get(2);
        TokensList tokenisedParams = Tokeniser.run(params.toString());
        Token body = (Token) tokens.get(bodyIdx);

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/defn");
        }
        if (!CheckType.isValidSymbol(fnName.toString())) {
            throw new SyntaxException("Illegal name passed to "
                    + "clojure.core/defn");
        }
        // Check params is an un-nested list.
        if (!CheckType.isParamList(params.toString())) {
            throw new SyntaxException("Illegal parameter list passed to "
                    + "clojure.core/defn");
        }
        isValidExpr(body, fnName, tokenisedParams); // throws exception if not valid

        UserFunction fn = new UserFunction(
                fnName,
                Tokeniser.run(params.toString()),
                body
        );
        getDefinitions().put((Token) tokens.get(1), fn);

        // Returns function name.
        return fnName;
    }

    /**
     * Checks function body is valid - it will only
     * check that expressions are in a proper list
     * structure and that the functions used are
     * already defined functions. It does no checking
     * of the arguments in any expression. Takes the
     * functions name as an argument so that that an
     * expression can call the function itself when
     * checking for for valid functions - this
     * allows for recursive calls.
     * Throws an excpetion if the check fails.
     */
    public void isValidExpr(Token token, Token fnName, TokensList tokenisedParams)
            throws SyntaxException, ClassCastException {

        String expr = token.toString();

        if (CheckType.isList(expr)) {
            TokensList tokens = Tokeniser.run(expr);
            Token function = (Token) tokens.get(0);

            if (function.equals(fnName) // for recursive calls
                    || getDefinitions().keySet().contains(function)) {

                // Check if any sub-expressions are valid.
                for (int i = 1; i < tokens.size(); i++) {
                    isValidExpr((Token) tokens.get(i), fnName, tokenisedParams);
                }
            } else {
                throw new SyntaxException("Unable to resolve " + expr
                        + " in this context.");
            }
        } else if (CheckType.isNumber(expr)
                || CheckType.isBool(expr)) {
            return;
        } else if (CheckType.isValidSymbol(expr)) {
            // Check symbol is defined or symbol is function parameter.
            if (getDefinitions().get(token) != null
                    || tokenisedParams.contains(token)) {
                return;
            }
            throw new SyntaxException("Unable to resolve " + expr
                    + " in this context.");
        } else {
            throw new SyntaxException("Illegal body passed to"
                    + "clojure.core/defn");
        }
    }
}
