package com.lissajouslaser;

/**
 * Defines a function.
 */
public interface Function extends TokensListOrToken {

    /**
     * Functions with normal evalution rules will have all of
     * their arguments evaluated. Some functions like if, def
     * have selective evaluation of their arguements, because
     * some expressions or symbols have to be left in
     * unevaluated form.
     * A Function with isEvaluationNormal() returning false will
     * not have any of their arguements evaluated by default,
     * and will need to to have evaluation implemented in
     * applyFn().
     */
    boolean isEvalutionNormal();

    Token getName();

    TokensListOrToken applyFn(TokensList tokens)
            throws ArityException, SyntaxException;
}
