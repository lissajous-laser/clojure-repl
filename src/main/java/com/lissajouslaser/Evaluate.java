package com.lissajouslaser;

import java.io.CharArrayReader;
import java.io.IOException;
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
 * - Consing a value onto nil has not been implmented
 * - User defined functions don't override functions
 *   supplied by the langauge.
 * - Functions and defined values are not in the same
 *   namespace - Lisp-2 rather than a Lisp-1
 * - Does not support anonymous functions.
 * - No support for higher order functions.
 * - No support for partial function application
 * - No support for multiple expressions
 */

/**
 * Evalutes inputted Clojure code.
 */
public class Evaluate {
    // Stores user defined values, lists.
    private Map<Token, TokensListOrToken> definedValues;
    private Map<String, Function> userDefinedFunctions;
    // To check that all user defined functions call
    // functions that already exist.
    private Set<String> coreFunctions;

    /**
     * Class constructor.
     */
    public Evaluate() {
        definedValues = new HashMap<>();
        userDefinedFunctions = new HashMap<>();
        String[] coreFunctionsArr = {"+", "-", "/", "*", "mod", "list",
                                     "cons", "first", "rest", "<", ">", "=",
                                     "and", "or", "not", "def", "if", "defn"};
        coreFunctions = new HashSet<>(Arrays.asList(coreFunctionsArr));
    }

    Map<Token, TokensListOrToken> getDefinedValues() {
        return definedValues;
    }

    Map<String, Function> getUserDefinedFunctions() {
        return userDefinedFunctions;
    }

    void setUserDefinedFunctions(Map<String, Function> functions) {
        userDefinedFunctions = functions;
    }

