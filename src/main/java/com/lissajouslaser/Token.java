package com.lissajouslaser;

/**
 * Encapsulates a parsed token. A token is immutable because
 * its instance variable is final and is also a String.
 */
public class Token implements TokensListOrToken {
    private final String tokenStr;

    Token(String tokenStr) {
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
