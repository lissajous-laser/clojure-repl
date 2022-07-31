package com.lissajouslaser;

import java.util.Map;

/**
 * Class that represents a user defined function.
 */
public class UserDefinedFunction extends Evaluate
        implements Function {
    private Token name;
    private TokensList params;
    private Token body;

    /**
     * Class constructor.
     */
    public UserDefinedFunction(Token name, TokensList params, Token body) {
        super();
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public void setLocalNamespace(
            Map<Token, TokensListOrToken> globalDefinedValues,
            Map<Token, Function> globalUserDefinedFunctions
    ) {
        getDefinedValues().putAll(globalDefinedValues);
        setdefinedFunctions(globalUserDefinedFunctions);
    }

    public Token getName() {
        return name;
    }

    /**
     * Adds parameter-argument bindings to local scope and then
     * performs application of the function.
     */
    public TokensListOrToken applyFn(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {

        // Make sure number of args and params are equal.
        if (tokens.size() - 1 != params.size()) {
            throw new ArityException("user/" + name);
        }
        // Add parameter-argument bindings.
        for (int i = 0; i < params.size(); i++) {
            Token param = (Token) params.get(i);
            TokensListOrToken arg = tokens.get(i + 1);
            getDefinedValues().put(param, arg);
        }
        return eval(body);
    }
}
