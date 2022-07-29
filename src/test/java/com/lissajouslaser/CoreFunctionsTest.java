package com.lissajouslaser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for CoreFunctions class.
 */
public class CoreFunctionsTest {
    @Test
    public void addWorks1() throws ArityException {
        String[] args = {"aFunction", "3", "4", "5"};
        TokensList tokens = new TokensList(args);
        assertEquals("12", CoreFunctionsArithmetic.add(tokens).toString());
    }

    @Test
    public void subWorks() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "4", "5"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("-6", CoreFunctionsArithmetic.sub(tokens).toString());
    }

    @Test
    public void mulWorks() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "4", "5"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("60", CoreFunctionsArithmetic.mul(tokens).toString());
    }

    @Test
    public void divWorks() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "60", "5", "4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("3", CoreFunctionsArithmetic.div(tokens).toString());
    }

    @Test
    public void divByZeroThrowsException() {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "0"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertThrows(ArithmeticException.class, () -> {
            CoreFunctionsArithmetic.div(tokens);
        });
    }

    @Test
    public void modWorks() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "38", "5"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("3", CoreFunctionsArithmetic.mod(tokens).toString());
    }

    @Test
    public void firstWorks1() throws ArityException, SyntaxException {
        String[] args1 = {"first"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "3", "4", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("3", CoreFunctionsList.first(parentTokens).toString());
    }

    @Test
    public void firstWorks2() throws ArityException, SyntaxException {
        String[] args1 = {"first"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("5", CoreFunctionsList.first(parentTokens).toString());
    }

    @Test
    public void firstWorks3() throws SyntaxException, ArityException {
        String[] args1 = {"first"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("nil", CoreFunctionsList.first(parentTokens).toString());
    }

    @Test
    public void restWorks() throws ArityException, SyntaxException {
        String[] args1 = {"rest"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "3", "4", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("(list 4 5)", CoreFunctionsList.rest(parentTokens).toString());
    }

    @Test
    public void restWorks2() throws SyntaxException, ArityException {
        String[] args1 = {"rest"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("(list)", CoreFunctionsList.rest(parentTokens).toString());
    }

    @Test
    public void consWorks1() throws ArityException, SyntaxException {
        String[] args1 = {"cons", "3"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "4", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("(list 3 4 5)", CoreFunctionsList.cons(parentTokens).toString());
    }

    @Test
    public void consWorks2() throws ArityException, SyntaxException {
        String[] args1 = {"cons"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "3"};
        TokensList childTokens1 = new TokensList(args2);
        String[] args3 = {"list", "4", "5"};
        TokensList childTokens2 = new TokensList(args3);
        parentTokens.add(childTokens1);
        parentTokens.add(childTokens2);
        assertEquals(
                "(list (list 3) 4 5)",
                CoreFunctionsList.cons(parentTokens).toString()
        );
    }

    @Test
    public void ltWorks1() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "4", "5"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", CoreFunctionsComparator.lt(tokens).toString());
    }

    @Test
    public void ltWorks2() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "5", "4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", CoreFunctionsComparator.lt(tokens).toString());
    }

    @Test
    public void ltWorks3() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", CoreFunctionsComparator.lt(tokens).toString());
    }

    @Test
    public void gtWorks1() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "2", "1"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", CoreFunctionsComparator.gt(tokens).toString());
    }

    @Test
    public void gtWorks2() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "5", "4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", CoreFunctionsComparator.gt(tokens).toString());
    }

    @Test
    public void gtWorks3() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", CoreFunctionsComparator.gt(tokens).toString());
    }

    @Test
    public void eqWorks1() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "3", "3"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", CoreFunctionsComparator.eq(tokens).toString());
    }

    @Test
    public void eqWorks2() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", CoreFunctionsComparator.eq(tokens).toString());
    }

    @Test
    public void andWorks1() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "true", "true", "3"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("3", CoreFunctionsBoolean.and(tokens).toString());
    }

    @Test
    public void andWorks2() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "false", "nil", "false"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", CoreFunctionsBoolean.and(tokens).toString());
    }

    @Test
    public void orWorks1() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "false", "nil", "true"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", CoreFunctionsBoolean.or(tokens).toString());
    }

    @Test
    public void orWorks2() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "false", "nil", "false"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", CoreFunctionsBoolean.and(tokens).toString());
    }

    @Test
    public void notWorks1() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "false"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", CoreFunctionsBoolean.not(tokens).toString());
    }

    @Test
    public void notWorks2() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "true"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", CoreFunctionsBoolean.not(tokens).toString());
    }

    @Test
    public void notWorks3() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "nil"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", CoreFunctionsBoolean.not(tokens).toString());
    }

    @Test
    public void notWorks4() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "-200"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", CoreFunctionsBoolean.not(tokens).toString());
    }

    @Test
    public void consingOntoANonListThrowsException() {
        String[] args = {"cons", "3", "true"};
        TokensList tokens = new TokensList(args);
        assertThrows(SyntaxException.class, () -> {
            CoreFunctionsList.cons(tokens);
        });
    }

    @Test
    public void consingWithThreeArgsThrowsException() {
        String[] args = {"cons", "3", "4", "(list 5 6)"};
        TokensList tokens = new TokensList(args);
        assertThrows(ArityException.class, () -> {
            CoreFunctionsList.cons(tokens);
        });
    }

    @Test
    public void callingFirstWithANonListThrowsException() {
        String[] args = {"first", "true"};
        TokensList tokens = new TokensList(args);
        assertThrows(SyntaxException.class, () -> {
            CoreFunctionsList.first(tokens);
        });
    }

    @Test
    public void callingFirstWithTwoArgsThrowsException() {
        String[] args1 = {"first", "3"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "4", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertThrows(ArityException.class,  () -> {
            CoreFunctionsList.first(parentTokens);
        });
    }

    @Test
    public void callingRestWithANonListThrowsException() {
        String[] args = {"rest", "true"};
        TokensList tokens = new TokensList(args);
        assertThrows(SyntaxException.class, () -> {
            CoreFunctionsList.rest(tokens);
        });
    }

    @Test
    public void callingRestWithTwoArgsThrowsException() {
        String[] args1 = {"rest", "3"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "4", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertThrows(ArityException.class,  () -> {
            CoreFunctionsList.rest(parentTokens);
        });
    }

    @Test
    public void callingModWithOneArgThrowsException() {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "80"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertThrows(ArityException.class,  () -> {
            CoreFunctionsArithmetic.mod(tokens);
        });
    }

    @Test
    public void callingNotWithTwoArgsThrowsException() {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "true", "false"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertThrows(ArityException.class,  () -> {
            CoreFunctionsBoolean.not(tokens);
        });
    }
}
