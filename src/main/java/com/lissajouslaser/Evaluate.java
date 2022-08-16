package com.lissajouslaser;

import java.util.Map;

/**
 * Evaluates inputted code and maintains the namespace of the
 * repl session through the defnitions instance variable.
 */
public class Evaluate extends ComplexEvaluation {

    /**
     * For passing a namespace with pre-defined functions.
     */
    public Evaluate(Map<Token, TokensListOrToken> definitions) {
        setDefinitions(definitions);
    }

    /**
     * Like ComplexFunction.eval(Token token) but accepts a
     * String instead which is converted into a token.
     */
    public TokensListOrToken eval(String expr)
        throws SyntaxException, ArithmeticException,
        NumberFormatException, ArityException, ClassCastException {
        return eval(new Token(expr.trim()));
    }
}
