package com.lissajouslaser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates initially defined functions for passing to
 * Evaluate(Map<Token, Function>) constructor.
 */
public final class NamespaceInitialiser {

    private NamespaceInitialiser() {}

    /**
     * Creates mapping of functions from a list of functions.
     */
    public static Map<Token, Function> run(List<Function> fnList) {
        Map<Token, Function> definedFunctions = new HashMap<>();

        for (Function fn: fnList) {
            definedFunctions.put(fn.getName(), fn);
        }
        return definedFunctions;
    }
}
