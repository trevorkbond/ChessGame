package dao;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import models.Game;
import models.User;
import services.handlers.Handler;
import spark.Request;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.sql.Connection;

/**
 * GameDAO is responsible for handling and retrieving the database's Games
 */
public class GameDAO {

    private Connection connection;

    public GameDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a game into the database
     *
     * @param game the game to insert
     * @throws DataAccessException if game with same ID already exists within the database
     */
    public void createGame(Game game) throws DataAccessException {
        //TODO: implement with database
    }

    /**
     * Finds a game with the given gameID in the database
     *
     * @param gameID the given gameID
     * @return the found Game
     * @throws DataAccessException if Game of given gameID isn't in database
     */
    public Game findGame(int gameID) throws DataAccessException {
        //TODO: implement with database
        return null;
    }

    /**
     * Reserves a "spot" for a player in a game - the User with given username takes either whitePlayer or blackPlayer
     *
     * @param gameID      the given gameID to
     * @param playerColor the desired color for the User to claim
     * @param username    the given username
     * @throws DataAccessException if the game isn't found, if spot is already taken or if a User with given
     *                             username doesn't exist
     */
    public void claimSpot(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        Game foundGame = findGame(gameID);
        if (playerColor == ChessGame.TeamColor.WHITE && foundGame.getWhiteUsername() == null) {
            System.out.println("Adding " + username + " to game of ID " + gameID);
            foundGame.setWhiteUsername(username);
        } else if (playerColor == ChessGame.TeamColor.BLACK && foundGame.getBlackUsername() == null) {
            System.out.println("Adding " + username + " to game of ID " + gameID);
            foundGame.setBlackUsername(username);
        } else if (playerColor != null) {
            throw new DataAccessException("Error: already taken");
        }
    }

    /**
     * Clears all of the games from the database
     */
    public void clearGames() {
        //TODO: implement with database
    }

    public HashSet<Game> getGames() throws SQLException {
        String selectSQL = "select * from game";
        HashSet<Game> games = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int gameID = rs.getInt(1);
                String whiteUsername = rs.getString(2);
                String blackUsername = rs.getString(3);
                String gameName = rs.getString(4);
                String gameJSON = rs.getString(5);

                ChessGame game = gameFromJSON(gameJSON);
                games.add(new Game(game, gameID, whiteUsername, blackUsername, gameName));
            }
        }
        return games;
    }

    private ChessGame gameFromJSON(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        return gson.fromJson(json, ChessGame.class);
    }
}
