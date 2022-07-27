package com.lissajouslaser;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tets for Evaluate class.
 */
public class EvaluateTest {

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
    public void dispatcherToListWorks() throws SyntaxException {
        Evaluate eval = new Evaluate();
        String[] tokensArr = {"list", "3", "4", "5"};
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(tokensArr));
        assertEquals("(list 3 4 5)", eval.dispatcher(tokens));
    }

    @Test
    public void evalWorks1() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("5", evaluate.eval("(+ 2 3)"));
    }

    @Test
    public void evalWorks2() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("(list 3 4 5)",
                evaluate.eval("(cons (+ 1 2) (list 4 5))"));
    }

    @Test
    public void evalWorks3() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("45", evaluate.eval("(* 9 (+ 2 3))"));
    }

    @Test
    public void evalWorks4() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("31", evaluate.eval("(first (rest (list 30 31 32)))"));
    }

    @Test
    public void evalWorks5() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("true", evaluate.eval("(< (/ 10 3) 4)"));
    }

    @Test
    public void evalWorks6() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("true", evaluate.eval("(and (> 5 -3) (< 6 9))"));
    }

    @Test
    public void evalWorks7() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("(list -3 -4 -5)",
                evaluate.eval("(cons -3 (cons -4 (list -5)))"));
    }

    @Test
    public void evalWorks8() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("(list -3)", evaluate.eval("(cons -3 (list))"));
    }

    @Test
    public void isValidSymbolWorks1() throws SyntaxException {
        assertTrue(Evaluate.isValidSymbol("a-number-4"));
    }

    @Test
    public void isValidSymbolWorks2() throws SyntaxException {
        assertFalse(Evaluate.isValidSymbol("1st-Number"));
    }

    @Test
    public void isNumberWorks1() throws SyntaxException {
        assertTrue(Evaluate.isValidSymbol("-52"));
    }

    @Test
    public void isValidBoolWorks1() throws SyntaxException {
        assertTrue(Evaluate.isBool("true"));
    }

    @Test
    public void defWorks1() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a 45)");
        assertEquals("45", evaluate.getDefinedValues().get("a"));
    }

    @Test
    public void useDefinedValueInAnExpression1() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a -3)");
        assertEquals("-10", evaluate.eval("(/ 30 a)"));
    }

    @Test
    public void useDefinedValueInAnExpression2() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a false)");
        assertEquals("false", evaluate.eval("(and 10 true a)"));
    }

    @Test
    public void ifWorks1() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("4", evaluate.eval("(if (= 3 3) 4 5)"));
    }

    @Test
    public void ifWorks2() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("4", evaluate.eval("(if (first (list 3)) 4 5)"));
    }

    @Test
    public void ifWorks3() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("5", evaluate.eval("(if (first (list)) 4 5)"));
    }

    @Test
    public void ifWorks4() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        assertEquals("nil", evaluate.eval("(if false 4)"));
    }

    @Test
    public void defnStoresFunction() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn increment (n) (+ n 1))");
        assertTrue(evaluate.getUserDefinedFunctions().get("increment") != null);
    }

    @Test
    public void userDefinedFnWorks1() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn increment (n) (+ n 1))");
        assertEquals("4", evaluate.eval("(increment 3)"));
    }

    @Test
    public void userDefinedFnWorks2() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn product-1st-2-list-items (l)"
                + " (* (first l) (first (rest l))))");
        assertEquals("42", evaluate.eval("(product-1st-2-list-items (list 6 7))"));
    }

    @Test
    public void userDefinedFnWorks3() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn xor (x y) "
                + "(and (or x y) (or (not x) (not y))))");
        assertEquals("true", evaluate.eval("(xor true false)"));
    }

    // Test recursive function factorial.
    @Test
    public void userDefinedFnWorksRecursive1() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn factorial (n) "
                + "(if (= n 1) "
                + "1 "
                + "(* n (factorial (- n 1)))))");
        assertEquals("24", evaluate.eval("(factorial 4)"));
    }

    // Test recursive function sum, giving sum of all numbers
    // in a list.
    @Test
    public void userDefinedFnWorksRecursive2() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn sum (l) "
                + "(if (= (first l) nil) "
                + "0 "
                + "(+ (first l) (sum (rest l)))))");
        assertEquals("10", evaluate.eval("(sum (list 1 2 3 4))"));
    }

    // Test recursive function Euclid's gcd algorithm.
    @Test
    public void userDefinedFnWorksRecursive3() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn gcd (a b) "
                + "(if (= b 0) "
                + "a "
                + "(gcd b (mod a b))))");
        assertEquals("3", evaluate.eval("(gcd 24 9)"));
    }

    // Three functions used to calculate the average of a list
    // of numbers.
    @Test
    public void threeUserDefinedFnsWork() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn sum (l) "
                + "(if (= (first l) nil) "
                + "0 "
                + "(+ (first l) (sum (rest l)))))");
        evaluate.eval("(defn count (l) "
                + "(if (= (first l) nil) "
                + "0 "
                + "(+ 1 (count (rest l)))))");
        evaluate.eval("(defn ave (l) (/ (sum l) (count l)))");
        assertEquals("3", evaluate.eval("(ave (list 1 2 3 4 5))"));
    }

    @Test
    public void functionBindingsGoOutOfScopeAfterFunctionHasReturned()
            throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a 10)");
        evaluate.eval("(defn num-identity (a) (* 1 a))");
        evaluate.eval("(num-identity 20)");
        assertEquals("10", evaluate.eval("a"));
    }

    @Test
    public void showUserDefinedFnsAndValuesExistInDifferentNamespaces()
            throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a 10)");
        evaluate.eval("(defn a (a) (* a a))");
        assertEquals("100", evaluate.eval("(a a)"));
    }

    @Test
    public void zeroArgumentFnWorks() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn returnZero () 0)");
        assertEquals("0", evaluate.eval("(returnZero)"));
    }

    @Test
    public void showCoreFunctionsAreNotOverridden() throws SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn not (x) nil)");
        assertEquals("true", evaluate.eval("(not false)"));
    }
}
