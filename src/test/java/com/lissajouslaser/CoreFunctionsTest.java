package com.lissajouslaser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for CoreFunctions class.
 */
public class CoreFunctionsTest {
    @Test
    public void addWorks1() {
        String[] args = {"3", "4", "5"};
        assertEquals("12", CoreFunctionsArithmetic.add(args));
    }

    @Test
    public void subWorks() {
        String[] args = {"3", "4", "5"};
        assertEquals("-6", CoreFunctionsArithmetic.sub(args));
    }

    @Test
    public void mulWorks() {
        String[] args = {"3", "4", "5"};
        assertEquals("60", CoreFunctionsArithmetic.mul(args));
    }

    @Test
    public void divWorks() {
        String[] args = {"60", "5", "4"};
        assertEquals("3", CoreFunctionsArithmetic.div(args));
    }

    @Test
    public void divByZeroReturnsErrMsg() {
        String[] args = {"3", "0"};
        assertThrows(ArithmeticException.class, () -> {
            CoreFunctionsArithmetic.div(args);
        });
    }

    @Test
    public void modWorks() throws SyntaxException, ArityException {
        String[] args = {"38", "5"};
        assertEquals("3", CoreFunctionsArithmetic.mod(args));
    }

    @Test
    public void listWorks() {
        String[] tokens = {"3", "4", "5"};
        assertEquals("(list 3 4 5)", CoreFunctionsList.list(tokens));
    }

    @Test
    public void firstWorks1() throws ArityException {
        String[] args = {"(list 3 4 5)"};
        try {
            assertEquals("3", CoreFunctionsList.first(args));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void firstWorks2() throws ArityException {
        String[] args = {"(list 5)"};
        try {
            assertEquals("5", CoreFunctionsList.first(args));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void firstWorks3() throws SyntaxException, ArityException {
        String[] args = {"(list)"};
        assertEquals("nil", CoreFunctionsList.first(args));
    }

    @Test
    public void restWorks() throws ArityException {
        String[] args = {"(list 3 4 5)"};
        try {
            assertEquals("(list 4 5)", CoreFunctionsList.rest(args));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void restWorks2() throws SyntaxException, ArityException {
        String[] args = {"(list)"};
        assertEquals("(list)", CoreFunctionsList.rest(args));
    }

    @Test
    public void ltWorks1() {
        String[] args = {"3", "4", "5"};
        assertEquals("true", CoreFunctionsComparator.lt(args));
    }

    @Test
    public void ltWorks2() {
        String[] args = {"3", "5", "4"};
        assertEquals("false", CoreFunctionsComparator.lt(args));
    }

    @Test
    public void ltWorks3() {
        String[] args = {"3"};
        assertEquals("true", CoreFunctionsComparator.lt(args));
    }

    @Test
    public void gtWorks1() {
        String[] args = {"3", "2", "1"};
        assertEquals("true", CoreFunctionsComparator.gt(args));
    }

    @Test
    public void gtWorks2() {
        String[] args = {"3", "5", "4"};
        assertEquals("false", CoreFunctionsComparator.gt(args));
    }

    @Test
    public void gtWorks3() {
        String[] args = {"3"};
        assertEquals("true", CoreFunctionsComparator.gt(args));
    }

    @Test
    public void eqWorks1() {
        String[] args = {"3", "3", "3"};
        assertEquals("true", CoreFunctionsComparator.eq(args));
    }

    @Test
    public void eqWorks2() {
        String[] args = {"3", "3", "4"};
        assertEquals("false", CoreFunctionsComparator.eq(args));
    }

    @Test
    public void consWorks1() throws ArityException {
        String[] args = {"3", "(list 4 5)"};
        try {
            assertEquals("(list 3 4 5)", CoreFunctionsList.cons(args));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void consWorks2() throws ArityException {
        String[] args = {"(list 3)", "(list 4 5)"};
        try {
            assertEquals("(list (list 3) 4 5)", CoreFunctionsList.cons(args));
        } catch (SyntaxException e) {
        }
    }

    @Test
    public void andWorks1() throws SyntaxException {
        String[] args = {"true", "true", "3"};
        assertEquals("true", CoreFunctionsBoolean.and(args));
    }

    @Test
    public void andWorks2() throws SyntaxException {
        String[] args = {"false", "nil", "false"};
        assertEquals("false", CoreFunctionsBoolean.and(args));
    }

    @Test
    public void orWorks1() throws SyntaxException {
        String[] args = {"false", "nil", "true"};
        assertEquals("true", CoreFunctionsBoolean.or(args));
    }

    @Test
    public void orWorks2() throws SyntaxException {
        String[] args = {"false", "nil", "false"};
        assertEquals("false", CoreFunctionsBoolean.and(args));
    }

    @Test
    public void notWorks1() throws SyntaxException, ArityException {
        String[] args = {"false"};
        assertEquals("true", CoreFunctionsBoolean.not(args));
    }

    @Test
    public void notWorks2() throws SyntaxException, ArityException {
        String[] args = {"true"};
        assertEquals("false", CoreFunctionsBoolean.not(args));
    }

    @Test
    public void notWorks3() throws SyntaxException, ArityException {
        String[] args = {"nil"};
        assertEquals("true", CoreFunctionsBoolean.not(args));
    }

    @Test
    public void notWorks4() throws SyntaxException, ArityException {
        String[] args = {"-200"};
        assertEquals("false", CoreFunctionsBoolean.not(args));
    }

    @Test
    public void consingOntoANonListThrowsException() {
        String[] args = {"3", "true"};
        assertThrows(SyntaxException.class, () -> {
            CoreFunctionsList.cons(args);
        });
    }

    @Test
    public void consingWithThreeArgsThrowsException() {
        String[] args = {"3", "4", "(list 5 6)"};
        assertThrows(ArityException.class, () -> {
            CoreFunctionsList.cons(args);
        });
    }

    @Test
    public void callingFirstWithANonListThrowsException() {
        String[] args = {"true"};
        assertThrows(SyntaxException.class, () -> {
            CoreFunctionsList.first(args);
        });
    }

    @Test
    public void callingFirstWithTwoArgsThrowsException() {
        String[] args = {"(list 1 2)", "(list 3 4)"};
        assertThrows(ArityException.class,  () -> {
            CoreFunctionsList.first(args);
        });
    }

    @Test
    public void callingRestWithANonListThrowsException() {
        String[] args = {"true"};
        assertThrows(SyntaxException.class, () -> {
            CoreFunctionsList.rest(args);
        });
    }

    @Test
    public void callingRestWithTwoArgsThrowsException() {
        String[] args = {"(list 1 2)", "(list 3 4)"};
        assertThrows(ArityException.class,  () -> {
            CoreFunctionsList.rest(args);
        });
    }

    @Test
    public void callingModWithOneArgThrowsException() {
        String[] args = {"80"};
        assertThrows(ArityException.class,  () -> {
            CoreFunctionsArithmetic.mod(args);
        });
    }

    @Test
    public void callingNotWithTwoArgsThrowsException() {
        String[] args = {"true", "false"};
        assertThrows(ArityException.class,  () -> {
            CoreFunctionsBoolean.not(args);
        });
    }
}
