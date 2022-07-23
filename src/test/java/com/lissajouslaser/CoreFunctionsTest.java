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
        assertEquals("12", CoreFunctions.add(args));
    }

    @Test
    public void subWorks() {
        String[] args = {"3", "4", "5"};
        assertEquals("-6", CoreFunctions.sub(args));
    }

    @Test
    public void mulWorks() {
        String[] args = {"3", "4", "5"};
        assertEquals("60", CoreFunctions.mul(args));
    }

    @Test
    public void divWorks() {
        String[] args = {"60", "5", "4"};
        assertEquals("3", CoreFunctions.div(args));
    }

    @Test
    public void divByZeroReturnsErrMsg() {
        String[] args = {"3", "0"};
        assertEquals("Error - Cannot divide by zero", CoreFunctions.div(args));
    }

    @Test
    public void listWorks() {
        String[] tokens = {"3", "4", "5"};
        assertEquals("(list 3 4 5)", CoreFunctions.list(tokens));
    }

    @Test
    public void firstWorks() {
        String[] args = {"(list 3 4 5)"};
        assertEquals("3", CoreFunctions.first(args));
    }

    @Test
    public void restWorks() {
        String[] args = {"(list 3 4 5)"};
        assertEquals("(list 4 5)", CoreFunctions.rest(args));
    }

}
