package com.lissajouslaser;

import java.util.LinkedList;

/**
 * An ArrayList of type TokensListOrToken.
 * It is used to store other TokensLists or Tokens.
 */
public class TokensList extends LinkedList<TokensListOrToken>
        implements TokensListOrToken {

    TokensList() {
        super();
    }

    TokensList(TokensList original) {
        super(original);
    }

    /**
     * Converts an array of Strings into a
     * TokensList of Tokens.
     */
    TokensList(String[] array) {
        super();
        for (String item: array) {
            this.add(new Token(item));
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("(");

        for (int i = 0; i < this.size(); i++) {
            result.append(this.get(i).toString() + " ");
        }
        if (this.size() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        result.append(")");

        return result.toString();
    }
}
