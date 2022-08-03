package com.lissajouslaser;

import com.lissajouslaser.functions.Add;
import com.lissajouslaser.functions.And;
import com.lissajouslaser.functions.Cons;
import com.lissajouslaser.functions.Def;
import com.lissajouslaser.functions.Defn;
import com.lissajouslaser.functions.Divide;
import com.lissajouslaser.functions.Empty;
import com.lissajouslaser.functions.Equals;
import com.lissajouslaser.functions.First;
import com.lissajouslaser.functions.Fn;
import com.lissajouslaser.functions.GreaterThan;
import com.lissajouslaser.functions.If;
import com.lissajouslaser.functions.LessThan;
import com.lissajouslaser.functions.List;
import com.lissajouslaser.functions.Mod;
import com.lissajouslaser.functions.Multiply;
import com.lissajouslaser.functions.Not;
import com.lissajouslaser.functions.Or;
import com.lissajouslaser.functions.Rest;
import com.lissajouslaser.functions.Subtract;
import java.util.Map;
import java.util.Scanner;

/**
 * Class for starting application.
 */
public final class App {
    private App() {
    }

    /**
     * Entry point of program.
     */
    public static void main(String[] args)
            throws SyntaxException, ArityException {

        java.util.List<Function> functions = java.util.List.of(
            new Add(),
            new And(),
            new Cons(),
            new Divide(),
            new Def(),
            new Defn(),
            new Equals(),
            new Empty(),
            new First(),
            new Fn(),
            new GreaterThan(),
            new If(),
            new LessThan(),
            new List(),
            new Mod(),
            new Multiply(),
            new Not(),
            new Or(),
            new Rest(),
            new Subtract()
        );

        Map<Token, TokensListOrToken> definedFunctions = NamespaceInitialiser.run(functions);

        Evaluate evaluate = new Evaluate(definedFunctions);
        Loop loop = new Loop(evaluate, new Scanner(System.in));
        loop.start();

        // System.out.println("(+ (+ 1 2) (+ 3 4) 5 (+ 6 7) 8 9)" + "  "
        // + Tokeniser.run("(+ (+ 1 2) (+ 3 4) 5 (+ 6 7) 8 9)"));
        // System.out.println("(+ (+ 1 2)(+ 3 4))" + "  " + Tokeniser.run("(+ (+ 1 2)(+ 3 4))"));
        // System.out.println("(+ (+ 1 2)3)" + "  " + Tokeniser.run("(+ (+ 1 2)3)"));
        // System.out.println("(+(+ 1 2) 3)" + "  " + Tokeniser.run("(+(+ 1 2) 3)"));
        // evaluate.eval("(defn curryed-division (divisor) "
        //         + "(fn (dividend) (/ dividend divisor))) ");
        // evaluate.eval("(def divide-by-2 (curryed-division 2))");
        // System.out.println(evaluate.eval("(divide-by-2 30)"));
    }
}
