package com.lissajouslaser;

/**
 * Exception for errors caused by passing the wrong number of
 * arguemnts to a function.
 */
public class ArityException extends Exception {
    private String fnName;

    public ArityException() {
        this.fnName = "";
    }

    public ArityException(String msg) {
        this.fnName =  " to " + msg;
    }

    @Override
    public String toString() {
        return "Error - wrong number of args passed" + fnName;
    }
}
