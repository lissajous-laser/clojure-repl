package com.lissajouslaser;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains methods for the evalution of nested expressions.
 * Used by classes where there deal with nested expressions,
 * and expressions where not all the arguments are evalutated.
 */
public abstract class ComplexEvaluation {
    // Stores defined values, lists and functions.
    private Map<Token, TokensListOrToken> definitions;

    public ComplexEvaluation() {
        definitions = new HashMap<>();
    }

    /**
     * Used by classes that will update definitions.
     */
    void setDefinitions(Map<Token, TokensListOrToken> definitions) {
        this.definitions = definitions;
    }

    public Map<Token, TokensListOrToken> getDefinitions() {
        return definitions;
    }

    /**
     * Used by subclasses that require a copy of definitions
     * but do not update it.
     */
    void copyDefinitions(Map<Token, TokensListOrToken> globalDefinitions) {
        getDefinitions().putAll(globalDefinitions);
    }

    /**
     * Performs evaluation of expression, including evaluation
     * of nested expressions.
     */
    /**
     * Performs evaluation of expression, including evaluation
     * of nested expressions.
     */
    public TokensListOrToken eval(Token token)
            throws SyntaxException, ArithmeticException,
            NumberFormatException, ArityException, ClassCastException {

        String expr = token.toString();

        if (CheckType.isList(expr)) {
            TokensList tokens = Tokeniser.run(expr);
            Function function = getFunctionReady(tokens);

            if (!function.isEvalutionNormal()) {
                return function.applyFn(tokens);
            }
            if (tokens.isEmpty()) {
                throw new SyntaxException("Unsupported input: " + expr);
            }
            TokensList tokensWithEvaluatedArgs = new TokensList();
            tokensWithEvaluatedArgs.add(tokens.get(0));

            for (int i = 1; i < tokens.size(); i++) {
                Token arg = (Token) tokens.get(i);
                tokensWithEvaluatedArgs.add(eval(arg));
            }
            return function.applyFn(tokensWithEvaluatedArgs);

        } else if (CheckType.isNumber(expr) || CheckType.isBool(expr)) {
            return token;
        } else if (CheckType.isValidSymbol(expr)) {
            TokensListOrToken valueOrListOrFn = getDefinitions().get(token);
            if (valueOrListOrFn == null) {
                throw new SyntaxException("Unable to resolve symbol: " + expr
                        + " in this context.");
            }
            return valueOrListOrFn;
        } else {
            throw new SyntaxException("Invalid token: " + expr);
        }
    }

    /**
     * Tries to match a user defined function to the expression
     * to evaluate, then applies function.
     */
    Function getFunctionReady(TokensList tokens)
            throws SyntaxException, ArityException, ClassCastException {

        Function function;
        TokensListOrToken nameOfFunctionOrFunction = tokens.get(0);

        if (nameOfFunctionOrFunction instanceof Function) {
            function = (Function) nameOfFunctionOrFunction;
        } else {
            Token fnName = (Token) nameOfFunctionOrFunction;
            function = (Function) getDefinitions().get(fnName);

            if (function == null) {
                throw new SyntaxException("Unable to resolve " + fnName
                        + " in this context.");
            }
        }
        // Allow Def or Defn to change definitions.
        if (function instanceof Define) {
            ((ComplexEvaluation) function).setDefinitions(getDefinitions());
        // Other functions that need access to definitions
        // get a copy.
        } else if (function instanceof ComplexEvaluation) {
            ((ComplexEvaluation) function).copyDefinitions(getDefinitions());
        }
        return function;
    }
}
