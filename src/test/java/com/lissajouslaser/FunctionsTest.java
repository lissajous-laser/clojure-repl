package com.lissajouslaser;

import com.lissajouslaser.functions.Add;
import com.lissajouslaser.functions.And;
import com.lissajouslaser.functions.Cons;
import com.lissajouslaser.functions.Divide;
import com.lissajouslaser.functions.Empty;
import com.lissajouslaser.functions.Equals;
import com.lissajouslaser.functions.First;
import com.lissajouslaser.functions.GreaterThan;
import com.lissajouslaser.functions.LessThan;
import com.lissajouslaser.functions.List;
import com.lissajouslaser.functions.Mod;
import com.lissajouslaser.functions.Multiply;
import com.lissajouslaser.functions.Not;
import com.lissajouslaser.functions.Or;
import com.lissajouslaser.functions.Rest;
import com.lissajouslaser.functions.Subtract;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for functions in the com.lissajouslaser.functions package.
 */
public class FunctionsTest {
    private final Add add = new Add();
    private final Mod mod = new Mod();
    private final And and = new And();
    private final Or or = new Or();
    private final Not not = new Not();
    private final Cons cons = new Cons();
    private final First first = new First();
    private final Rest rest = new Rest();
    private final Empty empty = new Empty();

    @Test
    public void addWorks1() throws ArityException {
        String[] args = {"aFunction", "3", "4", "5"};
        TokensList tokens = new TokensList(args);

        assertEquals("12", add.applyFn(tokens).toString());
    }

