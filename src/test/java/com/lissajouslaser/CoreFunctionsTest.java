package com.lissajouslaser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals("Error - Cannot divide by zero", CoreFunctionsArithmetic.div(args));
    }

    @Test
    public void listWorks() {
        String[] tokens = {"3", "4", "5"};
        assertEquals("(list 3 4 5)", CoreFunctionsList.list(tokens));
    }

    @Test
    public void firstWorks() {
        String[] args = {"(list 3 4 5)"};
        assertEquals("3", CoreFunctionsList.first(args));
    }

    @Test
    public void restWorks() {
        String[] args = {"(list 3 4 5)"};
        assertEquals("(list 4 5)", CoreFunctionsList.rest(args));
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
}
