package com.lissajouslaser;

/**
 * Class for managing exceptions arising from incorrectly
 * formatted syntax inputted by the user.
 */
public class SyntaxException extends Exception {

    public SyntaxException() {}

    @Override
    public String toString() {
        return "Syntax error";
    }
}
