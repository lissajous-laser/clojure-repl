package com.lissajouslaser;

import com.lissajouslaser.functions.Add;
import com.lissajouslaser.functions.And;
import com.lissajouslaser.functions.Cons;
import com.lissajouslaser.functions.Divide;
import com.lissajouslaser.functions.Equals;
import com.lissajouslaser.functions.First;
import com.lissajouslaser.functions.GreaterThan;
import com.lissajouslaser.functions.LessThan;
import com.lissajouslaser.functions.Mod;
import com.lissajouslaser.functions.Multiply;
import com.lissajouslaser.functions.Not;
import com.lissajouslaser.functions.Or;
import com.lissajouslaser.functions.Rest;
import com.lissajouslaser.functions.Subtract;
import java.util.Map;
// import java.util.Scanner;

/**
 * Class for starting application.
 */
public final class App {
    private App() {
    }

    /**
     * Entry point of program.
     */
    public static void main(String[] args) throws SyntaxException, ArityException {
        java.util.List<Function> functions = java.util.List.of(
            new Add(),
            new And(),
            new Cons(),
            new Divide(),
            new Equals(),
            new First(),
            new GreaterThan(),
            new LessThan(),
            new com.lissajouslaser.functions.List(),
            new Mod(),
            new Multiply(),
            new Not(),
            new Or(),
            new Rest(),
            new Subtract()
        );

        Map<Token, Function> definedFunctions = NamespaceInitialiser.run(functions);

        Evaluate evaluate = new Evaluate(definedFunctions);
        // Loop loop = new Loop(evaluate, new Scanner(System.in));
        // loop.start();

        evaluate.eval("(defn factorial (n) "
                + "(if (= n 1) "
                + "1 "
                + "(* n (factorial (- n 1)))))");
        System.out.println(evaluate.eval("(factorial 4)").toString());
    }
}
