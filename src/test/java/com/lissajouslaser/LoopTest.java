package com.lissajouslaser;

// import java.io.IOException;
// import java.nio.file.Paths;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests for loop class.
 */
public class LoopTest {

    // simKeyboardInput.txt contains repl inputs that will
    // cause exceptions to be thrown. Testing that they
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
                + "(defn xor [x y] (and (or x y) (or (not x) (not y))))\n"
                + "(xor false true)\n"
                + "(quit)\n";
        Scanner scanner = new Scanner(simKeyboardInput);
        Evaluate evaluate = new Evaluate();
        assertDoesNotThrow(() -> {
            Loop repl = new Loop(evaluate, scanner);
            repl.start();
        });
    }
}
