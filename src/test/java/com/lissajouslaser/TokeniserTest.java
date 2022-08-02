package com.lissajouslaser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Tokeniser class.
 */
public class TokeniserTest {
    @Test
    public void tokeniserWorks1() throws SyntaxException {
        TokensList tokens = new TokensList();
        tokens.add(new Token("+"));
        tokens.add(new Token("2"));
        tokens.add(new Token("3"));
        assertTrue(Tokeniser.run("(+ 2 3)").equals(tokens));
    }

    @Test
    public void tokeniserWorksWithSquareBrackets() throws SyntaxException {
        String[] fnStart = {"defn", "add"};
        TokensList parentTokens = new TokensList(fnStart);
        String[] params = {"x", "y"};
        TokensList paramsTokens = new TokensList(params);
        String[] body = {"+", "x", "y"};
        TokensList bodyTokens = new TokensList(body);
        parentTokens.add(paramsTokens);
        parentTokens.add(bodyTokens);
        assertEquals(
                parentTokens.toString(),
                Tokeniser.run("(defn add [x y] (+ x y))").toString()
        );
    }

    @Test
    public void tokeniseThrowsExceptionWhenGivenSyntaxError() {
        assertThrows(SyntaxException.class,
                () -> {
                    Tokeniser.run("))");
                });
    }

}

