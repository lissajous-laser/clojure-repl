package com.lissajouslaser;

import java.util.ArrayList;

/**
 * Utility class which defines basic list functions
 * in the langauge. Clojure lists are singly linked
 * lists.
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

    /**
     * Adds an element to the front of the list.
     */
    public static String cons(String[] args) throws SyntaxException {
        // Arguments to cons should be the thing to be added to
        // the list and the list.
        if (args.length != 2) {
            return "Error - wrong number of args passed to clojure.core/cons";
        }
        // Check second arg is a list.
        if (Evaluate.isList(args[1])) {
            ArrayList<String> tokensOfList = Evaluate.tokeniseList(args[1]);

            if (!tokensOfList.get(0).equals("list")) {
                throw new SyntaxException("Illegal list passed to clojure.core/cons");
            }
            // Create output string with added element.
            StringBuilder returnString = new StringBuilder();
            returnString.append("(list ");
            returnString.append(args[0] + " ");

            for (int i = 1; i < tokensOfList.size(); i++) {
                returnString.append(tokensOfList.get(i) + " ");
            }
            // Delete last space character.
            returnString.deleteCharAt(returnString.length() - 1);
            returnString.append(")");
            return returnString.substring(0);
        }
        throw new SyntaxException("Illegal list passed to clojure.core/cons");
    }

    /**
     * Returns the first item of a list. Passing an empty list
     * to first returns null.
     */
    public static String first(String[] args) throws SyntaxException {
        // The only arg passed to first should be a single list.
        // First check there is one arg.
        if (args.length != 1) {
            return "Error - wrong number of args passed to clojure.core/first";
        }
        // Check if arg is a list.
        if (Evaluate.isList(args[0])) {
            ArrayList<String> tokensOfList = Evaluate.tokeniseList(args[0]);

            // For empty list ()
            if (tokensOfList.size() == 0) {
                return "nil";
            }
            if (!tokensOfList.get(0).equals("list")) {
                throw new SyntaxException("Illegal list passed to "
                        + "clojure.core/first");
            // For empty list (list)
            } else if (tokensOfList.size() == 1) {
                return "nil";
            } else {
                return tokensOfList.get(1);
            }
        }
        throw new SyntaxException("Illegal list passed to clojure.core/first");
    }

    /**
     * Returns a list without its first item.
     */
    public static String rest(String[] args) throws SyntaxException {
        // The only arg passed to first should be a single list.
        // First check there is one arg.
        if (args.length != 1) {
            return "Error - wrong number of args passed to clojure.core/rest";
        }
        // Check arg is a list.
        if (Evaluate.isList(args[0])) {
            ArrayList<String> tokensOfList = Evaluate.tokeniseList(args[0]);

            if (!tokensOfList.get(0).equals("list")) {
                throw new SyntaxException("Illegal list passed to "
                        + "clojure.core/rest");
            // For empty list (list) or list with one element.
            } else if (tokensOfList.size() == 1 || tokensOfList.size() == 2) {
                return "(list)";
            } else {
                // Recreate string without first element.
                StringBuilder returnString = new StringBuilder();

                returnString.append("(list ");

                for (int i = 2; i < tokensOfList.size(); i++) {
                    returnString.append(tokensOfList.get(i) + " ");
                }
                // Delete last space character.
                returnString.deleteCharAt(returnString.length() - 1);
                returnString.append(")");
                return returnString.substring(0);
            }
        }
        throw new SyntaxException("Illegal list passed to clojure.core/rest");
    }
}
