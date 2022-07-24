package com.lissajouslaser;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tets for Evaluate class.
 */
public class EvaluateTest {

    @Test
    public void checkBalancedParensWorks1() {
        Evaluate eval = new Evaluate();
        assertEquals(true, eval.checkBalancedParens("(hello)"));
    }

    @Test
    public void checkBalancedParensWorks2() {
        Evaluate eval = new Evaluate();
        assertEquals(false, eval.checkBalancedParens("(+ 2) 3)"));
    }

    @Test
    public void checkBalancedParensWorks3() {
        Evaluate eval = new Evaluate();
        assertEquals(true, eval.checkBalancedParens("(+ (first (list 4 5 6)) (inc 7))"));
    }

    @Test
    public void tokeniseWorks1() {
        // String[] tokens = {"+", "2", "3"};
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("+");
        tokens.add("2");
        tokens.add("3");
        // Use deepEquals() for functional equality
        try {
            assertTrue(Evaluate.tokeniseList("(+ 2 3)").equals(tokens));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void tokeniseThrowsExceptionWhenGivenSyntaxError() {
        SyntaxException syntaxExc = assertThrows(SyntaxException.class,
                () -> {
                    Evaluate.tokeniseList(")))");
                });
    }

    // @Test
    // public void tokeniseWorks3() {
    //     Evaluate eval = new Evaluate();
    //     String[] tokens = {"x"};
    //     assertTrue(Arrays.deepEquals(eval.tokenise("x"), tokens));
    // }

    @Test
    public void dispatcherToListWorks() {
        Evaluate eval = new Evaluate();
        String[] tokensArr = {"list", "3", "4", "5"};
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(tokensArr));
        try {
            assertEquals("(list 3 4 5)", eval.dispatcher(tokens));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void evalWorks1() {
        Evaluate evaluate = new Evaluate();
        try {
            assertEquals("5", evaluate.eval("(+ 2 3)"));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void evalWorks2() {
        Evaluate evaluate = new Evaluate();
        try {
            assertEquals("(list 3 4 5)", evaluate.eval("(cons (+ 1 2) (list 4 5))"));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void evalWorks3() {
        Evaluate evaluate = new Evaluate();
        try {
            assertEquals("45", evaluate.eval("(* 9 (+ 2 3))"));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void evalWorks4() {
        Evaluate evaluate = new Evaluate();
        try {
            assertEquals("31", evaluate.eval("(first (rest (list 30 31 32)))"));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void evalWorks5() {
        Evaluate evaluate = new Evaluate();
        try {
            assertEquals("true", evaluate.eval("(< (/ 10 3) 4)"));
        } catch (SyntaxException e) {
        }
    }

}