    /**
     * If input is a list it will take apart and
     * function and arguments and put into an array.
     */
    static TokensList tokeniseList(String expr) throws SyntaxException {
        TokensList tokens = new TokensList();

        char[] exprAsChars = expr.trim().toCharArray();
        int i;

        CharArrayReader charArrReader = new CharArrayReader(exprAsChars);
        // In expression we expect to see at least an opening
        // and closing parens.
        int openParens = 0;
        int closeParens = 0;
        // Checks if reader position is currently inside a
        // nested expression.
        boolean nestedExpr = false;
        StringBuilder tokenBuilder = new StringBuilder();

        try {
            while ((i = charArrReader.read()) != -1) {

                // Incorrect syntax.
                if (closeParens > openParens) {
                    throw new SyntaxException("Unmatched parentheses.");
                }

                switch (i) {
                    case '(':
                        openParens++;
                        // No action on the parens that wraps the
                        // top level expression.
                        if (openParens == 1) {
                            continue;
                            // Include parens in doubly or deeper nested
                            // expressions.
                        } else {
                            nestedExpr = true;
                            tokenBuilder.append((char) i);
                        }
                        break;
                    case ')':
                        closeParens++;
                        if (nestedExpr) {
                            tokenBuilder.append((char) i);
                            // Does not add empty strings to tokens.
                        } else if (tokenBuilder.length() == 0) {
                            continue;
                        } else {
                            tokens.add(new Token(tokenBuilder.substring(0)));
                            tokenBuilder.delete(0, tokenBuilder.length());
                        }
                        if (openParens - closeParens == 1) {
                            nestedExpr = false;
                        }
                        break;
                    case ' ':
                        // We want to have the whole nested expression
                        // as a single token. We do not care about
                        // separating out doubly nested expression or
                        // deeper because they will be dealt with on
                        // recursion.
                        if (nestedExpr) {
                            tokenBuilder.append((char) i);
                            // Does not add empty strings to tokens.
                            // For the case if input text has souble
                            // spaces.
                        } else if (tokenBuilder.length() == 0) {
                            continue;
                        } else {
                            tokens.add(new Token(tokenBuilder.substring(0)));
                            tokenBuilder.delete(0, tokenBuilder.length());
                        }
                        break;
                    default:
                        tokenBuilder.append((char) i);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        // Check parens are balanced.
        if (openParens == closeParens) {
            return tokens;
        } else {
            throw new SyntaxException("Unmatched parentheses.");
        }
    }

    /**
     * Checks if you have a list.
     */
    static boolean isList(String expr) {
        return expr.trim().matches("\\(.*\\)");
    }

    /**
     * Check if you have a number.
     */
    static boolean isNumber(String expr) {
        return expr.trim().matches("-?\\d+");
    }

    /**
     * Check if you have a symbol in the valid format.
     */
    static boolean isValidSymbol(String expr) {
        boolean matchesRegex = expr.trim().matches("[A-Za-z_-][\\w-?]*");
        return matchesRegex && !isBool(expr);
    }

    /**
     * Check if you have a boolean. Included nil here for
     * convenience.
     */
    static boolean isBool(String expr) {
        return expr.trim().matches("true|false|nil");
    }

    /**
     * Check that parameters list in a function is valid.
     * Must be a un-nested list.
     */
    static boolean isParamList(String expr) {
        return expr.trim().matches("\\([^\\(\\)]*\\)");
    }

    /**
     * Sends tokens to appropriate handler method
     * based on if tokens is an expression, and in
     * the event of an expression, what function
     * is beinga applied.
     * Returns results in String representation:
     * Clojure is dynamically typed, and I needed
     * a way to return values of different types.
     */
    TokensListOrToken dispatcher(TokensList tokens)
            throws SyntaxException, ArithmeticException,
            NumberFormatException, ArityException, ClassCastException {

        String operator = ((Token) tokens.get(0)).toString();
        switch (operator) {
            case "+":
                return CoreFunctionsArithmetic.add(tokens);
            case "-":
                return CoreFunctionsArithmetic.sub(tokens);
            case "/":
                return CoreFunctionsArithmetic.div(tokens);
            case "*":
                return CoreFunctionsArithmetic.mul(tokens);
            case "mod":
                return CoreFunctionsArithmetic.mod(tokens);
            case "list":
                // List is a data structure so it does not
                // require evaluation.
                return tokens;
            case "cons":
                return CoreFunctionsList.cons(tokens);
            case "first":
                return CoreFunctionsList.first(tokens);
            case "rest":
                return CoreFunctionsList.rest(tokens);
            case "<":
                return CoreFunctionsComparator.lt(tokens);
            case ">":
                return CoreFunctionsComparator.gt(tokens);
            case "=":
                return CoreFunctionsComparator.eq(tokens);
            case "and":
                return CoreFunctionsBoolean.and(tokens);
            case "or":
                return CoreFunctionsBoolean.or(tokens);
            case "not":
                return CoreFunctionsBoolean.not(tokens);
            default:
                return userDefined(tokens);
        }
    }

    /**
     * Performs evaluation of expression, including evaluation
     * of nesteed expressions.
     */
    public TokensListOrToken eval(Token token)
            throws SyntaxException, ArithmeticException,
            NumberFormatException, ArityException, ClassCastException {

        String expr = token.toString();

        if (isList(expr)) {
            TokensList tokens = tokeniseList(expr);

            if (tokens.isEmpty()) {
                throw new SyntaxException("() is not supported.");
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
                    return dispatcher(tokensWithEvaluatedArgs);
            }
        } else if (isNumber(expr) || isBool(expr)) {
            return token;
        } else if (isValidSymbol(expr)) {
            TokensListOrToken valueOrList = getDefinedValues().get(token);
            if (valueOrList == null) {
                throw new SyntaxException("Unable to resolve symbol: " + expr
                        + " in this context.");
            }
            return valueOrList;
        } else {
            throw new SyntaxException("Unsupported input: " + expr);
        }
    }

    /**
     * Aceepts a String arg which is converted into a token.
     */
    public TokensListOrToken eval(String expr)
        throws SyntaxException, ArithmeticException,
        NumberFormatException, ArityException, ClassCastException {
        return eval(new Token(expr));
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

        if (isValidSymbol(valueName.toString())) {
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
        if (CoreFunctionsBoolean.isNilOrFalse(testExpr.toString())) {
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
        String fnName = ((Token) tokens.get(1)).toString();
        String params = ((Token) tokens.get(2)).toString();
        String body = ((Token) tokens.get(bodyIdx)).toString();

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/defn");
        }
        if (!isValidSymbol(fnName)) {
            throw new SyntaxException("Illegal name passed to "
                    + "clojure.core/defn");
        }
        // Check params is an un-nested list.
        if (!isParamList(params)) {
            throw new SyntaxException("Illegal parameter list passed to "
                    + "clojure.core/defn");
        }
        isValidExpr(body, fnName); // throws exception if not valid

        Function fn = new Function(fnName, tokeniseList(params), body);
        userDefinedFunctions.put(fnName, fn);

        return (Token) tokens.get(1);
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
    void isValidExpr(String expr, String fnName)
            throws SyntaxException, ClassCastException {
        if (isList(expr)) {
            TokensList tokens = tokeniseList(expr);
            String function = ((Token) tokens.get(0)).toString();

            if (function.matches(fnName) // for recursive calls
                    || coreFunctions.contains(function)
                    || userDefinedFunctions.keySet().contains(function)) {

                // Check if any sub-expressions are valid.
                for (int i = 1; i < tokens.size(); i++) {
                    isValidExpr(((Token) tokens.get(i)).toString(), fnName);
                }
            } else {
                throw new SyntaxException("Unable to resolve " + expr
                        + " in this context.");
            }
        } else if (isNumber(expr) || isBool(expr) || isValidSymbol(expr)) {
            return;
        } else {
            throw new SyntaxException("Illegal body passed to clojure.core/defn");
        }
    }

    /**
     * Tries to match a user defined function to the expression
     * to evaluate, then applies function.
     */
    TokensListOrToken userDefined(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {
        String fnName = ((Token) tokens.get(0)).toString();
        Function function = userDefinedFunctions.get(fnName);
        if (function == null) {
            throw new SyntaxException("Unable to resolve: " + fnName
                    + " in this context.");
        } else {
            return function.applyFn(tokens, definedValues, userDefinedFunctions);
        }
    }
}
