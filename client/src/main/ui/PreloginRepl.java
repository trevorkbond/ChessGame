package ui;

import client.ChessClient;
import client.InvalidResponseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class PreloginRepl extends Repl {

    private HashMap<String, Integer> validLengths;
    private ChessClient client;

    public PreloginRepl() {
        validLengths = new HashMap<>();
        client = ChessClient.getInstance();
        validLengths.put("register", 4);
        validLengths.put("login", 3);
        validLengths.put("quit", 1);
        validLengths.put("help", 1);
    }

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.println("Welcome to ChessGame. Please sign in or register to play.\n");
        setInfoPrinting();
        System.out.println(help());
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            setInputPrinting();
            String selection = scanner.nextLine();
            try {
                result = eval(selection);
                if (result.equals("help")) {
                    setInfoPrinting();
                    System.out.println(help());
                } else if (!result.equals("quit")) {
                    setInfoPrinting();
                    System.out.println(result);
                    if (client.getState().equals(ChessClient.ClientState.LOGGED_IN)) {
                        break;
                    }
                }
            }
            catch (InvalidResponseException e) {
                setInfoPrinting();
                System.out.println(e.getMessage());
                System.out.println(help());
            }
        }

    }

    private String eval(String selection) throws InvalidResponseException {
        String[] args = selection.split(" ");
        ArrayList<String> params = new ArrayList<>(Arrays.asList(args));
        String command = args[0];
        if (validLengths.get(command) == null || args.length != validLengths.get(command)) {
            throw new InvalidResponseException("Bad request. Please try again.");
        } else if (command.equals("help")) {
            return "help";
        } else if (command.equals("quit")) {
            client.setState(ChessClient.ClientState.QUIT);
            return "quit";
        }

        ArrayList<String> subParams = new ArrayList<>(params.subList(1, params.size()));
        try {
            return client.executeCommand(command, subParams);
        } catch (IOException e) {
            return handleIOException(e);
        }
    }

    private String handleIOException(IOException e) {
        String errorMessage = e.getMessage();
        return switch (errorMessage) {
            case "400" -> "Bad request. Please try again";
            case "401" -> "Login credentials are incorrect. Please try again";
            case "403" -> "That username is already taken. Please try again.";
            case "500" -> "There was a server error. Please try again. (Is it running?)";
            case "Connection refused" -> "The server isn't running. Please run it and try again.";
            default -> "There was an error somewhere. Please try again.";
        };
    }

    public String help() {
        return
        """
        register <USERNAME> <PASSWORD> <EMAIL> - to register as a user
        login <USERNAME> PASSWORD> - to login and play chess
        quit - to quit the program
        help - to display command options again
        """;
    }

    private void setInfoPrinting() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }

    private void resetTextPrinting() {
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private void setInputPrinting() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "[" + client.getState() + "]>>> ");
    }
}
