package com.lissajouslaser;

/**
 * Exception for errors caused by passing the wrong number of
 * arguemnts to a function.
 */
public class ArityException extends Exception {
    private String msg;

    public ArityException() {
        this.msg = "";
    }

    public ArityException(String msg) {
        this.msg =  " to: " + msg;
    }

    @Override
    public String toString() {
        return "Arity Exception\nWrong number of args passed" + msg;
    }
}
