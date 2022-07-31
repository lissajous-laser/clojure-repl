package com.lissajouslaser;

/**
 * Defines a function.
 */
public interface Function {
    TokensListOrToken applyFn(TokensList tokens)
            throws ArityException, SyntaxException;

    Token getName();
}
