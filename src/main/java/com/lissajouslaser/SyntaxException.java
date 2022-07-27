package com.lissajouslaser;

/**
 * Class for exceptions arising from incorrect
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
