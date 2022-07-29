package com.lissajouslaser;

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
    public void tokeniseWorks1() throws SyntaxException {
        TokensList tokens = new TokensList();
        tokens.add(new Token("+"));
        tokens.add(new Token("2"));
        tokens.add(new Token("3"));
        assertTrue(Evaluate.tokeniseList("(+ 2 3)").equals(tokens));
    }

    @Test
    public void tokeniseThrowsExceptionWhenGivenSyntaxError() {
        assertThrows(SyntaxException.class,
                () -> {
                    Evaluate.tokeniseList("))");
                });
    }

    @Test
    public void dispatcherToListWorks() throws SyntaxException, ArityException {
        Evaluate eval = new Evaluate();
        String[] tokensArr = {"list", "3", "4", "5"};
        TokensList tokens = new TokensList(tokensArr);
        assertEquals("(list 3 4 5)", eval.dispatcher(tokens).toString());
    }

    @Test
    public void evalWorks1() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("5", evaluate.eval(new Token("(+ 2 3)")).toString());
    }

    @Test
    public void evalWorks2() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("(list 3 4 5)",
                evaluate.eval(new Token("(cons (+ 1 2) (list 4 5))")).toString());
    }

    @Test
    public void evalWorks3() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("45", evaluate.eval("(* 9 (+ 2 3))").toString());
    }

    @Test
    public void evalWorks4() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("31",
                evaluate.eval("(first (rest (list 30 31 32)))").toString());
    }

    @Test
    public void evalWorks5() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("true", evaluate.eval("(< (/ 10 3) 4)").toString());
    }

    @Test
    public void evalWorks6() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("true", evaluate.eval("(and (> 5 -3) (< 6 9))").toString());
    }

    @Test
    public void evalWorks7() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("(list -3 -4 -5)",
                evaluate.eval("(cons -3 (cons -4 (list -5)))").toString());
    }

    @Test
    public void evalWorks8() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("(list -3)", evaluate.eval("(cons -3 (list))").toString());
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
    public void defCreatesEntryInDefinedValues()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a 45)");
        Token valueOfA = (Token) evaluate.getDefinedValues().get(new Token("a"));
        assertEquals("45", valueOfA.toString());
    }

    @Test
    public void useDefinedValueInAnExpression1()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a -3)");
        assertEquals("-10", evaluate.eval("(/ 30 a)").toString());
    }

    @Test
    public void useDefinedValueInAnExpression2()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a false)");
        assertEquals("false", evaluate.eval("(and 10 true a)").toString());
    }

    @Test
    public void ifWorks1() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("4", evaluate.eval("(if (= 3 3) 4 5)").toString());
    }

    @Test
    public void ifWorks2() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("4", evaluate.eval("(if (first (list 3)) 4 5)").toString());
    }

    @Test
    public void ifWorks3() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("5", evaluate.eval("(if (first (list)) 4 5)").toString());
    }

    @Test
    public void ifWorks4() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals("nil", evaluate.eval("(if false 4)").toString());
    }

    @Test
    public void defnStoresFunction() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn increment (n) (+ n 1))");
        assertTrue(evaluate.getUserDefinedFunctions().get("increment") != null);
    }

    @Test
    public void userDefinedFnWorks1() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn increment (n) (+ n 1))");
        assertEquals("4", evaluate.eval("(increment 3)").toString());
    }

    @Test
    public void userDefinedFnWorks2() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn product-1st-2-list-items (l)"
                + " (* (first l) (first (rest l))))");
        assertEquals(
                "42",
                evaluate.eval("(product-1st-2-list-items (list 6 7))").toString()
        );
    }

    @Test
    public void userDefinedFnWorks3() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn xor (x y) "
                + "(and (or x y) (or (not x) (not y))))");
        assertEquals("true", evaluate.eval("(xor true false)").toString());
    }

    // Test recursive function factorial.
    @Test
    public void userDefinedFnWorksRecursive1() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn factorial (n) "
                + "(if (= n 1) "
                + "1 "
                + "(* n (factorial (- n 1)))))");
        assertEquals("24", evaluate.eval("(factorial 4)").toString());
    }

    // Test recursive function sum, giving sum of all numbers
    // in a list.
    @Test
    public void userDefinedFnWorksRecursive2() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn sum (l) "
                + "(if (= (first l) nil) "
                + "0 "
                + "(+ (first l) (sum (rest l)))))");
        assertEquals("10", evaluate.eval("(sum (list 1 2 3 4))").toString());
    }

    // Test recursive function Euclid's gcd algorithm.
    @Test
    public void userDefinedFnWorksRecursive3() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn gcd (a b) "
                + "(if (= b 0) "
                + "a "
                + "(gcd b (mod a b))))");
        assertEquals("3", evaluate.eval("(gcd 24 9)").toString());
    }

    // Three functions used to calculate the average of a list
    // of numbers.
    @Test
    public void threeUserDefinedFnsWork() throws SyntaxException, ArityException {
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
        assertEquals("3", evaluate.eval("(ave (list 1 2 3 4 5))").toString());
    }

    @Test
    public void functionBindingsGoOutOfScopeAfterFunctionHasReturned()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a 10)");
        evaluate.eval("(defn num-identity (a) (* 1 a))");
        evaluate.eval("(num-identity 20)");
        assertEquals("10", evaluate.eval("a").toString());
    }

    @Test
    public void showUserDefinedFnsAndValuesExistInDifferentNamespaces()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a 10)");
        evaluate.eval("(defn a (a) (* a a))");
        assertEquals("100", evaluate.eval("(a a)").toString());
    }

    @Test
    public void zeroArgumentFnWorks() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn returnZero () 0)");
        assertEquals("0", evaluate.eval("(returnZero)").toString());
    }

    @Test
    public void showCoreFunctionsAreNotOverridden()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn not (x) nil)");
        assertEquals("true", evaluate.eval("(not false)").toString());
    }

    @Test
    public void twoFunctionsCallingEachOtherWorks()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        // Declare a dummy add3() first so that input validation
        // for double() passes.
        evaluate.eval("(defn add3 () 3)");
        evaluate.eval("(defn double (x) (add3 (* x 2)))");
        evaluate.eval("(defn add3 (x) (if (> x 100) x (double (+ x 3))))");
        assertEquals("122", evaluate.eval("(double 5)").toString());
    } // 5 10 13 26 29 58 61 122

    @Test
    public void callingFirstWithANestedListReturnsAList()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        assertEquals(
            "(list 1 2)",
            evaluate.eval("(first (list (list 1 2) (list 3 4)))").toString()
        );
    }

    @Test
    public void listsCanBeDefined()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a-list (list 1 2 3))");
        assertEquals("(list 1 2 3)", evaluate.eval("a-list").toString());
    }

    @Test
    public void consingADefinedListDoesNotChangeTheDefinedList()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a-list (list 1 2 3))");
        evaluate.eval("(cons 0 a-list)");
        assertEquals("(list 1 2 3)", evaluate.eval("a-list").toString());
    }

    @Test
    public void callingRestWithADefinedListDoesNotChangeTheDefinedList()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a-list (list 1 2 3))");
        evaluate.eval("(rest a-list)");
        assertEquals("(list 1 2 3)", evaluate.eval("a-list").toString());
    }

    @Test void changingAListAccessedFromADefinedListDoesNotChangeTheDefinedList()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def a-list (list (list 1) 2 3))");
        evaluate.eval("(cons 0 (first a-list))");
        assertEquals("(list (list 1) 2 3)", evaluate.eval("a-list").toString());
    }

    @Test
    public void definingAListUsingADefinedListWorks()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def list1 (list 1 2 3))");
        evaluate.eval("(def list2 (cons 0 list1))");
        assertEquals("(list 0 1 2 3)", evaluate.eval("list2").toString());
    }

    @Test
    public void equalsIsDeeplyEqual()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(def list1 (list 3 4 5 (list 5 6) (list 7 (list 8))))");
        evaluate.eval("(def list2 (list 3 4 5 (list 5 6) (list 7 (list 8))))");
        assertEquals("true", evaluate.eval("(= list1 list2)").toString());
    }

    @Test
    public void namingAFunctionWithANumberThrowsException() {
        Evaluate evaluate = new Evaluate();
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(defn 3 (a) (* a a))");
        });
    }

    @Test
    public void notHavingFunctionParametersInAListThrowsException() {
        Evaluate evaluate = new Evaluate();
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(defn square a (* a a))");
        });
    }

    @Test
    public void havingFunctionParametersAsANestedListThrowsException() {
        Evaluate evaluate = new Evaluate();
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(defn square ((first (list a))) (* a a))");
        });
    }

    @Test
    public void havingTwoBodiesInDefnThrowsException() {
        Evaluate evaluate = new Evaluate();
        assertThrows(ArityException.class, () -> {
            evaluate.eval("(defn square (a) (* a a) (* a a))");
        });
    }

    @Test
    public void passingTooManyArgsToAUserDefinedFunctionThrowsException()
            throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate();
        evaluate.eval("(defn square (a) (* a a))");
        assertThrows(ArityException.class, () -> {
            evaluate.eval("(square 6 7)");
        });
    }

    @Test
    public void callingAnUndefinedFuctionThrowsException() {
        Evaluate evaluate = new Evaluate();
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(expt 2 3)");
        });
    }

    @Test
    public void namingValueWithBooleanThrowsException() {
        Evaluate evaluate = new Evaluate();
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(def true false)");
        });
    }

    @Test
    public void invalidValueThrowsException() {
        Evaluate evaluate = new Evaluate();
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("$50");
        });
    }

    @Test
    public void usingUndefinedValueThrowsException() {
        Evaluate evaluate = new Evaluate();
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(+ a 3");
        });
    }
}
