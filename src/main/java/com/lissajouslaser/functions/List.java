package com.lissajouslaser.functions;

import com.lissajouslaser.Function;
import com.lissajouslaser.Token;
import com.lissajouslaser.TokensList;

/**
 * List function.
 */
public class List implements Function {

    public Token getName() {
        return new Token("list");
    }

    /**
     * The list function does not evaluate its
     * arguments/elements, but its arguments
     * will have been evaluated. ie. passing
     * "(list (+ 1 2) 3)" to Evaluate.eval()
     * returns (list 3 3).
     */
    public TokensList applyFn(TokensList tokens) {
        return tokens;
    }

}
