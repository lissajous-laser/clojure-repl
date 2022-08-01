package com.lissajouslaser;

import java.util.Scanner;

/**
 * Mediates looping of the repl, scans user input and prints
 * the output of evaluaton.
 */
public class Loop {
    private Evaluate evaluate;
    private Scanner scanner;

    public Loop(Evaluate evaluate, Scanner scanner) {
        this.evaluate = evaluate;
        this.scanner = scanner;
    }

    /**
     * Starts the repl.
     */
    public void start() {
        String input = "";

        System.out.println("Clojure REPL");
        System.out.println("Exit: (quit)\n");

        while (true) {
            System.out.print("user=> ");

            input = scanner.nextLine();
            if ("(quit)".equals(input.trim())) {
                break;
            }
            try {
                System.out.println(evaluate.eval(input));
                // Most exceptions are caught here becuase we do not
                // want evaluation to continue when there is an error.
            } catch (SyntaxException | ArithmeticException
                    | NumberFormatException | ArityException
                    | ClassCastException e) {
                System.out.println(e);
            }
        }
        System.out.println("Bye for now!");
    }
}
