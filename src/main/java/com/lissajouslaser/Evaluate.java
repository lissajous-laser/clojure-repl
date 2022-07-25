package com.lissajouslaser;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Evalutes inputted Clojure code.
 */
public class Evaluate {
    private Map<String, String> definitions;

    /*
     * Possible Clojure forms:
     * - Single entity without parentheses, eg
     * a symbol, or a value.
     * - An expression. Either a definition, an
     * anonymous function or a function
     * application.
     */

    /*
     * Limitations:
     * - No reader macro for non-evaluating lists, eg '()
     *   needs to be writtern as (list).
     * - Consing a value onto nil has not been implmented.
     */

    public Evaluate() {
        definitions = new HashMap<>();
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }

    /**
     * Adds a new symbol-value mapping, overwrites any
     * existing pairing.
     */
    public void addDefinedValues(String key, String value) {
        definitions.put(key, value);
    }

    /**
     * If input is a list it will take apart
     * function and arguments and put into an array.
     * Throws SyntaxException if there is a syntax
     * error.
     */
    public static ArrayList<String> tokeniseList(String expr) throws SyntaxException {
        ArrayList<String> tokens = new ArrayList<>();

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
        StringBuilder token = new StringBuilder();

        try {
            while ((i = charArrReader.read()) != -1) {

                // Incorrect syntax.
                if (closeParens > openParens) {
                    throw new SyntaxException();
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
                            token.append((char) i);
                        }
                        break;
                    case ')':
                        closeParens++;
                        if (nestedExpr) {
                            token.append((char) i);
                        // Does not add empty strings to tokens.
                        } else if (token.length() == 0) {
                            continue;
                        } else {
                            tokens.add(token.substring(0));
                            token.delete(0, token.length());
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
                            token.append((char) i);
                        // Does not add empty strings to tokens.
                        // For the case if input text has souble
                        // spaces.
                        } else if (token.length() == 0) {
                            continue;
                        } else {
                            tokens.add(token.substring(0));
                            token.delete(0, token.length());
                        }
                        break;
                    default:
                        token.append((char) i);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error - Exception caught while reading from "
                    + "CharArrayReader");
        }

        // Check parens are balanced.
        if (openParens == closeParens) {
            return tokens;
        } else {
            throw new SyntaxException();
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
        return expr.trim().matches("[A-Za-z_-][\\w-?]*");
    }

    /**
     * Check if you have a boolean. Nil is logical false in
     * Clojure.
     */
    static boolean isBool(String expr) {
        return expr.trim().matches("(true)|(false)|(nil)");
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
    public String dispatcher(ArrayList<String> tokens) throws SyntaxException {

        // Make array of tokens with only the args.
        String[] args = new String[tokens.size() - 1];
        for (int i = 1; i < tokens.size(); i++) {
            args[i - 1] = tokens.get(i);
        }
        switch (tokens.get(0)) {
            case "+":
                return CoreFunctionsArithmetic.add(args);
            case "-":
                return CoreFunctionsArithmetic.sub(args);
            case "/":
                return CoreFunctionsArithmetic.div(args);
            case "*":
                return CoreFunctionsArithmetic.mul(args);
            case "list":
                return CoreFunctionsList.list(args);
            case "cons":
                return CoreFunctionsList.cons(args);
            case "first":
                return CoreFunctionsList.first(args);
            case "rest":
                return CoreFunctionsList.rest(args);
            case "<":
                return CoreFunctionsComparator.lt(args);
            case ">":
                return CoreFunctionsComparator.gt(args);
            case "=":
                return CoreFunctionsComparator.eq(args);
            case "and":
                return CoreFunctionsBoolean.and(args);
            case "or":
                return CoreFunctionsBoolean.or(args);
            case "not":
                return CoreFunctionsBoolean.not(args);
            default:
                userDefinedFn(args);
                // TODO - handler for user defined functions
                return " ";
        }
    }

    /**
     * Performs evaluation of expression, including evaluation
     * of nesteed expressions.
     */
    public String eval(String expr) throws SyntaxException {

        if (isList(expr)) {
            ArrayList<String> tokens = tokeniseList(expr);

            // def, fn, and if are 'special forms' that cannot be
            // evaluated like normal expressions.
            if ("def".equals(tokens.get(0))) {

                return def(tokens);
            } else if ("fn".equals(tokens.get(0))) {
                // TODO - probably going to be hard.
                return "TODO";
            } else if ("if".equals(tokens.get(0))) {
                return ifClj(tokens);
            } else {
                // Evaluating normal expressions.
                // Create a new expression where aguments of the old
                // expression have been evaluated.
                ArrayList<String> tokensWithEvaluatedArgs = new ArrayList<>();
                tokensWithEvaluatedArgs.add(tokens.get(0));

                for (int i = 1; i < tokens.size(); i++) {
                    tokensWithEvaluatedArgs.add(eval(tokens.get(i)));
                }
                return dispatcher(tokensWithEvaluatedArgs);
            }
        }
        if (isNumber(expr) || isBool(expr)) {
            return expr;
        } else if (isValidSymbol(expr)) {
            String value = definitions.get(expr);
            if (value == null) {
                return "Error - Unable to resolve symbol";
            } else {
                return value;
            }
        } else {
            throw new SyntaxException();
        }
    }

    /**
     * Allows you to define values.
     */
    public String def(ArrayList<String> tokens) {
        final int numOfArgs = 2;

        if (tokens.size() != numOfArgs + 1) {
            return "Error - wrong number of args passed to clojure.core/def";
        }
        if (Evaluate.isValidSymbol(tokens.get(1))) {
            definitions.put(tokens.get(1), tokens.get(2));
            return "user/" + tokens.get(2);
        }
        return "Error - Illegal argument passed to clojure.core/def";
    }

    /**
     * Conditional if expression. Named ifClj because if is a
     * reserved keyword. In Clojure, false and nil are falsey
     * values, and all others are truthy values. If the test
     * for an if expression with an else clause is false, nil
     * is returned.
     */
    public String ifClj(ArrayList<String> tokens) throws SyntaxException {
        final int listItemsWhenTwoArgs = 3;
        final int listItemsWhenThreeArgs = 4;

        if (tokens.size() < listItemsWhenTwoArgs
                || tokens.size() > listItemsWhenThreeArgs + 1) {
            return "Error - wrong nmber of args passed to clojure.core/if";
        }
        String test = eval(tokens.get(1));
        if (CoreFunctionsBoolean.isNilOrFalse(test)) {
            if (tokens.size() == listItemsWhenThreeArgs) {
                return eval(tokens.get(listItemsWhenThreeArgs - 1));
            } else {
                return "nil";
            }
        } else {
            return eval(tokens.get(2));
        }
    }

    void userDefinedFn(String[] tokens) {
    }

}