    @Test
    public void subWorks() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "4", "5"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        Subtract sub = new Subtract();
        assertEquals("-6", sub.applyFn(tokens).toString());
    }

    @Test
    public void mulWorks() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "4", "5"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        Multiply mul = new Multiply();
        assertEquals("60", mul.applyFn(tokens).toString());
    }

    @Test
    public void divWorks() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "60", "5", "4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        Divide div = new Divide();
        assertEquals("3", div.applyFn(tokens).toString());
    }

    @Test
    public void divByZeroThrowsException() {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "0"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        Divide div = new Divide();
        assertThrows(ArithmeticException.class, () -> {
            div.applyFn(tokens);
        });
    }

    @Test
    public void modWorks() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "38", "5"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("3", mod.applyFn(tokens).toString());
    }

    @Test
    public void modReturnsHighestPossibleWithExceedingDivisor()
            throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "10", "-4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("-2", mod.applyFn(tokens).toString());
    }

    @Test
    public void listWorks() throws ArityException, SyntaxException {
        String[] args = {"list", "3", "4", "5"};
        TokensList tokens = new TokensList(args);
        List list = new List();
        assertEquals("(list 3 4 5)", list.applyFn(tokens).toString());
    }

    @Test
    public void firstWorks1() throws ArityException, SyntaxException {
        String[] args1 = {"first"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "3", "4", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("3", first.applyFn(parentTokens).toString());
    }

    @Test
    public void firstWorks2() throws ArityException, SyntaxException {
        String[] args1 = {"first"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("5", first.applyFn(parentTokens).toString());
    }

    @Test
    public void firstWorks3() throws SyntaxException, ArityException {
        String[] args1 = {"first"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("nil", first.applyFn(parentTokens).toString());
    }

    @Test
    public void restWorks() throws ArityException, SyntaxException {
        String[] args1 = {"rest"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "3", "4", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("(list 4 5)", rest.applyFn(parentTokens).toString());
    }

    @Test
    public void restWorks2() throws SyntaxException, ArityException {
        String[] args1 = {"rest"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("(list)", rest.applyFn(parentTokens).toString());
    }

    @Test
    public void consWorks1() throws ArityException, SyntaxException {
        String[] args1 = {"cons", "3"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "4", "5"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("(list 3 4 5)", cons.applyFn(parentTokens).toString());
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
                cons.applyFn(parentTokens).toString()
        );
    }

    @Test
    public void ltWorks1() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "4", "5"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        LessThan lt = new LessThan();
        assertEquals("true", lt.applyFn(tokens).toString());
    }

    @Test
    public void ltWorks2() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "5", "4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        LessThan lt = new LessThan();
        assertEquals("false", lt.applyFn(tokens).toString());
    }

    @Test
    public void ltWorks3() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        LessThan lt = new LessThan();
        assertEquals("true", lt.applyFn(tokens).toString());
    }

    @Test
    public void gtWorks1() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "2", "1"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        GreaterThan gt = new GreaterThan();
        assertEquals("true", gt.applyFn(tokens).toString());
    }

    @Test
    public void gtWorks2() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "5", "4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        GreaterThan gt = new GreaterThan();
        assertEquals("false", gt.applyFn(tokens).toString());
    }

    @Test
    public void gtWorks3() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        GreaterThan gt = new GreaterThan();
        assertEquals("true", gt.applyFn(tokens).toString());
    }

    @Test
    public void eqWorks1() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "3", "3"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        Equals eq = new Equals();
        assertEquals("true", eq.applyFn(tokens).toString());
    }

    @Test
    public void eqWorks2() throws ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "3", "4"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        Equals eq = new Equals();
        assertEquals("false", eq.applyFn(tokens).toString());
    }

    @Test
    public void andWorks1() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "true", "true", "3"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("3", and.applyFn(tokens).toString());
    }

    @Test
    public void andWorks2() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "false", "nil", "false"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", and.applyFn(tokens).toString());
    }

    @Test
    public void orWorks1() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "false", "nil", "true"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", or.applyFn(tokens).toString());
    }

    @Test
    public void orWorks2() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "false", "nil", "false"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", or.applyFn(tokens).toString());
    }

    @Test
    public void notWorks1() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "false"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", not.applyFn(tokens).toString());
    }

    @Test
    public void notWorks2() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "true"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", not.applyFn(tokens).toString());
    }

    @Test
    public void notWorks3() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "nil"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("true", not.applyFn(tokens).toString());
    }

    @Test
    public void notWorks4() throws SyntaxException, ArityException {
        TokensList tokens = new TokensList();
        String[] args = {"aFunction", "-200"};
        for (String item: args) {
            tokens.add(new Token(item));
        }
        assertEquals("false", not.applyFn(tokens).toString());
    }

    @Test
    public void consingOntoANonListThrowsException() {
        String[] args = {"cons", "3", "true"};
        TokensList tokens = new TokensList(args);
        assertThrows(ClassCastException.class, () -> {
            cons.applyFn(tokens);
        });
    }

    @Test
    public void consingWithThreeArgsThrowsException() {
        String[] args = {"cons", "3", "4", "(list 5 6)"};
        TokensList tokens = new TokensList(args);
        assertThrows(ArityException.class, () -> {
            cons.applyFn(tokens);
        });
    }

    @Test
    public void callingFirstWithANonListThrowsException() {
        String[] args = {"first", "true"};
        TokensList tokens = new TokensList(args);
        assertThrows(ClassCastException.class, () -> {
            first.applyFn(tokens);
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
            first.applyFn(parentTokens);
        });
    }

    @Test
    public void callingRestWithANonListThrowsException() {
        String[] args = {"rest", "true"};
        TokensList tokens = new TokensList(args);
        assertThrows(ClassCastException.class, () -> {
            rest.applyFn(tokens);
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
            rest.applyFn(parentTokens);
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
            mod.applyFn(tokens);
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
            not.applyFn(tokens);
        });
    }

    @Test
    public void emptyWorks1() throws ArityException, SyntaxException {
        String[] args1 = {"rest"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("true", empty.applyFn(parentTokens).toString());
    }

    @Test
    public void emptyWorks2() throws ArityException, SyntaxException {
        String[] args1 = {"rest"};
        TokensList parentTokens = new TokensList(args1);
        String[] args2 = {"list", "1"};
        TokensList childTokens = new TokensList(args2);
        parentTokens.add(childTokens);
        assertEquals("false", empty.applyFn(parentTokens).toString());
    }

}
