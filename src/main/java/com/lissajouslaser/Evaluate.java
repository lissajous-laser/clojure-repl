package com.lissajouslaser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Limitations:
 * - No reader macro for non-evaluating lists, eg '()
 *   needs to be writtern as (list).
 * - Lists are the only supported data structe. this
 *   also means function parameters are in a list
 *   instead of a vector, which is more similar to
 *   Common Lisp.
 * - Supported types are integers, lists, booleans
 *   and nil.
 * - Consing a value onto nil has not been implmented
 * - Functions and defined values are not in the same
 *   namespace - Lisp-2 rather than a Lisp-1
 * - No support for higher order functions.
 * - Does not support anonymous functions.
 * - No support for partial function application
 * - No support for multiple expressions
 */

/**
 * Evaluates inputted code and contains the namespace
 * of user definitions.
 */
public class Evaluate {
    // Stores user defined values, lists.
    private Map<Token, TokensListOrToken> definedValues;
    private Map<Token, Function> definedFunctions;
    // To check that all user defined functions call
    // functions that already exist.
    private Set<String> coreFunctions;

    /**
     * Class constructor.
     */
    public Evaluate() {
        definedValues = new HashMap<>();
        definedFunctions = new HashMap<>();
        String[] coreFunctionsArr = {"if", "def", "defn"};
        coreFunctions = new HashSet<>(Arrays.asList(coreFunctionsArr));
    }

    /**
     * For passing a namespace with pre-defined functions.
     */
    public Evaluate(Map<Token, Function> definedFunctions) {
        this();
        this.definedFunctions = definedFunctions;

    }

    Map<Token, TokensListOrToken> getDefinedValues() {
        return definedValues;
    }

    Map<Token, Function> getdefinedFunctions() {
        return definedFunctions;
    }

    void setdefinedFunctions(Map<Token, Function> functions) {
        definedFunctions = functions;
    }

    /**
     * Used by subclasses.
     */
    void copyDefinedValues(Map<Token, TokensListOrToken> globalDefinedValues) {
        getDefinedValues().putAll(globalDefinedValues);
    }

    /**
     * Performs evaluation of expression, including evaluation
     * of nested expressions.
     */
    public TokensListOrToken eval(Token token)
            throws SyntaxException, ArithmeticException,
            NumberFormatException, ArityException, ClassCastException {

        String expr = token.toString();

        if (CheckType.isList(expr)) {
            TokensList tokens = Tokeniser.run(expr);

            if (tokens.isEmpty()) {
                throw new SyntaxException("Unsupported input: " + expr);
            }
            String operator = ((Token) tokens.get(0)).toString();
            switch (operator) {
                case "def":
                    // def is a special form: its name should not
                    // be evaluated.
                    return def(tokens);
                case "defn":
                    // defn is a special form: only its body should be
                    // evaluated, and only when arguments are passed to it.
                    return defn(tokens);
                case "if":
                    // if is a special form: only the chosen branch should
                    // be evaluated so that you don't have unwanted
                    // side-effects from evaluating the other branch.
                    return ifClj(tokens);
                default:
                    // Evaluating normal expressions.
                    // Create a new expression where aguments of the old
                    // expression have been evaluated.
                    TokensList tokensWithEvaluatedArgs = new TokensList();
                    tokensWithEvaluatedArgs.add(tokens.get(0));

                    for (int i = 1; i < tokens.size(); i++) {
                        Token arg = (Token) tokens.get(i);
                        tokensWithEvaluatedArgs.add(eval(arg));
                    }
                    return userDefined(tokensWithEvaluatedArgs);
            }
        } else if (CheckType.isNumber(expr) || CheckType.isBool(expr)) {
            return token;
        } else if (CheckType.isValidSymbol(expr)) {
            TokensListOrToken valueOrList = getDefinedValues().get(token);
            if (valueOrList == null) {
                throw new SyntaxException("Unable to resolve symbol: " + expr
                        + " in this context.");
            }
            return valueOrList;
        } else {
            throw new SyntaxException("Invalid token: " + expr);
        }
    }

    /**
     * Like eval(Token token) but accepts a String instead
     * which is converted into a token.
     */
    public TokensListOrToken eval(String expr)
        throws SyntaxException, ArithmeticException,
        NumberFormatException, ArityException, ClassCastException {
        return eval(new Token(expr.trim()));
    }

    /**
     * Allows you to define values.
     */
    public Token def(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {
        final int numOfArgs = 2;

        if (tokens.size() != numOfArgs + 1) {
            throw new ArityException("clojure.core/def");
        }
        Token valueName = (Token) tokens.get(1);
        TokensListOrToken evaluatedExpr = eval((Token) tokens.get(2));

        if (CheckType.isValidSymbol(valueName.toString())) {
            definedValues.put(valueName, evaluatedExpr);
            return (Token) tokens.get(1);
        }
        throw new SyntaxException("Illegal name passed to clojure.core/def");
    }

    /**
     * Conditional if expression. Named ifClj because if is a
     * reserved keyword. In Clojure, false and nil are falsey
     * values, and all others are truthy values. If the test
     * for an if expression with an else clause is false, nil
     * is returned.
     */
    public Token ifClj(TokensList tokens)
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
                return (Token) eval((Token) tokens.get(falseCaseIdx));
            } else {
                return new Token("nil");
            }
        } else {
            return (Token) eval((Token) tokens.get(trueCaseIdx));
        }
    }

    /**
     * Allows creation of functions.
     */
    public Token defn(TokensList tokens)
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

        UserDefinedFunction fn = new UserDefinedFunction(
                fnName,
                Tokeniser.run(params.toString()),
                body

        );
        definedFunctions.put((Token) tokens.get(1), fn);

        return new Token(fn.toString());
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
    void isValidExpr(Token token, Token fnName, TokensList tokenisedParams)
            throws SyntaxException, ClassCastException {

        String expr = token.toString();

        if (CheckType.isList(expr)) {
            TokensList tokens = Tokeniser.run(expr);
            Token function = (Token) tokens.get(0);

            if (function.equals(fnName) // for recursive calls
                    || coreFunctions.contains(function.toString())
                    || definedFunctions.keySet().contains(function)) {

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
            if (definedValues.get(token) != null
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

    /**
     * Tries to match a user defined function to the expression
     * to evaluate, then applies function.
     */
    TokensListOrToken userDefined(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {
        Token fnName = (Token) tokens.get(0);
        Function function = definedFunctions.get(fnName);
        if (function == null) {
            throw new SyntaxException("Unable to resolve " + fnName
                    + " in this context.");
        }
        if (function instanceof Evaluate) {
            ((Evaluate) function).setdefinedFunctions(definedFunctions);
            ((Evaluate) function).copyDefinedValues(definedValues);
        }
        return function.applyFn(tokens);
    }
}
