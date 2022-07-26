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
        Evaluate evaluate = new Evaluate();
        // evaluate.eval("(defn product-1st-2-list-items (l)"
        //         + " (* (first l) (first (rest l) )))");
        // System.out.println(evaluate.eval("(product-1st-2-list-items (list 6 7))"));
        // System.out.println();

        evaluate.eval("(defn factorial (n) (if (= n 1) 1 (* n (factorial (- n 1)))))");
        System.out.println(evaluate.eval("(factorial 3)"));
    }
}
