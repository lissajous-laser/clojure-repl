package com.lissajouslaser;

/**
 * Utility class which defines basic list functions
 * in the langauge.
 */
public final class CoreFunctionsList {

    private CoreFunctionsList() {}

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
}
