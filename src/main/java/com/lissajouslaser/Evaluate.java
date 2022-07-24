package com.lissajouslaser;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Evalutes inputted Clojure code.
 */
public class Evaluate {

    /*
     * Possible Clojure forms:
     * - Single entity without parentheses, eg
     * a symbol, or a value.
     * - An expression. Either a definition, an
     * anonymous function or a function
     * application.
     */

    /**
     * Check that the number of opening and closing parens
     * are equal, and that there are never more closing
     * parens entered than opening parens entered at any
     * point in the expression.
     */
    public boolean checkBalancedParens(String expr) {
        char[] chars = expr.toCharArray();

        CharArrayReader charArrayReader = new CharArrayReader(chars);
        int character;
        int openParens = 0;
        int closeParens = 0;

        try {
            while ((character = charArrayReader.read()) != -1) {
                switch (character) {
                    case '(':
                        openParens++;
                        break;
                    case ')':
                        closeParens++;
                        if (closeParens > openParens) {
                            return false;
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("I/O error has occurred");
        }
        return openParens == closeParens;
    }

    /**
     * If input is a list it will take apart
     * functions and arguments and put into an array.
     * If input is not a list it will check there is
     * only one value.
     *  if (sanitised.matches("[^ ]+")) {
            String[] arr = new String[1];
            arr[0] = sanitised;
            return arr;
        } else {
            return null;
        }
     */

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
    public static boolean isList(String expr) {
        String sanitised = expr.trim();
        return sanitised.matches("\\(.*\\)");
    }

    /**
     * Check if you have a value or a symbol.
     */
    public static boolean isValueOrSymbol(String expr) {
        String sanitised = expr.trim();
        return sanitised.matches("\\w+");
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
        if (tokens.size() > 1) {

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
                case "def":
                    CoreFunctionsDefinition.def(args);
                    break;
                case "fn":
                    CoreFunctionsDefinition.fn(args);
                    break;
                default:
                    userDefinedFn(args);
                    break;
            }
        }
        return " ";
    }

    /**
     * Performs evaluation of expression, including evaluation
     * of nesteed expressions.
     */
    public String eval(String expr) throws SyntaxException {
        if (isList(expr)) {
            ArrayList<String> tokens = tokeniseList(expr);

            // Create new expression where aguments have been
            // evaluated.
            ArrayList<String> tokensWithEvaluatedArgs = new ArrayList<>();

            tokensWithEvaluatedArgs.add(tokens.get(0));

            for (int i = 1; i < tokens.size(); i++) {
                tokensWithEvaluatedArgs.add(eval(tokens.get(i)));
            }
            return dispatcher(tokensWithEvaluatedArgs);
        }
        if (isValueOrSymbol(expr)) {
            return expr;
        } else {
            throw new SyntaxException();
        }

    }

    void userDefinedFn(String[] tokens) {

    }
}
