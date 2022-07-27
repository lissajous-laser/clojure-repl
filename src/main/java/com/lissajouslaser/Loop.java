package com.lissajouslaser;

import java.util.Scanner;

/**
 * Mediates repl loop, reads user input and prints results
 * to output.
 */
public class Loop {
    private Evaluate evaluate;
    private Scanner scanner;

    public Loop() {
        evaluate = new Evaluate();
        scanner = new Scanner(System.in);
    }

    public Loop(Evaluate evaluate, Scanner scanner) {
        this.evaluate = evaluate;
        this.scanner = scanner;
    }

    /**
     * Starts repl.
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
                    | NumberFormatException | ArityException e) {
                System.out.println(e);
            }
        }
        System.out.println("Bye for now!");
    }

}
