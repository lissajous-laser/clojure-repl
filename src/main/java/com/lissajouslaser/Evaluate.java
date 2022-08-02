package com.lissajouslaser;

import java.util.Map;

/*
 * Achievements:
 * - Supports higher order functions.
 * Limitations:
 * - No reader macros, eg '() needs to be writtern as
 *   (list).
 * - The pre-definded funtions don't have nullary
 *   opertion, eg. weird things like (+), (or) have
 *   not been implemented.
 * - Lists are the only supported data structure.
 *   Function parameters may be inputted as a Clojure
 *   vector, but will be converted to Clojure lists
 *   internally.
 * - Supported types are integers, lists, booleans
 *   and nil.
 * - Consing a value onto nil has not been implmented.
 * - Does not support anonymous functions.
 * - Supports anonymous functions, but currently does
 *   not support closures or function currying.
 */

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
