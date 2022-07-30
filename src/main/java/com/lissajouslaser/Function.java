package com.lissajouslaser;

import java.util.Map;

/**
 * Class that represents a user defined function.
 */
public class Function extends Evaluate {
    private String name;
    private TokensList params;
    private String body;

    /**
     * Class constructor.
     */
    public Function(String name, TokensList params, String body) {
        super();
        this.name = name;
        this.params = params;
        this.body = body;
    }

    /**
     * Adds parameter-argument bindings to local scope and then
     * performs application of the function.
     */
    public TokensListOrToken applyFn(
            TokensList tokens,
            Map<Token, TokensListOrToken> globalDefinedValues,
            Map<Token, Function> globalUserDefinedFunctions
    ) throws SyntaxException, ArityException, ClassCastException {

        // Make sure number of args and params are equal.
        if (tokens.size() - 1 != params.size()) {
            throw new ArityException("user/" + name);
        }
        setUserDefinedFunctions(globalUserDefinedFunctions);
        getDefinedValues().putAll(globalDefinedValues);

        // Add parameter-argument bindings.
        for (int i = 0; i < params.size(); i++) {
            Token param = (Token) params.get(i);
            TokensListOrToken arg = tokens.get(i + 1);
            getDefinedValues().put(param, arg);
        }
        return eval(body);
    }
}
