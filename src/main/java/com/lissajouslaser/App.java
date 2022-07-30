package com.lissajouslaser;

/**
 * Class for starting application.
 */
public final class App {
    private App() {
    }

    /**
     * Instantiates Loop and starts repl.
     */
    public static void main(String[] args) throws SyntaxException, ArityException {
        Loop repl = new Loop();
        repl.start();
    }
}
