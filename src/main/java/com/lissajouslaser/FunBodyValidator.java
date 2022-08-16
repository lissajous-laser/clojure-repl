package com.lissajouslaser;

import java.util.Map;

/**
 * Class for checking if the body of a function is valid.
 * its methods perform a similar function to
 * ComplexEvalution.eval(), by traversing through nested
 * expression, but differs by not trying to evaluate
 * a result.
 */
public class FunBodyValidator {
    private Map<Token, TokensListOrToken> definitions;
    private Token funName;
    private TokensList params;
    private Token body;

    /**
     * Constructor.
     */
    public FunBodyValidator(
            UserFunction fun,
            Map<Token, TokensListOrToken> definitions
    ) {
        this.definitions = definitions;
        funName = fun.getName();
        params = fun.getParams();
        body = fun.getBody();
    }

    public void validate()
            throws SyntaxException, ClassCastException {
        isValidExpr(body);
    }

    /**
     * Checks an expression is valid - it will only
     * check that expressions are in a proper list
     * structure and that the functions used are
     * already defined functions, and that any
     * arguments which as symbols are already
     * defined. It does not check whether the
     * number of arguments in an expression
     * is the correct arity for to pass to the
     * function. Throws an excpetion if the check
     * fails.
     */
    void isValidExpr(Token token)
            throws SyntaxException, ClassCastException {

        String expr = token.toString();

        if (CheckType.isList(expr)) {
            TokensList tokens = Tokeniser.run(expr);
            Token function = (Token) tokens.get(0);

            if (function.equals(funName) // for recursive calls
                    || params.contains(function) // higher order functions
                    || definitions.keySet().contains(function)) {

                // Check if any sub-expressions are valid.
                for (int i = 1; i < tokens.size(); i++) {
                    isValidExpr((Token) tokens.get(i));
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
            if (definitions.get(token) != null
                    || params.contains(token)) {
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
