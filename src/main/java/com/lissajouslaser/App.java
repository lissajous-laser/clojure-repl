package com.lissajouslaser;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws SyntaxException {
        Loop repl = new Loop();
        repl.start();

        // try {
        //     throw new ArithmeticException("a problem");
        // } catch (Exception e) {
        //     System.out.println(e);
        // }

    }
}
