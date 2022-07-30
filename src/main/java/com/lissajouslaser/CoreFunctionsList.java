package com.lissajouslaser;

/**
 * Utility class which defines basic list functions
 * in the langauge. Clojure lists are singly linked
 * lists.
 */
public final class CoreFunctionsList {

    private CoreFunctionsList() {
    }

    /**
     * Adds an element at the front of the list.
     */
    public static TokensList cons(TokensList tokens)
            throws SyntaxException, ArityException {

        final int validSize = 3;

        if (tokens.size() != validSize) {
            throw new ArityException("clojure.core/cons");
        }
        // Check third arg is a list.
        TokensListOrToken thirdArg = tokens.get(2);
        if (thirdArg instanceof TokensList
                && ((TokensList) thirdArg).get(0) instanceof Token
                && ((Token) ((TokensList) thirdArg).get(0)).toString().equals("list")) {

            TokensList list = new TokensList((TokensList) thirdArg);
            list.add(1, tokens.get(1));

            return list;
        }
        throw new SyntaxException("Illegal list passed to clojure.core/cons");
    }

    /**
     * Returns the first element of a list. Passing an empty list
     * to first returns nil.
     */
    public static TokensListOrToken first(TokensList tokens)
            throws SyntaxException, ArityException {

        if (tokens.size() != 2) {
            throw new ArityException("clojure.core/first");
        }
        // Check if second arg is a list.
        TokensListOrToken secondArg = tokens.get(1);
        if (secondArg instanceof TokensList
                && ((TokensList) secondArg).get(0) instanceof Token
                && ((Token) ((TokensList) secondArg).get(0)).toString().equals("list")) {

            TokensList list = (TokensList) secondArg;

            if (list.size() == 1) {
                return new Token("nil"); // for empty list
            }
            return list.get(1);
        }
        throw new SyntaxException("Illegal list passed to clojure.core/first");
    }

    /**
     * Returns a list without its first element. Passing an
     * empty list to rest returns an empty list.
     */
    public static TokensList rest(TokensList tokens)
            throws SyntaxException, ArityException {
        // The only arg passed to first should be a single list.
        // First check there is one arg.
        if (tokens.size() != 2) {
            throw new ArityException("clojure.core/rest");
        }
        // Check arg is a list.
        TokensListOrToken secondArg = tokens.get(1);
        if (secondArg instanceof TokensList
                && ((TokensList) secondArg).get(0) instanceof Token
                && ((Token) ((TokensList) secondArg).get(0)).toString().equals("list")) {

            TokensList list = new TokensList((TokensList) secondArg);

            if (list.size() > 1) {
                list.remove(1);
            }
            return list;
        }
        throw new SyntaxException("Illegal list passed to clojure.core/rest");
    }
}
