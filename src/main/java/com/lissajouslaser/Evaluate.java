package com.lissajouslaser;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.Arrays;

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
     *
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
    public static String[] tokenise(String expr) {

        String[] tokens = expr.trim().split("[ \\(\\)]");
        // Need to remove first element of array because
        // regex keeps putting an empty String there.
        String[] tokensRemoveFirst = Arrays.copyOfRange(tokens, 1, tokens.length);
        return tokensRemoveFirst;

    }

    /**
     * Checks if you have a list, as opposed to a value.
     */
    public static boolean isList(String expr) {
        String sanitised = expr.trim();
        return sanitised.matches("\\(.*\\)");
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
    public String dispatcher(String[] tokens) {
        if (tokens.length > 1) {
            // New array of tokens with out function.
            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

            switch (tokens[0]) {
                case "+":
                    return CoreFunctions.add(args);
                case "-":
                    return CoreFunctions.sub(args);
                case "/":
                    return CoreFunctions.div(args);
                case "*":
                    return CoreFunctions.mul(args);
                case "list":
                    return CoreFunctions.list(args);
                case "cons":
                    CoreFunctions.cons(args);
                    break;
                case "first":
                    CoreFunctions.first(args);
                    break;
                case "rest":
                    CoreFunctions.rest(args);
                    break;
                case "<":
                    CoreFunctions.lt(args);
                    break;
                case ">":
                    CoreFunctions.gt(args);
                    break;
                case "=":
                    CoreFunctions.eq(args);
                    break;
                case "def":
                    CoreFunctions.def(args);
                    break;
                case "fn":
                    CoreFunctions.fn(args);
                    break;
                default:
                    userDefinedFn(tokens);
                    break;
            }
        }
        return " ";
    }

    void userDefinedFn(String[] tokens) {

    }
}
