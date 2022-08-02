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
    boolean isEvaluationNormal();

    /**
     * A function with isDefinitionCreator() returning true will
     * have its definitions instance variable reference the
     * definitions of Evaluate, ie. the namespace, to allow it
     * to add new definitions to the namespace.
     */
    boolean isDefinitionCreator();

    Token getName();

    TokensListOrToken applyFn(TokensList tokens)
            throws ArityException, SyntaxException;
}
