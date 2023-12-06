package server;

import chess.ChessBoardImpl;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessGameImpl;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.ChessBoardAdapter;
import models.ChessMoveAdapter;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverNotifications.LoadGame;
import webSocketMessages.serverNotifications.ServerError;
import webSocketMessages.serverNotifications.ServerNotification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler extends Endpoint {
    private static WebSocketSessions webSocketSessions;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;

    public WebSocketHandler() throws DataAccessException {
        webSocketSessions = WebSocketSessions.getInstance();
        Connection connection = Server.database.getConnection();
        gameDAO = new GameDAO(connection);
        authDAO = new AuthDAO(connection);
        userDAO = new UserDAO(connection);
    }

    public static Object jsonToObject(Class desiredClass, String message) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(ChessMove.class, new ChessMoveAdapter());

        Gson gson = builder.create();
        return gson.fromJson(message, desiredClass);
    }

    @OnWebSocketClose
    public void onClose(Session session, int errorCode, String msg) {
        webSocketSessions.removeSession(session);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
        UserGameCommand command = getCommand(message);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, (JoinPlayer) command);
            case MAKE_MOVE -> makeMove(session, (MakeMove) command);
            case JOIN_OBSERVER -> joinObserver(session, (JoinObserver) command);
        }
    }

    private void joinPlayer(Session session, JoinPlayer command) throws IOException, DataAccessException {
        try {
            checkAuth(command.getGameID(), command.getAuthString());
            checkTeamNotTaken(command.getGameID(), command.getPlayerColor(), command.getAuthString());

            int gameID = command.getGameID();
            String authString = command.getAuthString();
            String broadcastString = command.getUsername() + " joined the game as a player on team " + command.getPlayerColor();

            joinHelper(session, gameID, authString, broadcastString);
        } catch (DataAccessException e) {
            webSocketSessions.addSessionToGame(command.getGameID(), command.getAuthString(), session);
            sendErrorMessage(command.getGameID(), command.getAuthString(), e.getMessage());
        }
    }

    private void joinObserver(Session session, JoinObserver command) throws IOException {
        try {
            checkAuth(command.getGameID(), command.getAuthString());

            int gameID = command.getGameID();
            String authString = command.getAuthString();
            String broadcastString = command.getUsername() + " joined the game as an observer";

            joinHelper(session, gameID, authString, broadcastString);
        } catch (DataAccessException e) {
            webSocketSessions.addSessionToGame(command.getGameID(), command.getAuthString(), session);
            sendErrorMessage(command.getGameID(), command.getAuthString(), e.getMessage());
        }
    }

    private void joinHelper(Session session, int gameID, String authString, String broadcastMessage) throws IOException, DataAccessException {
        webSocketSessions.addSessionToGame(gameID, authString, session);
        ServerNotification broadcast = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, broadcastMessage);
        broadcastMessage(gameID, objectToJson(broadcast), authString);

        ChessGameImpl game = gameDAO.findGame(gameID).getGame();
        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
        sendMessage(gameID, objectToJson(loadGame), authString);
    }

    private void checkAuth(int gameID, String token) throws DataAccessException {
        authDAO.findToken(new AuthToken(null, token));
    }

    private void checkTeamNotTaken(int gameID, ChessGame.TeamColor teamColor, String token) throws DataAccessException {
        Game foundGame = gameDAO.findGame(gameID);
        if (teamColor.equals(ChessGame.TeamColor.BLACK) && foundGame.getBlackUsername() != null) {
            AuthToken foundToken = authDAO.findToken(new AuthToken(null, token));
            if (!foundToken.getUsername().equals(foundGame.getBlackUsername())) {
                System.out.println("Black team taken");
                throw new DataAccessException("Error: team already taken");
            }
        } else if (teamColor.equals(ChessGame.TeamColor.WHITE) && foundGame.getWhiteUsername() != null) {
            AuthToken foundToken = authDAO.findToken(new AuthToken(null, token));
            if (!foundToken.getUsername().equals(foundGame.getWhiteUsername())) {
                System.out.println("White team taken");
                throw new DataAccessException("Error: team already taken");
            }
        }
    }

    private void sendErrorMessage(int gameID, String token, String message) throws IOException {
        ServerError error = new ServerError(ServerMessage.ServerMessageType.ERROR, message);
        sendMessage(gameID, objectToJson(error), token);
    }


    private String objectToJson(Object o) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoardImpl.class, new ChessBoardAdapter());
        Gson gson = builder.create();
        return gson.toJson(o);
    }

    private void makeMove(Session session, MakeMove command) throws DataAccessException, IOException {
        try {
            ChessGameImpl updatedGame = gameDAO.updateGame(command.getGameID(), command.getChessMove());

            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame);
            sendMessage(command.getGameID(), objectToJson(loadGame), command.getAuthString());
            broadcastMessage(command.getGameID(), objectToJson(loadGame), command.getAuthString());

            ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, command.getUsername() + " made a move");
            broadcastMessage(command.getGameID(), objectToJson(notification), command.getAuthString());
        }
        catch (InvalidMoveException e) {
            sendErrorMessage(command.getGameID(), command.getAuthString(), e.getMessage());
        }
    }

    private UserGameCommand getCommand(String message) {
        System.out.println("in Handler usercommand: " + message);
        UserGameCommand command = (UserGameCommand) jsonToObject(UserGameCommand.class, message);
        Class desiredClass = getDesiredClass(command);
        return (UserGameCommand) jsonToObject(desiredClass, message);
    }

    private Class getDesiredClass(UserGameCommand command) {
        return switch (command.getCommandType()) {
            case JOIN_PLAYER -> JoinPlayer.class;
            case JOIN_OBSERVER -> JoinObserver.class;
            case MAKE_MOVE -> MakeMove.class;
            case LEAVE -> null;
            case RESIGN -> null;
        };
    }

    private void sendMessage(int gameID, String message, String user) throws IOException {
        webSocketSessions.getSessionsForGame(gameID).get(user).getRemote().sendString(message);
    }

    private void broadcastMessage(int gameID, String message, String exceptThisUser) throws IOException {
        HashMap<String, Session> sessions = webSocketSessions.getSessionsForGame(gameID);
        for (String user : sessions.keySet()) {
            if (!user.equals(exceptThisUser)) {
                sessions.get(user).getRemote().sendString(message);
            }
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
    }
}
