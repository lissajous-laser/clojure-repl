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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tets for Evaluate class.
 */
public class EvaluateTest {
    private Map<Token, TokensListOrToken> definitions = NamespaceInitialiser.run(
        java.util.List.of(
            new Add(),
            new And(),
            new Cons(),
            new Def(),
            new Defn(),
            new Divide(),
            new Empty(),
            new Equals(),
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
        )
    );

    @Test
    public void evalWorks1() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("5", evaluate.eval(new Token("(+ 2 3)")).toString());
    }

    @Test
    public void evalWorks2() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("(list 3 4 5)",
                evaluate.eval(new Token("(cons (+ 1 2) (list 4 5))")).toString());
    }

    @Test
    public void evalWorks3() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("45", evaluate.eval("(* 9 (+ 2 3))").toString());
    }

    @Test
    public void evalWorks4() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("31",
                evaluate.eval("(first (rest (list 30 31 32)))").toString());
    }

    @Test
    public void evalWorks5() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("true", evaluate.eval("(< (/ 10 3) 4)").toString());
    }

    @Test
    public void evalWorks6() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("true", evaluate.eval("(and (> 5 -3) (< 6 9))").toString());
    }

    @Test
    public void evalWorks7() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("(list -3 -4 -5)",
                evaluate.eval("(cons -3 (cons -4 (list -5)))").toString());
    }

    @Test
    public void evalWorks8() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("(list -3)", evaluate.eval("(cons -3 (list))").toString());
    }

    @Test
    public void defCreatesEntryInDefinedValues()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a 45)");
        Token valueOfA = (Token) evaluate.getDefinitions().get(new Token("a"));
        assertEquals("45", valueOfA.toString());
    }

    @Test
    public void useDefinedValueInAnExpression1()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a -3)");
        assertEquals("-10", evaluate.eval("(/ 30 a)").toString());
    }

    @Test
    public void useDefinedValueInAnExpression2()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a false)");
        assertEquals("false", evaluate.eval("(and 10 true a)").toString());
    }

    @Test
    public void ifWorks1() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("4", evaluate.eval("(if (= 3 3) 4 5)").toString());
    }

    @Test
    public void ifWorks2() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("4", evaluate.eval("(if (first (list 3)) 4 5)").toString());
    }

    @Test
    public void ifWorks3() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("5", evaluate.eval("(if (first (list)) 4 5)").toString());
    }

    @Test
    public void ifWorks4() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("nil", evaluate.eval("(if false 4)").toString());
    }

    @Test
    public void defnStoresFunction() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn increment (n) (+ n 1))");
        assertTrue(evaluate.getDefinitions().get(new Token("increment")) != null);
    }

    @Test
    public void userDefinedFnWorks1() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn increment (n) (+ n 1))");
        assertEquals("4", evaluate.eval("(increment 3)").toString());
    }

    @Test
    public void userDefinedFnWorks2() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn product-1st-2-list-items (l)"
                + " (* (first l) (first (rest l))))");
        assertEquals(
                "42",
                evaluate.eval("(product-1st-2-list-items (list 6 7))").toString()
        );
    }

    @Test
    public void userDefinedFnWorks3() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn xor (x y) "
                + "(and (or x y) (or (not x) (not y))))");
        assertEquals("true", evaluate.eval("(xor true false)").toString());
    }

    // Test recursive function factorial.
    @Test
    public void userDefinedFnWorksRecursive1() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
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
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn sum (l) "
                + "(if (= (first l) nil) "
                + "0 "
                + "(+ (first l) (sum (rest l)))))");
        assertEquals("10", evaluate.eval("(sum (list 1 2 3 4))").toString());
    }

    // Test recursive function Euclid's gcd algorithm.
    @Test
    public void userDefinedFnWorksRecursive3() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
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
        Evaluate evaluate = new Evaluate(definitions);
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
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a 10)");
        evaluate.eval("(defn num-identity (a) (* 1 a))");
        evaluate.eval("(num-identity 20)");
        assertEquals("10", evaluate.eval("a").toString());
    }

    @Test
    public void showUserDefinedFnsAndValuesExistInSameNamespace()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a 10)");
        evaluate.eval("(defn a (a) (* a a))");
        assertThrows(ClassCastException.class, () -> {
            evaluate.eval("(a a)").toString();
        });
    }

    @Test
    public void zeroArgumentFnWorks() throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn returnZero () 0)");
        assertEquals("0", evaluate.eval("(returnZero)").toString());
    }

    @Test
    public void coreFunctionsCanBeOverridden()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn not (x) nil)");
        assertEquals("nil", evaluate.eval("(not false)").toString());
    }

    // Initial argument and successive results
    // 5 10 13 26 29 58 61 122
    @Test
    public void twoFunctionsCallingEachOtherWorks()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        // Declare a dummy add3() first so that input validation
        // for double() passes.
        evaluate.eval("(defn add3 () 3)");
        evaluate.eval("(defn double (x) (add3 (* x 2)))");
        evaluate.eval("(defn add3 (x) (if (> x 100) x (double (+ x 3))))");
        assertEquals("122", evaluate.eval("(double 5)").toString());
    }

    // Test that the namespace of a function is updated before
    // each function application.
    @Test
    public void redefiningAFunctionChangesTheResultOfAFunctionThatCallsIt()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn change (x) (- 0 x))");
        evaluate.eval("(defn doubleChange (x) (* 2 (change x)))");
        evaluate.eval("(defn change (x) (- 32 x))");
        assertEquals("46", evaluate.eval("(doubleChange 9)").toString());
    }

    @Test
    public void callingFirstWithANestedListReturnsAList()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals(
            "(list 1 2)",
            evaluate.eval("(first (list (list 1 2) (list 3 4)))").toString()
        );
    }

    @Test
    public void listsCanBeDefined()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a-list (list 1 2 3))");
        assertEquals("(list 1 2 3)", evaluate.eval("a-list").toString());
    }

    @Test
    public void consingADefinedListDoesNotChangeTheDefinedList()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a-list (list 1 2 3))");
        evaluate.eval("(cons 0 a-list)");
        assertEquals("(list 1 2 3)", evaluate.eval("a-list").toString());
    }

    @Test
    public void callingRestWithADefinedListDoesNotChangeTheDefinedList()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a-list (list 1 2 3))");
        evaluate.eval("(rest a-list)");
        assertEquals("(list 1 2 3)", evaluate.eval("a-list").toString());
    }

    @Test void changingAListAccessedFromADefinedListDoesNotChangeTheDefinedList()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def a-list (list (list 1) 2 3))");
        evaluate.eval("(cons 0 (first a-list))");
        assertEquals("(list (list 1) 2 3)", evaluate.eval("a-list").toString());
    }

    @Test
    public void definingAListUsingADefinedListWorks()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def list1 (list 1 2 3))");
        evaluate.eval("(def list2 (cons 0 list1))");
        assertEquals("(list 0 1 2 3)", evaluate.eval("list2").toString());
    }

    @Test
    public void equalsIsDeeplyEqual()
            throws SyntaxException, ArityException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(def list1 (list 3 4 5 (list 5 6) (list 7 (list 8))))");
        evaluate.eval("(def list2 (list 3 4 5 (list 5 6) (list 7 (list 8))))");
        assertEquals("true", evaluate.eval("(= list1 list2)").toString());
    }

    @Test
    public void namingAFunctionWithANumberThrowsException() {
        Evaluate evaluate = new Evaluate(definitions);
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(defn 3 (a) (* a a))");
        });
    }

    @Test
    public void notHavingFunctionParametersInAListThrowsException() {
        Evaluate evaluate = new Evaluate(definitions);
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(defn square a (* a a))");
        });
    }

    @Test
    public void havingFunctionParametersAsANestedListThrowsException() {
        Evaluate evaluate = new Evaluate(definitions);
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(defn square ((first (list a))) (* a a))");
        });
    }

    @Test
    public void havingTwoBodiesInDefnThrowsException() {
        Evaluate evaluate = new Evaluate(definitions);
        assertThrows(ArityException.class, () -> {
            evaluate.eval("(defn square (a) (* a a) (* a a))");
        });
    }

    @Test
    public void passingTooManyArgsToAUserDefinedFunctionThrowsException()
            throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn square (a) (* a a))");
        assertThrows(ArityException.class, () -> {
            evaluate.eval("(square 6 7)");
        });
    }

    @Test
    public void callingAnUndefinedFuctionThrowsException() {
        Evaluate evaluate = new Evaluate(definitions);
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(expt 2 3)");
        });
    }

    @Test
    public void namingValueWithBooleanThrowsException() {
        Evaluate evaluate = new Evaluate(definitions);
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(def true false)");
        });
    }

    @Test
    public void invalidValueThrowsException() {
        Evaluate evaluate = new Evaluate(definitions);
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("$50");
        });
    }

    @Test
    public void usingUndefinedValueThrowsException() {
        Evaluate evaluate = new Evaluate(definitions);
        assertThrows(SyntaxException.class, () -> {
            evaluate.eval("(+ a 3");
        });
    }

    @Test
    public void fnWorks1() throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("4", evaluate.eval("((fn (x) (+ x 1)) 3)").toString());
    }

    @Test
    public void highOrderFunctionWorks1() throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn do-something (x) (x 100 8))");
        assertEquals("4", evaluate.eval("(do-something mod)").toString());
    }

    // Implement map function.
    @Test
    public void highOrderFunctionWorks2() throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn map (function numbers)"
                + " (if (= (first numbers) nil)"
                + " (list)"
                + " (cons (function (first numbers))"
                + " (map function (rest numbers)))))");
        evaluate.eval("(defn increment (x) (+ x 1))");
        assertEquals(
            "(list 2 3 4)",
            evaluate.eval("(map increment (list 1 2 3))"
        ).toString());
    }

    // Implement map function.
    @Test
    public void highOrderFunctionWorks3() throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn map (function numbers)"
                + " (if (= (first numbers) nil)"
                + " (list)"
                + " (cons (function (first numbers))"
                + " (map function (rest numbers)))))");
        assertEquals(
            "(list 1 3 5)",
            evaluate.eval("(map first (list (list 1 2) (list 3 4) (list 5 6)))"
        ).toString());
    }

    // Implement filter function.
    @Test
    public void highOrderFunctionWorks4() throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn filter (function numbers)"
                + " (if (empty? numbers)"
                + " (list)"
                + " (if (function (first numbers))"
                + " (cons (first numbers) (filter function (rest numbers)))"
                + " (filter function (rest numbers)))))");
        evaluate.eval("(defn multiple-of-3? (x) (= (mod x 3) 0))");
        assertEquals(
            "(list 3 6 9)",
            evaluate.eval("(filter multiple-of-3? (list 1 2 3 4 5 6 7 8 9))"
        ).toString());
    }

    // Implement reduce function.
    @Test
    public void highOrderFunctionWorks5() throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn reduce (function accum numbers)"
                + " (if (empty? numbers)"
                + " accum"
                + " (reduce function"
                + " (function accum (first numbers))"
                + " (rest numbers))))");
        assertEquals(
            "15",
            evaluate.eval("(reduce + 0 (list 1 2 3 4 5))"
        ).toString());
    }

    @Test
    public void higherOrderFunctionWithAnonymousFunctionWorks()
            throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        evaluate.eval("(defn filter [function numbers]"
                + " (if (empty? numbers)"
                + " (list)"
                + " (if (function (first numbers))"
                + " (cons (first numbers) (filter function (rest numbers)))"
                + " (filter function (rest numbers)))))");
        assertEquals(
            "(list 12 5 0 0 5 12)",
            evaluate.eval("(filter (fn [x] (or (> x 0) (= x 0))) "
            + "(list 12 5 0 -3 -4 -3 0 5 12))"
        ).toString());
    }

    @Test
    public void tokeniserHandlesInputsWithoutOptionalSpaces1()
            throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("6", evaluate.eval("(+(+ 1 2) 3)").toString());
    }

    @Test
    public void tokeniserHandlesInputsWithoutOptionalSpaces2()
            throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("6", evaluate.eval("(+ (+ 1 2)3)").toString());
    }

    @Test
    public void tokeniserHandlesInputsWithoutOptionalSpaces3()
            throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals("10", evaluate.eval("(+ (+ 1 2)(+ 3 4))").toString());
    }

    @Test
    public void tokeniserHandlesInputsWithoutOptionalSpaces4()
            throws ArityException, SyntaxException {
        Evaluate evaluate = new Evaluate(definitions);
        assertEquals(
                "45",
                evaluate.eval("(+ (+ 1 2) (+ 3 4) 5 (+ 6 7) 8 9)"
        ).toString());
    }

}
