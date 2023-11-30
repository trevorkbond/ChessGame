package ui;

import client.ChessClient;
import client.InvalidResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Repl {

    protected final HashMap<String, Integer> validLengths;

    protected ChessClient client;

    public Repl() throws Exception {
        client = ChessClient.getInstance();
        validLengths = new HashMap<>();
    }

    public static String objectToJson(Object object) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(object);
    }

    public void run(ChessClient.ClientState quitState, String welcome) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.println(welcome);
        setInfoPrinting();
        System.out.println(help());
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            setInputPrinting();
            String selection = scanner.nextLine();
            try {
                result = eval(selection, quitState);
                if (result.equals("help")) {
                    setInfoPrinting();
                    System.out.println(help());
                } else if (!result.equals("quit")) {
                    setInfoPrinting();
                    System.out.println(result);
                    if (quitState.equals(ChessClient.ClientState.QUIT) && client.getState().equals(ChessClient.ClientState.LOGGED_IN)) {
                        break;
                    } else if (quitState.equals(ChessClient.ClientState.LOGGED_OUT) && client.getState().equals(ChessClient.ClientState.LOGGED_OUT)) {
                        break;
                    } else if (quitState.equals(ChessClient.ClientState.LOGGED_OUT) && client.getState().equals(ChessClient.ClientState.IN_GAME)) {
                        resetTextPrint();
                        break;
                    }
                }
            } catch (InvalidResponseException e) {
                setInfoPrinting();
                System.out.println(e.getMessage());
                System.out.println(help());
            }
        }

    }

    private String eval(String selection, ChessClient.ClientState quitState) throws InvalidResponseException {
        String[] args = selection.split(" ");
        ArrayList<String> params = new ArrayList<>(Arrays.asList(args));
        String command = args[0];
        String helpQuitOrNull = evalHelper(command, quitState, args);
        if (helpQuitOrNull != null) {
            return helpQuitOrNull;
        } else {
            ArrayList<String> subParams = new ArrayList<>(params.subList(1, params.size()));
            try {
                return client.executeCommand(command, subParams);
            } catch (IOException e) {
                return handleIOException(e);
            }
        }
    }

    private String evalHelper(String command, ChessClient.ClientState quitState, String[] args) throws InvalidResponseException {
        if (validLengths.get(command) == null || args.length != validLengths.get(command)) {
            throw new InvalidResponseException("Bad request. Please try again.");
        } else if (command.equals("help")) {
            return "help";
        } else if (command.equals("quit")) {
            client.setState(quitState);
            return "quit";
        }
        return null;
    }

    private String handleIOException(IOException e) {
        String errorMessage = e.getMessage();
        return switch (errorMessage) {
            case "400" -> "Bad request. Please try again";
            case "401" -> "Unauthorized action. Please try again";
            case "403" -> "Given request paramenter already taken. Please try again.";
            case "500" -> "There was a server error. Please try again. (Is it running?)";
            case "Connection refused" -> "The server isn't running. Please run it and try again.";
            default -> "There was an error somewhere. Please try again.";
        };
    }

    private String help() {
        if (client.getState().equals(ChessClient.ClientState.LOGGED_OUT)) {
            return
                    """
                            register <USERNAME> <PASSWORD> <EMAIL> - to register as a user
                            login <USERNAME> PASSWORD> - to login and play chess
                            quit - to quit the program
                            help - to display command options again
                            """;
        } else if (client.getState().equals(ChessClient.ClientState.LOGGED_IN)) {
            return
                    """
                            create <NAME> - create a game
                            list - get list of all games
                            join <ID> <BLACK|WHITE> - join a game
                            observe <ID> - observe a game
                            logout - logout and go to prelogin
                            quit - quit chess game (only valid after joining/observing game)
                            help - list possible commands
                            """;
        } else {
            return null;
        }
    }

    public void setInfoPrinting() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }

    public void resetBackground() {
        System.out.print("\u001b[0m");
    }

    public void setBackground(String background) {
        System.out.print(background);
    }

    public void resetTextPrint() {
        System.out.print("\u001b[0m");
    }

    public void setInputPrinting() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "[" + client.getState() + "]>>> ");
    }

}
