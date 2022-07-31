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
import java.util.Scanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests for loop class.
 */
public class LoopTest {
    private Map<Token, Function> definedFunctions = NamespaceInitialiser.run(
        java.util.List.of(
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
        )
    );

    // Contains repl inputs that will cause
    // exceptions to be thrown. Testing that they
    // are caught by start()
    @Test
    public void incorrectInputDoesNotThrowException() {
        String simKeyboardInput =
                // Invalid inputs.
                "(def true true)\n"
                + "(/ 3 0)\n"
                + "(cons 3 4 (list 5 6))\n"
                + "(+ 1 true)\n"
                // Valid inputs.
                + "(defn xor (x y) (and (or x y) (or (not x) (not y))))\n"
                + "   (xor false true)   \n"
                + "   (quit)   \n";
        Scanner scanner = new Scanner(simKeyboardInput);
        Evaluate evaluate = new Evaluate(definedFunctions);
        assertDoesNotThrow(() -> {
            Loop repl = new Loop(evaluate, scanner);
            repl.start();
        });
    }
}
