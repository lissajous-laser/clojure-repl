package com.lissajouslaser;

/**
 * Class for passing an expression to the correct
 * language-defined function.
 */
public final class Dispatch {

    private Dispatch() {}

    /**
     * Sends TokensList to the appropriate language-defined
     * function.
     */
    public static TokensListOrToken pass(TokensList tokens)
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
                throw new SyntaxException("Unable to resolve: "
                        + operator + " in this context.");
        }
    }

}
