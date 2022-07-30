package com.lissajouslaser;

/**
 * An exception arising from incorrect
 * syntax inputted by the user.
 */
public class SyntaxException extends Exception {
    private String msg;

    public SyntaxException() {
        this.msg = "";
    }

    public SyntaxException(String msg) {
        this.msg = "\n" + msg;
    }

    @Override
    public String toString() {
        return "Syntax Exception" + msg;
    }
}
