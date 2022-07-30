package com.lissajouslaser;

import java.io.CharArrayReader;
import java.io.IOException;

/**
 * Breaks an expression up into consituent Tokens inside
 * a TokensList.
 */
public final class Tokeniser {

    public Tokeniser() {}

    /**
     * If input is a list it will take apart and
     * function and arguments and put into an array.
     */
    static TokensList tokenise(String expr) throws SyntaxException {
        TokensList tokens = new TokensList();

        char[] exprAsChars = expr.trim().toCharArray();
        int i;

        CharArrayReader charArrReader = new CharArrayReader(exprAsChars);
        // In expression we expect to see at least an opening
        // and closing parens.
        int openParens = 0;
        int closeParens = 0;
        // Checks if reader position is currently inside a
        // nested expression.
        boolean nestedExpr = false;
        StringBuilder tokenBuilder = new StringBuilder();

        try {
            while ((i = charArrReader.read()) != -1) {

                // Incorrect syntax.
                if (closeParens > openParens) {
                    throw new SyntaxException("Unmatched parentheses.");
                }

                switch (i) {
                    case '(':
                        openParens++;
                        // No action on the parens that wraps the
                        // top level expression.
                        if (openParens == 1) {
                            continue;
                            // Include parens in doubly or deeper nested
                            // expressions.
                        } else {
                            nestedExpr = true;
                            tokenBuilder.append((char) i);
                        }
                        break;
                    case ')':
                        closeParens++;
                        if (nestedExpr) {
                            tokenBuilder.append((char) i);
                            // Does not add empty strings to tokens.
                        } else if (tokenBuilder.length() == 0) {
                            continue;
                        } else {
                            tokens.add(new Token(tokenBuilder.substring(0)));
                            tokenBuilder.delete(0, tokenBuilder.length());
                        }
                        if (openParens - closeParens == 1) {
                            nestedExpr = false;
                        }
                        break;
                    case ' ':
                        // We want to have the whole nested expression
                        // as a single token. We do not care about
                        // separating out doubly nested expression or
                        // deeper because they will be dealt with on
                        // recursion.
                        if (nestedExpr) {
                            tokenBuilder.append((char) i);
                            // Does not add empty strings to tokens.
                            // For the case if input text has souble
                            // spaces.
                        } else if (tokenBuilder.length() == 0) {
                            continue;
                        } else {
                            tokens.add(new Token(tokenBuilder.substring(0)));
                            tokenBuilder.delete(0, tokenBuilder.length());
                        }
                        break;
                    default:
                        tokenBuilder.append((char) i);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        // Check parens are balanced.
        if (openParens == closeParens) {
            return tokens;
        } else {
            throw new SyntaxException("Unmatched parentheses.");
        }
    }
}
