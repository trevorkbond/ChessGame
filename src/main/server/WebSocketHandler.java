package server;

import chess.ChessBoardImpl;
import chess.ChessGame;
import chess.ChessGameImpl;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.ChessBoardAdapter;
import models.Game;
import models.User;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverNotifications.LoadGame;
import webSocketMessages.serverNotifications.ServerNotificationError;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.xml.crypto.Data;
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
        }
    }

    private void joinPlayer(Session session, JoinPlayer command) throws IOException, DataAccessException {
        checkAuth(command.getGameID(), command.getAuthString());
//        checkTeamNotTaken(command.getGameID(), command.getTeamColor(), command.getAuthString());

        webSocketSessions.addSessionToGame(command.getGameID(), command.getAuthString(), session);
        ServerNotificationError broadcast = new ServerNotificationError(ServerMessage.ServerMessageType.NOTIFICATION, command.getAuthString() + " joined the game.");
        String broadcastMessage = objectToJson(broadcast);
        broadcastMessage(command.getGameID(), broadcastMessage, command.getAuthString());

        ChessGameImpl game = gameDAO.findGame(command.getGameID()).getGame();
        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String sendMessage = objectToJson(loadGame);
        sendMessage(command.getGameID(), sendMessage, command.getAuthString());
    }

    private void checkAuth(int gameID, String token) throws IOException {
        AuthToken authToken = new AuthToken(null, token);
        try {
            authDAO.findToken(authToken);
        } catch (DataAccessException e) {
            ServerNotificationError error = new ServerNotificationError(ServerMessage.ServerMessageType.ERROR, e.getMessage());
            sendMessage(gameID, objectToJson(error), token);
        }
    }

    private void checkTeamNotTaken(int gameID, ChessGame.TeamColor teamColor, String token) throws IOException {
        try {
            Game foundGame = gameDAO.findGame(gameID);
            if (teamColor.equals(ChessGame.TeamColor.BLACK) && foundGame.getBlackUsername() != null) {
                sendErrorMessage(gameID, token, "Error: team already taken");
            } else if (teamColor.equals(ChessGame.TeamColor.WHITE) && foundGame.getWhiteUsername() != null) {
                sendErrorMessage(gameID, token, "Error: team already taken");
            }
        } catch (DataAccessException e) {
            sendErrorMessage(gameID, token, e.getMessage());
        }
    }

    private void sendErrorMessage(int gameID, String token, String message) throws IOException {
        ServerNotificationError error = new ServerNotificationError(ServerMessage.ServerMessageType.ERROR, message);
        sendMessage(gameID, objectToJson(error), token);
    }



    private String objectToJson(Object o) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoardImpl.class, new ChessBoardAdapter());
        Gson gson = builder.create();
        return gson.toJson(o);
    }

    private void makeMove(Session session, MakeMove command) throws DataAccessException, InvalidMoveException {
        gameDAO.updateGame(command.getGameID(), command.getChessMove());

    }

    private UserGameCommand getCommand(String message) {
        UserGameCommand command = (UserGameCommand) jsonToObject(UserGameCommand.class, message);
        Class desiredClass = getDesiredClass(command);
        return (UserGameCommand) jsonToObject(desiredClass, message);
    }

    private Class getDesiredClass(UserGameCommand command) {
        return switch (command.getCommandType()) {
            case JOIN_PLAYER -> JoinPlayer.class;
            case JOIN_OBSERVER -> null;
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
