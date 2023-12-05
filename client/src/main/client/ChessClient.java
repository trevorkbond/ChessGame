package client;

import chess.ChessBoardImpl;
import chess.ChessGame;
import chess.ChessGameImpl;
import models.Game;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.ListGamesResult;
import result.LoginRegisterResult;
import ui.GameUI;
import ui.PostLoginRepl;
import ui.PreloginRepl;
import webSocketMessages.userCommands.JoinPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

@WebSocket
public class ChessClient {
    private static String authToken;
    private static ChessClient instance;
    private static HashMap<Integer, Integer> userIDToDatabaseID;
    private static TreeMap<Integer, Game> userIDToGame;
    private static String clientUsername;
    private static ChessGame.TeamColor teamColor;
    private final ServerFacade serverFacade;
    private WebSocketFacade webSocketFacade;
    private ClientState state;
    private ChessGameImpl clientGame;

    public void setClientGame(ChessGameImpl clientGame) {
        this.clientGame = clientGame;
    }

    private ChessClient() throws Exception {
        serverFacade = new ServerFacade();
        state = ClientState.LOGGED_OUT;
        authToken = null;
        userIDToDatabaseID = new HashMap<>();
        userIDToGame = new TreeMap<>();
    }

    public static void main(String[] args) throws Exception {
        PreloginRepl prelogin = new PreloginRepl();
        PostLoginRepl postlogin = new PostLoginRepl();
        ChessClient client = null;
        try {
            client = ChessClient.getInstance();
        } catch (Exception e) {
            System.out.println("Some wack websocket thing happened\n" + e.getStackTrace());
        }
        GameUI inGame = new GameUI();
        while (!client.getState().equals(ChessClient.ClientState.QUIT)) {
            if (client.getState().equals(ChessClient.ClientState.LOGGED_OUT)) {
                prelogin.run(ChessClient.ClientState.QUIT, "Welcome to ChessGame. Please sign in or register to play.\n");
            } else if (client.getState().equals(ChessClient.ClientState.LOGGED_IN)) {
                postlogin.run(ChessClient.ClientState.LOGGED_OUT, "Please select from the following commands.\n");
            } else if (client.getState().equals(ChessClient.ClientState.IN_GAME)) {
                if (client.clientGame != null) {
                    inGame.run(teamColor, client.clientGame.getBoard());
                }
            }
        }
        System.out.println("Goodbye!");
    }

    public static ChessClient getInstance() throws Exception {
        if (instance == null) {
            instance = new ChessClient();
        }
        return instance;
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

    public String executeCommand(String command, ArrayList<String> params) throws Exception {
        return switch (command) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "create" -> createGame(params);
            case "logout" -> logout();
            case "list" -> list();
            case "join" -> join(params);
            case "clear" -> clear();
            case "observe" -> observe(params);
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
        clientUsername = username;
        return String.format("You have logged in as %s.", result.getUsername());
    }

    public String login(ArrayList<String> params) throws IOException {
        String username = params.get(0);
        String password = params.get(1);
        LoginRequest request = new LoginRequest(username, password);
        LoginRegisterResult result = serverFacade.login(request);
        authToken = result.getAuthToken();
        setState(ClientState.LOGGED_IN);
        clientUsername = username;
        return String.format("You have logged in as %s.", result.getUsername());
    }

    public String createGame(ArrayList<String> params) throws IOException {
        String gameName = params.get(0);
        CreateGameRequest request = new CreateGameRequest(gameName);
        serverFacade.createGame(request, authToken);
        return String.format("Created game with name %s.", gameName);
    }

    public String logout() throws IOException {
        serverFacade.logout(authToken);
        setState(ClientState.LOGGED_OUT);
        authToken = null;
        return "You have logged out.";
    }

    public String list() throws IOException {
        ListGamesResult result = serverFacade.listGames(authToken);
        HashSet<Game> games = result.getGames();
        updateGameIDs(games);
        return gameListToString();
    }

    public String join(ArrayList<String> params) throws Exception {
        int gameID;
        try {
            gameID = Integer.parseInt(params.get(0));
        } catch (NumberFormatException e) {
            throw new InvalidResponseException("Bad request. Please try again.");
        }
        Integer databaseID = userIDToDatabaseID.get(gameID);
        if (databaseID == null) {
            throw new InvalidResponseException("Error joining game. Try listing games again or check given game ID.");
        }
        if (!params.get(1).equals("WHITE") && !params.get(1).equals("BLACK")) {
            throw new InvalidResponseException("Bad request. Please try again");
        }
        ChessGame.TeamColor team = ChessGame.TeamColor.valueOf(params.get(1));
        teamColor = team;

        JoinGameRequest request = new JoinGameRequest(team, databaseID);
        JoinPlayer webSocketMessage = new JoinPlayer(authToken, databaseID, team, clientUsername);
        serverFacade.joinGame(request, authToken);

        webSocketFacade = new WebSocketFacade(this);
        webSocketFacade.joinPlayer(webSocketMessage);

        setState(ClientState.IN_GAME);
        return String.format("You have joined game with ID %d", gameID);
    }

    public String observe(ArrayList<String> params) throws IOException, InvalidResponseException {
        int gameID = Integer.parseInt(params.get(0));
        Integer databaseID = userIDToDatabaseID.get(gameID);
        if (databaseID == null) {
            throw new InvalidResponseException("Error observing game. Try listing games again or check given game ID.");
        }

        JoinGameRequest request = new JoinGameRequest(null, databaseID);
        serverFacade.joinGame(request, authToken);
        setState(ClientState.IN_GAME);
        return String.format("You are observing game with ID %d", gameID);
    }

    public String clear() throws IOException {
        serverFacade.clear();
        setState(ClientState.LOGGED_OUT);
        return "All databases have been cleared";
    }

    private String gameListToString() {
        StringBuilder listString = new StringBuilder();
        for (Integer userFacingID : userIDToGame.keySet()) {
            listString.append(userFacingID).append(": ");
            listString.append(userIDToGame.get(userFacingID).toString());
        }
        return listString.toString();
    }

    private void updateGameIDs(HashSet<Game> games) {
        userIDToDatabaseID.clear();
        userIDToGame.clear();
        int userFacingID = 1;
        for (Game game : games) {
            userIDToDatabaseID.put(userFacingID, game.getGameID());
            userIDToGame.put(userFacingID, game);
            userFacingID++;
        }
    }

    public enum ClientState {
        LOGGED_OUT,
        LOGGED_IN,
        IN_GAME,
        QUIT
    }

}
