package com.lissajouslaser;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Limitations:
* - No reader macro for non-evaluating lists, eg '()
*   needs to be writtern as (list).
* - Lists are the only supported data structe. this
*   also means function parameters are in a list
*   instead of a vector.
* - Consing a value onto nil has not been implmented.
* - Functions and defined values are not in the same
    namespace - Lisp-2 rather than a Lisp-1
* - Does not support anonymous functions.
* - No support for functions with multiple expressions.
*/

/**
 * Evalutes inputted Clojure code.
 */
public class Evaluate {
    private Map<String, String> definedValues;
    private Map<String, Function> userDefinedFunctions;
    private List<String> coreFunctions; // for validating user defined functions

    /**
     * Class constructor.
     */
    public Evaluate() {
        definedValues = new HashMap<>();
        userDefinedFunctions = new HashMap<>();
        String[] coreFunctionsArr = {"+", "-", "/", "*", "list", "cons",
                                     "first", "rest", "<", ">", "=", "and",
                                     "or", "not", "def", "if", "fn"};
        coreFunctions = new ArrayList<>(Arrays.asList(coreFunctionsArr));
    }

    public Map<String, String> getDefinedValues() {
        return definedValues;
    }

    public void setDefinedValues(Map<String, String> globalDefinedValues) {
        definedValues = globalDefinedValues;
    }

    public Map<String, Function> getUserDefinedFunctions() {
        return userDefinedFunctions;
    }

    public void setUserDefinedFunctions(Map<String, Function> functions) {
        userDefinedFunctions = functions;
    }

    public List<String> getCoreFunctions() {
        return coreFunctions;
    }

    /**
     * If input is a list it will take apart
     * function and arguments and put into an array.
     * Throws SyntaxException if there is a syntax
     * error.
     */
    static ArrayList<String> tokeniseList(String expr) throws SyntaxException {
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
    String dispatcher(ArrayList<String> tokens) throws SyntaxException {

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
                return userDefined(tokens);
        }
    }

    /**
     * Performs evaluation of expression, including evaluation
     * of nesteed expressions.
     */
    public String eval(String expr) throws SyntaxException {

        if (isList(expr)) {
            ArrayList<String> tokens = tokeniseList(expr);

            switch (tokens.get(0)) {
                case "def":
                    // def is a special form: symbol representing name
                    // should not be evaluated.
                    return def(tokens);
                case "defn":
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
                    ArrayList<String> tokensWithEvaluatedArgs = new ArrayList<>();
                    tokensWithEvaluatedArgs.add(tokens.get(0));

                    for (int i = 1; i < tokens.size(); i++) {
                        System.out.print("At Evaluate:268: "); ///
                        System.out.println(tokens.get(i)); ///
                        tokensWithEvaluatedArgs.add(eval(tokens.get(i)));
                    }
                    return dispatcher(tokensWithEvaluatedArgs);
            }
        }
        if (isNumber(expr) || isBool(expr)) {
            return expr;
        } else if (isValidSymbol(expr)) {
            String value = definedValues.get(expr);
            System.out.print("At evaluate:279: ");
            System.out.println("symbol resolves to: " + value);
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
        if (isValidSymbol(tokens.get(1))) {
            definedValues.put(tokens.get(1), tokens.get(2));
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
        final int listSizeTwoArgs = 3;
        final int listSizeThreeArgs = 4;
        final int testIdx = 1;
        final int trueCaseIdx = 2;
        final int falseCaseIdx = 3;


        if (tokens.size() < listSizeTwoArgs
                || tokens.size() > listSizeThreeArgs) {
            return "Error - wrong nmber of args passed to clojure.core/if";
        }
        String test = eval(tokens.get(testIdx));
        if (CoreFunctionsBoolean.isNilOrFalse(test)) {
            if (tokens.size() == listSizeThreeArgs) {
                return eval(tokens.get(falseCaseIdx));
            } else {
                return "nil";
            }
        } else {
            return eval(tokens.get(trueCaseIdx));
        }
    }

    /**
     * Allows creation of functions.
     */
    public String defn(ArrayList<String> tokens) throws SyntaxException {
        final int listSize = 4;
        final int bodyIdx = 3;
        String fnName = tokens.get(1);
        String params = tokens.get(2);
        String body = tokens.get(bodyIdx);

        if (tokens.size() != listSize) {
            return "Error - wrong nmber of args passed to clojure.core/defn";
        }
        if (!isValidSymbol(fnName) || !isList(params)) {
            return "Some failure";
        }
        String validExprTest = isValidExpr(body, fnName);
        if ("true".equals(validExprTest)) {
            Function fn = new Function(fnName, tokeniseList(params), body);
            userDefinedFunctions.put(fnName, fn);

            return "user/" + fnName;
        } else {
            return "Some failure";
        }
    }

    /**
     * Checks function body is valid - it will only
     * check that expressions are in a proper list
     * structure and that the functions used are
     * already defined funtions. It does no checking
     * of the operands in any expression. Takes the
     * functions name as an argument so that that an
     * expression can call the function itself when
     * checking for for valid functions - this
     * allows for recursive calls.
     */
    String isValidExpr(String expr, String fnName)
            throws SyntaxException {
        if (isList(expr)) {
            ArrayList<String> tokens = tokeniseList(expr);
            String function = tokens.get(0);

            if (function.matches(fnName)
                    || coreFunctions.contains(function)
                    || userDefinedFunctions.keySet().contains(function)) {

                ArrayList<String> isValidExprResults = new ArrayList<>();
                isValidExprResults.add("true");

                for (int i = 1; i < tokens.size(); i++) {
                    isValidExprResults.add(isValidExpr(tokens.get(i), fnName));
                }
                // Convert to array.
                // TODO - may need to change all Arrays to ArrayList
                String[] isValidExprResultsArr = new String[isValidExprResults.size()];
                for (int i = 0; i < isValidExprResults.size(); i++) {
                    isValidExprResultsArr[i] = isValidExprResults.get(i);
                }
                return CoreFunctionsBoolean
                        .and(isValidExprResultsArr);
            } else {
                return "false";
            }
        } else if (isNumber(expr) || isBool(expr) || isValidSymbol(expr)) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Tries to match a user defined function to the expression
     * to evaluate.
     */
    String userDefined(ArrayList<String> tokens)
            throws SyntaxException {
        String fnName = tokens.get(0);
        Function fn = userDefinedFunctions.get(fnName);
        if (fn == null) {
            return "Unable to resolve: " + fnName + " in this context";
        } else {
            ArrayList<String> args = new
                    ArrayList<>(tokens.subList(1, tokens.size()));
            return fn.applyFn(args, this);
        }
    }

}
