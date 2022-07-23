package com.lissajouslaser;

import java.util.Arrays;

/**
 * Utility class which defines core functions in the langauge.
 */
public final class CoreFunctions {

    private CoreFunctions() {}

    /**
     * Add arguments.
     */
    public static String add(String[] args) {
        try {
            int sum = Arrays.stream(args)
                    .mapToInt(x -> Integer.valueOf(x))
                    .reduce(0, (x, y) -> x + y);
            return String.valueOf(sum);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        }
    }

    /**
     * Subtract from first arg the rest of the args.
     */
    public static String sub(String[] args) {
        try {
            int initial = Integer.valueOf(args[0]);

            // The args that will be subtracted from the
            // initial value.
            String[] subtractors = Arrays.copyOfRange(args, 1, args.length);

            int subtraction = Arrays.stream(subtractors)
                    .mapToInt(x -> Integer.valueOf(x))
                    .reduce(initial, (x, y) -> x - y);
            return String.valueOf(subtraction);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        }
    }

    /**
     * Multiply args.
     */
    public static String mul(String[] args) {
        try {
            int product = Arrays.stream(args)
                    .mapToInt(x -> Integer.valueOf(x))
                    .reduce(1, (x, y) -> x * y);
            return String.valueOf(product);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        }
    }

    /**
     * Divide from first arg the rest of the args.
     */
    public static String div(String[] args) {
        try {
            int initial = Integer.valueOf(args[0]);

            // The args that will be subtracted from the
            // initial value.
            String[] divisors = Arrays.copyOfRange(args, 1, args.length);

            int division = Arrays.stream(divisors)
                    .mapToInt(x -> Integer.valueOf(x))
                    .reduce(initial, (x, y) -> x / y);
            return String.valueOf(division);
        } catch (NumberFormatException e) {
            return "Error - String cannot be cast to number";
        } catch (ArithmeticException e) {
            return "Error - Cannot divide by zero";
        }
    }

    /**
     *  For case list we return the expression as it was when
     *  passed to tokenise, as we do not want to evaluate it.
     *  I have decided to not use the clojure reader macro for
     *  lists (the single quote) for simplicity.
     */
    public static String list(String[] args) {
        StringBuilder returnString = new StringBuilder();

        returnString.append("(list ");

        for (String str: args) {
            returnString.append(str);
            returnString.append(" ");
        }
        // Delete last space character.
        returnString.deleteCharAt(returnString.length() - 1);
        returnString.append(")");
        return returnString.substring(0);
    }

    static void cons(String[] args) {

    }

    /**
     * Returns the first item of a list.
     */
    public static String first(String[] args) {
        // The only arg passed to first should be a single list.
        // First check there is one arg.
        if (args.length != 1) {
            return "Error - wrong number of args passed to clojure.core/first";
        }
        // Check arg is a list.
        if (Evaluate.isList(args[0])) {
            String[] tokensOfList = Evaluate.tokenise(args[0]);

            // For empty list ()
            if (tokensOfList.length == 0) {
                return "nil";
            }
            if (!tokensOfList[0].equals("list")) {
                return "Error - illegal argument passed to clojure.core/first";
            // For empty list (list)
            } else if (tokensOfList.length == 1) {
                return "nil";
            } else {
                return tokensOfList[1];
            }
        }
        return "Error - Illegal argument passed to clojure.core/first";
    }

    /**
     * Returns a list without its first item.
     */
    public static String rest(String[] args) {
        // The only arg passed to first should be a single list.
        // First check there is one arg.
        if (args.length != 1) {
            return "Error - wrong number of args passed to clojure.core/rest";
        }
        // Check arg is a list.
        if (Evaluate.isList(args[0])) {
            String[] tokensOfList = Evaluate.tokenise(args[0]);

            // For empty list ()
            if (tokensOfList.length == 0) {
                return "(list)";
            }
            if (!tokensOfList[0].equals("list")) {
                return "Error - illegal argument passed to clojure.core/rest";
            // For empty list (list) or list with one element.
            } else if (tokensOfList.length == 1 || tokensOfList.length == 2) {
                return "(list)";
            } else {
                // Recreate string withtout first element.
                StringBuilder returnString = new StringBuilder();

                returnString.append("(list ");

                for (int i = 2; i < tokensOfList.length; i++) {
                    returnString.append(tokensOfList[i]);
                    returnString.append(" ");
                }
                // Delete last space character.
                returnString.deleteCharAt(returnString.length() - 1);
                returnString.append(")");
                return returnString.substring(0);
            }
        }
        return "Error - Illegal argument passed to clojure.core/rest";
    }

    static void lt(String[] args) {

    }

    static void gt(String[] args) {

    }

    static void eq(String[] args) {

    }

    static void def(String[] args) {

    }

    static void fn(String[] args) {

    }

}
