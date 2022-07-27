package com.lissajouslaser;

/**
 * Class for exceptions caused by using a symbol not
 * previously defined.
 */
public class UnresolvedSymbolException extends Exception {
    private String msg;

    public UnresolvedSymbolException() {
        this.msg = "";
    }

    public UnresolvedSymbolException(String msg) {
        this.msg =  " to: " + msg;
    }

    @Override
    public String toString() {
        return "Arity Exception - wrong number of args passed" + msg;
    }
}
