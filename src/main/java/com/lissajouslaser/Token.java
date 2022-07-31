package com.lissajouslaser;

/**
 * Encapsulates a parsed token. A token is immutable because
 * its instance variable is final and is a String. Integers
 * and boolean types are represented as Strings due to the
 * complexity of implementing a dynamically typed language in
 * a statically typed language.
 */
public class Token implements TokensListOrToken {
    private final String tokenStr;

    public Token(String tokenStr) {
        this.tokenStr = tokenStr;
    }

    @Override
    public String toString() {
        return tokenStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        if (((Token) obj).toString().equals(this.toString())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return tokenStr.hashCode();
    }
}
