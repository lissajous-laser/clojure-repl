package com.lissajouslaser;

import java.io.CharArrayReader;
import java.io.IOException;

/**
 * Breaks an expression up into consituent Tokens inside
 * a TokensList.
 */
public final class Tokeniser {

    private Tokeniser() {}

    /**
     * If input is a list it will take apart and
     * function and arguments, put each element
     * in a Token, and put the Tokens in a
     * TokensList. Any nested expressions are
     * treated as a single element and put in a
     * Token, where they can be later tokenised
     * through recursion.
     */
    public static TokensList run(String expr) throws SyntaxException {
        TokensList tokens = new TokensList();

        char[] exprAsChars = expr.trim().toCharArray();
        int i;

        CharArrayReader charArrReader = new CharArrayReader(exprAsChars);
        // In expression we expect to see at least an opening
        // and closing parens.
        int openParens = 0;
        int closeParens = 0;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((i = charArrReader.read()) != -1) {

                // Incorrect syntax.
                if (closeParens > openParens) {
                    throw new SyntaxException("Unmatched parentheses.");
                }
                switch (i) {
                    // case '[' and ']' allows Clojure vectors to be converted
                    // to lists. Only use is for allowing Clojure function
                    // parameters to be inputted as a Clojure vector literals.
                    case '[':
                    case '(':
                        openParens++;
                        if (openParens - closeParens == 2
                                && stringBuilder.length() != 0) {
                            // stringBuilder.append("•<•"); ///
                            tokens.add(new Token(stringBuilder.substring(0)));
                            stringBuilder.delete(0, stringBuilder.length());
                        }
                        if (openParens != 1) {
                            stringBuilder.append('(');
                        }
                        break;
                    case ']':
                    case ')':
                        closeParens++;
                        if (openParens - closeParens != 0) {
                            stringBuilder.append(')');
                        }
                        if (openParens - closeParens < 2
                                && stringBuilder.length() != 0) {
                            // stringBuilder.append("•>•"); ///
                            tokens.add(new Token(stringBuilder.substring(0)));
                            stringBuilder.delete(0, stringBuilder.length());
                        }
                        break;
                    case ' ':
                        // We want to have the whole nested expression
                        // as a single token. We do not care about
                        // separating out doubly nested expression or
                        // deeper because they will be dealt with on
                        // recursion.
                        if (openParens - closeParens > 1) {
                            stringBuilder.append((char) i);
                        } else if (stringBuilder.length() != 0) {
                            // stringBuilder.append("•∆•"); ///
                            tokens.add(new Token(stringBuilder.substring(0)));
                            stringBuilder.delete(0, stringBuilder.length());
                        }
                        break;
                    default:
                        stringBuilder.append((char) i);
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
