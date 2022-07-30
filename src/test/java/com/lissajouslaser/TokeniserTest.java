package com.lissajouslaser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Tokeniser class.
 */
public class TokeniserTest {
    @Test
    public void tokeniseWorks1() throws SyntaxException {
        TokensList tokens = new TokensList();
        tokens.add(new Token("+"));
        tokens.add(new Token("2"));
        tokens.add(new Token("3"));
        assertTrue(Tokeniser.tokenise("(+ 2 3)").equals(tokens));
    }

    @Test
    public void tokeniseThrowsExceptionWhenGivenSyntaxError() {
        assertThrows(SyntaxException.class,
                () -> {
                    Tokeniser.tokenise("))");
                });
    }
}

