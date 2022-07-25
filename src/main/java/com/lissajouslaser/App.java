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
        System.out.println("Hello World!");

        Evaluate evaluate = new Evaluate();
        // System.out.println(evaluate.eval("(if false 4)"));
        // evaluate.eval("(list)");
        System.out.println(evaluate.eval("(list)"));
        System.out.println("--");
        System.out.println(evaluate.eval("(list 2)"));
        // System.out.println(evaluate.eval("(list (list 3) 4)"));
        // System.out.println(evaluate.eval("(first (list 3 4))"));

        // String[] emptylist = {""};
        // System.out.println(CoreFunctionsList.list(emptylist));
        // System.out.println();
        // System.out.println(evaluate.eval("(first (list))"));


    }
}
