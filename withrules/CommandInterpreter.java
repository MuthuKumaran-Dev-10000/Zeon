package withrules;

import java.util.Scanner;

public class CommandInterpreter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Murga++ Command Interpreter");
        System.out.println("Enter a command (type 'exit' to quit):");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                break;
            }

            interpretCommand(command);
        }

        System.out.println("Exiting let++ Command Interpreter");
        scanner.close();
    }

    /**
     * Interprets and executes commands entered by the user.
     *
     * @param command The command to be interpreted.
     */
    private static void interpretCommand(String command) {

        String[] tokens = command.split(" ");

        if (tokens.length == 0) {
            System.out.println("Invalid command");
            return;
        }

        String keyword = tokens[0];
        switch (keyword) {
            case "print":
                if (tokens.length < 2) {
                    System.out.println("Usage: print(\"message\")");
                    return;
                }
                String message = tokens[1];
                System.out.println("Printing: " + message);
                break;
            case "let":

                break;
            default:
                System.out.println("Unknown command: " + keyword);
        }
    }
}