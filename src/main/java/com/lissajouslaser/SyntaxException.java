package com.lissajouslaser;

/**
 * Class for managing exceptions arising from incorrectly
 * formatted syntax inputted by the user.
 */
public class SyntaxException extends Exception {
    private String msg;

    public SyntaxException() {
        this.msg = "";
    }

    public SyntaxException(String msg) {
        this.msg =  msg;
    }

    @Override
    public String toString() {
        return "Syntax error";
    }
}
