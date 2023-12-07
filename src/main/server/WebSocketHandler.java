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
import webSocketMessages.userCommands.*;

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
            case RESIGN -> resign(session, (Resign) command);
            case LEAVE -> leave(session, (Leave) command);
        }
    }

    private void joinPlayer(Session session, JoinPlayer command) throws IOException, DataAccessException {
        try {
            checkAuth(command.getGameID(), command.getAuthString());
            checkValidGameID(command.getGameID());
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
            checkValidGameID(command.getGameID());

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
        AuthToken foundToken = authDAO.findToken(new AuthToken(null, token));
        if (foundToken == null) {
            throw new DataAccessException("Error: authToken not valid");
        }
    }

    private void checkValidGameID(int gameID) throws DataAccessException {
        Game foundGame = gameDAO.findGame(gameID);
        if (foundGame == null) {
            throw new DataAccessException("Error: game not found");
        }
    }

    private void checkTeamNotTaken(int gameID, ChessGame.TeamColor teamColor, String token) throws DataAccessException {
        Game foundGame = gameDAO.findGame(gameID);
        if (teamColor.equals(ChessGame.TeamColor.BLACK)) {
            if (foundGame.getBlackUsername() != null) {
                AuthToken foundToken = authDAO.findToken(new AuthToken(null, token));
                if (!foundToken.getUsername().equals(foundGame.getBlackUsername())) {
                    throw new DataAccessException("Error: team already taken");
                }
            } else {
                throw new DataAccessException("Error: Tried to join empty team");
            }
        } else if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
            if (foundGame.getWhiteUsername() != null) {
                AuthToken foundToken = authDAO.findToken(new AuthToken(null, token));
                if (!foundToken.getUsername().equals(foundGame.getWhiteUsername())) {
                    throw new DataAccessException("Error: team already taken");
                }
            } else {
                throw new DataAccessException("Error: Tried to join empty team");
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
            Game foundGame = gameDAO.findGame(command.getGameID());
            ChessGame.TeamColor moveColor = foundGame.getGame().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor();
            checkCorrectUsername(moveColor, command.getAuthString(), foundGame);
            checkGameNotOver(foundGame);

            ChessGameImpl updatedGame = gameDAO.updateGame(command.getGameID(), command.getMove());

            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame);
            sendMessage(command.getGameID(), objectToJson(loadGame), command.getAuthString());
            broadcastMessage(command.getGameID(), objectToJson(loadGame), command.getAuthString());

            ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, command.getUsername() + " made a move");
            broadcastMessage(command.getGameID(), objectToJson(notification), command.getAuthString());

            checkIfInCheck(foundGame, moveColor, updatedGame, command.getGameID(), command.getAuthString());
        }
        catch (InvalidMoveException e) {
            sendErrorMessage(command.getGameID(), command.getAuthString(), e.getMessage());
        }
    }


    private void resign(Session session, Resign command) throws IOException {
        try {
            checkAuth(command.getGameID(), command.getAuthString());
            checkValidGameID(command.getGameID());
            checkNotObserver(command.getGameID(), command.getAuthString());
            Game foundGame = gameDAO.findGame(command.getGameID());
            checkGameNotOver(foundGame);

            gameDAO.updateGameOver(command.getGameID());
            String resignedUser = authDAO.findToken(new AuthToken(null, command.getAuthString())).getUsername();
            notifyEveryone(resignedUser + " resigned, the game is over", command.getAuthString(), command.getGameID());

        } catch (DataAccessException e) {
            webSocketSessions.addSessionToGame(command.getGameID(), command.getAuthString(), session);
            sendErrorMessage(command.getGameID(), command.getAuthString(), e.getMessage());
        } catch (InvalidMoveException e) {
            sendErrorMessage(command.getGameID(), command.getAuthString(), e.getMessage());
        }
    }

    private void leave(Session session, Leave command) throws IOException {
        try {
            checkAuth(command.getGameID(), command.getAuthString());
            checkValidGameID(command.getGameID());

            String username = authDAO.findToken(new AuthToken(null, command.getAuthString())).getUsername();
            String userPosition = getUserPosition(command.getGameID(), command.getAuthString());
            if (userPosition != null) {
                gameDAO.updateUsername(command.getGameID(), null, userPosition);
            }

            ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, username + " left the game");
            broadcastMessage(command.getGameID(), objectToJson(notification), command.getAuthString());

            webSocketSessions.removeSessionFromGame(command.getGameID(), command.getAuthString());
        } catch (DataAccessException e) {
            sendErrorMessage(command.getGameID(), command.getAuthString(), e.getMessage());
        }
    }

    private String getUserPosition(int gameID, String authToken) throws DataAccessException {
        Game foundGame = gameDAO.findGame(gameID);
        String username = authDAO.findToken(new AuthToken(null, authToken)).getUsername();
        if (username.equals(foundGame.getBlackUsername())) {
            return "blackUsername";
        } else if (username.equals(foundGame.getWhiteUsername())) {
            return "whiteUsername";
        }
        return null;
    }

    private void checkNotObserver(int gameID, String authString) throws DataAccessException {
        String username = authDAO.findToken(new AuthToken(null, authString)).getUsername();
        Game foundGame = gameDAO.findGame(gameID);
        if (!username.equals(foundGame.getWhiteUsername()) && !username.equals(foundGame.getBlackUsername())) {
            throw new DataAccessException("Error: you are an observer and cannot resign a game");
        }
    }

    private void checkGameNotOver(Game foundGame) throws InvalidMoveException {
        if (foundGame.isGameOver()) {
            throw new InvalidMoveException("Error: This game is over");
        }
    }

    private void checkIfInCheck(Game foundGame, ChessGame.TeamColor moveColor, ChessGameImpl game, int gameID, String authString) throws IOException, DataAccessException {
        ChessGame.TeamColor otherColor = null;
        String inCheckUser = null;
        if (moveColor.equals(ChessGame.TeamColor.WHITE)) {
            otherColor = ChessGame.TeamColor.BLACK;
            inCheckUser = foundGame.getBlackUsername();
        } else {
            otherColor = ChessGame.TeamColor.WHITE;
            inCheckUser = foundGame.getWhiteUsername();
        }
        if (game.isInCheckmate(otherColor)) {
            gameDAO.updateGameOver(gameID);
            notifyEveryone(inCheckUser + " is in checkmate\nThe game is over", authString, gameID);
        } else if (game.isInStalemate(otherColor)) {
            gameDAO.updateGameOver(gameID);
            notifyEveryone("The game is in a stalemate\nThe game is over", authString, gameID);
        } else if (game.isInCheck(otherColor)) {
            notifyEveryone(inCheckUser + " is in check", authString, gameID);
        }
    }

    private void notifyEveryone(String message, String authString, int gameID) throws IOException {
        ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        sendMessage(gameID, objectToJson(notification), authString);
        broadcastMessage(gameID, objectToJson(notification), authString);
    }

    private void checkCorrectUsername(ChessGame.TeamColor moveColor, String authString, Game game) throws DataAccessException, InvalidMoveException {
        AuthToken userToken = authDAO.findToken(new AuthToken(null, authString));
        String username = userToken.getUsername();
        if (moveColor.equals(ChessGame.TeamColor.BLACK) && !game.getBlackUsername().equals(username)) {
            throw new InvalidMoveException("Error: You cannot move for the other team");
        } else if (moveColor.equals(ChessGame.TeamColor.WHITE) && !game.getWhiteUsername().equals(username)) {
            throw new InvalidMoveException("Error: You cannot move for the other team");
        }
    }

    private UserGameCommand getCommand(String message) {
        UserGameCommand command = (UserGameCommand) jsonToObject(UserGameCommand.class, message);
        Class desiredClass = getDesiredClass(command);
        return (UserGameCommand) jsonToObject(desiredClass, message);
    }

    private Class getDesiredClass(UserGameCommand command) {
        return switch (command.getCommandType()) {
            case JOIN_PLAYER -> JoinPlayer.class;
            case JOIN_OBSERVER -> JoinObserver.class;
            case MAKE_MOVE -> MakeMove.class;
            case LEAVE -> Leave.class;
            case RESIGN -> Resign.class;
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
