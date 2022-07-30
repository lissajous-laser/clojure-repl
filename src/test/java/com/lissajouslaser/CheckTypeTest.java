package com.lissajouslaser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for CheckType class.
 */
public class CheckTypeTest {

    @Test
    public void isValidSymbolWorks1() throws SyntaxException {
        assertTrue(CheckType.isValidSymbol("a-number-4"));
    }

    @Test
    public void isValidSymbolWorks2() throws SyntaxException {
        assertFalse(CheckType.isValidSymbol("1st-Number"));
    }

    @Test
    public void isNumberWorks1() throws SyntaxException {
        assertTrue(CheckType.isValidSymbol("-52"));
    }

    @Test
    public void isValidBoolWorks1() throws SyntaxException {
        assertTrue(CheckType.isBool("true"));
    }
}
