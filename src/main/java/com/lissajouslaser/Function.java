package com.lissajouslaser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for user defined functions.
 */
public class Function extends Evaluate {
    private String name;
    private List<String> params;
    private String body;

    /**
     * Class constructor.
     */
    public Function(String name, ArrayList<String> params, String body) {
        super();
        this.name = name;
        this.params = params;
        this.body = body;
    }

    /**
     * Returns function expression with all the parameters
     * substituted for arguments.
     */
    public String applyFn(ArrayList<String> args, Evaluate global)
            throws SyntaxException {
        if (args.size() != params.size()) {
            throw new SyntaxException("user/" + name);
        }
        setUserDefinedFunctions(global.getUserDefinedFunctions());

        Map<String, String> globalDefinedValues = global.getDefinedValues();
        Map<String, String> definedValuesInScope = this.getDefinedValues();

        // Deep copy the global definedValues.
        for (String sym: globalDefinedValues.keySet()) {
            definedValuesInScope.put(sym, globalDefinedValues.get(sym));
        }
        // Add parameter-argument bindings.
        for (int i = 0; i < params.size(); i++) {
            definedValuesInScope.put(params.get(i), args.get(i));
        }
        return  eval(body);
    }
}
