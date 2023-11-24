package client;

import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.LoginRegisterResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChessClient {
    private static String authToken;
    private final ServerFacade serverFacade;
    private ClientState state;
    private static ChessClient instance;
    private static HashMap<Integer, Integer> gameIDs;

    public static ChessClient getInstance() {
        if (instance == null) {
            instance = new ChessClient();
        }
        return instance;
    }

    private ChessClient() {
        serverFacade = new ServerFacade();
        state = ClientState.LOGGED_OUT;
        authToken = null;
        gameIDs = new HashMap<>();
    }

    public static String getAuthToken() {
        return authToken;
    }

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public String executeCommand(String command, ArrayList<String> params) throws IOException {
        return switch (command) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "create" -> createGame(params);
            case "logout" -> logout();
            case "list" -> list();
            default -> throw new IllegalStateException("Unexpected value: " + command);
        };
    }

    public String register(ArrayList<String> params) throws IOException {
        String username = params.get(0);
        String password = params.get(1);
        String email = params.get(2);
        RegisterRequest request = new RegisterRequest(username, password, email);
        LoginRegisterResult result = serverFacade.register(request);
        authToken = result.getAuthToken();
        setState(ClientState.LOGGED_IN);
        return String.format("You have logged in as %s.", result.getUsername());
    }

    public String login(ArrayList<String> params) throws IOException {
        String username = params.get(0);
        String password = params.get(1);
        LoginRequest request = new LoginRequest(username, password);
        LoginRegisterResult result = serverFacade.login(request);
        authToken = result.getAuthToken();
        setState(ClientState.LOGGED_IN);
        return String.format("You have logged in as %s.", result.getUsername());
    }

    public String createGame(ArrayList<String> params) throws IOException {
        String gameName = params.get(0);
        CreateGameRequest request = new CreateGameRequest(gameName);
        CreateGameResult result = serverFacade.createGame(request, authToken);
        return String.format("Created game with name %s and ID %d.", gameName, result.getGameID());
    }

    public String logout() throws IOException {
        serverFacade.logout(authToken);
        setState(ClientState.LOGGED_OUT);
        authToken = null;
        return "You have logged out.";
    }

    public String list() throws IOException {
        serverFacade.listGames(authToken);
        return null;
    }

    public enum ClientState {
        LOGGED_OUT,
        LOGGED_IN,
        QUIT
    }

}
