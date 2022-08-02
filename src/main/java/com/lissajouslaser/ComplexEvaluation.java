package com.lissajouslaser;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains methods for the evalution of nested expressions.
 * Used by the Evaluate class, and by Function classes that
 * have special evaluation rules.
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
    public TokensListOrToken eval(Token token)
            throws SyntaxException, ArithmeticException,
            NumberFormatException, ArityException, ClassCastException {

        String expr = token.toString();

        if (CheckType.isList(expr)) {
            TokensList tokens = Tokeniser.run(expr);
            Function function = (Function) eval((Token) tokens.get(0));

            if (function.isDefinitionCreator()) {
                // For functions that add defintiions, eg. def, defn.
                ((ComplexEvaluation) function).setDefinitions(getDefinitions());
            } else if (function instanceof ComplexEvaluation) {
                // Other functions that need access to definitions
                // (eg. if) get a copy.
                ((ComplexEvaluation) function).copyDefinitions(getDefinitions());
            }
            if (!function.isEvaluationNormal()) {
                return function.applyFn(tokens);
            }
            if (tokens.isEmpty()) {
                throw new SyntaxException("Unsupported input: " + expr);
            }
            // Evaluate any expressions in arguments.
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
}
