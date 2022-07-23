package com.lissajouslaser;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Evaluate eval = new Evaluate();
        String[] tokens = {"+", "2", "3"};
        // Use deepEquals() for functional equality
        assertTrue(Arrays.deepEquals(eval.tokenise("(+ 2 3)"), tokens));
    }

    // @Test
    // public void tokeniseWorks2() {
    //     Evaluate eval = new Evaluate();
    //     assertEquals(null, eval.tokenise("2 3"));
    // }

    // @Test
    // public void tokeniseWorks3() {
    //     Evaluate eval = new Evaluate();
    //     String[] tokens = {"x"};
    //     assertTrue(Arrays.deepEquals(eval.tokenise("x"), tokens));
    // }

    @Test
    public void dispatcherToListWorks() {
        Evaluate eval = new Evaluate();
        String[] tokens = {"list", "3", "4", "5"};
        assertEquals("(list 3 4 5)", eval.dispatcher(tokens));
    }

}
