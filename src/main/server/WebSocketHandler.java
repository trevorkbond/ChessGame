package server;

import chess.ChessBoardImpl;
import chess.ChessGameImpl;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.ChessBoardAdapter;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverNotifications.LoadGame;
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

    public WebSocketHandler() throws DataAccessException {
        webSocketSessions = WebSocketSessions.getInstance();
        Connection connection = Server.database.getConnection();
        gameDAO = new GameDAO(connection);
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
        webSocketSessions.addSessionToGame(command.getGameID(), command.getUsername(), session);
        String broadcast = command.getUsername() + " joined the game.";
        System.out.println("sessions after join:\n" + webSocketSessions);
        broadcastMessage(command.getGameID(), broadcast, command.getUsername());

        ChessGameImpl game = gameDAO.findGame(command.getGameID()).getGame();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoardImpl.class, new ChessBoardAdapter());
        Gson gson = builder.create();
        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String message = gson.toJson(loadGame);
        sendMessage(command.getGameID(), message, command.getUsername());
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
        System.out.println("in sendMessage, message is: " + message);
        System.out.println(webSocketSessions.getSessionsForGame((gameID)).get(user));
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
