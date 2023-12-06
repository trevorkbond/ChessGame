package dao;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import models.ChessBoardAdapter;
import models.Game;

import java.sql.*;
import java.util.HashSet;

/**
 * GameDAO is responsible for handling and retrieving the database's Games
 */
public class GameDAO extends DAO {

    private final Connection connection;

    public GameDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a game into the database
     *
     * @param game the game to insert
     * @throws DataAccessException if game with same ID already exists within the database
     */
    public int createGame(Game game) throws DataAccessException {
        if (findGame(game.getGameID()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        ChessGameImpl chessGame = game.getGame();
        chessGame.getBoard().resetBoard();
        String insertSQL = "insert into game (gameName, game) values (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, game.getGameName());
            stmt.setString(2, gameToJson(chessGame));

            if (stmt.executeUpdate() == 1) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    int id = generatedKeys.getInt(1);
                    return id;
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return -1;
    }

    /**
     * Finds a game with the given gameID in the database
     *
     * @param gameID the given gameID
     * @return the found Game
     * @throws DataAccessException if Game of given gameID isn't in database
     */
    public Game findGame(int gameID) throws DataAccessException {
        String selectSQL = "select * from game where gameID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int foundGameID = rs.getInt(1);
                String whiteUsername = rs.getString(2);
                String blackUsername = rs.getString(3);
                String gameName = rs.getString(4);
                String gameJSON = rs.getString(5);

                ChessGameImpl game = gameFromJSON(gameJSON);
                return new Game(game, foundGameID, whiteUsername, blackUsername, gameName);
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
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
            updateUsername(gameID, username, "whiteUsername");
        } else if (playerColor == ChessGame.TeamColor.BLACK && foundGame.getBlackUsername() == null) {
            updateUsername(gameID, username, "blackUsername");
        } else if (playerColor != null) {
            throw new DataAccessException("Error: already taken");
        }
    }

    private void updateUsername(int gameID, String username, String userToChange) throws DataAccessException {
        String updateSQL = "update game set " + userToChange + " = ? where gameID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
            stmt.setString(1, username);
            stmt.setInt(2, gameID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public ChessGameImpl updateGame(int gameID, ChessMove move) throws DataAccessException, InvalidMoveException {
        Game foundGame = findGame(gameID);
        if (foundGame == null) {
            throw new DataAccessException("Error: bad request");
        }
        ChessGameImpl gameState = foundGame.getGame();
        gameState.makeMove(move);
        String serializedGame = gameToJson(gameState);
        String updateSQL = "update game set game = ? where gameID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
            stmt.setString(1, serializedGame);
            stmt.setInt(2, gameID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return gameState;
    }

    /**
     * Clears all of the games from the database
     */
    public void clearGames() throws DataAccessException {
        String dropSQL = "delete from game";
        try (PreparedStatement stmt = connection.prepareStatement(dropSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void resetIDCounter() throws DataAccessException {
        String alterSQL = "alter table game auto_increment = 1";
        try (PreparedStatement stmt = connection.prepareStatement(alterSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public HashSet<Game> getGames() throws DataAccessException {
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

                ChessGameImpl game = gameFromJSON(gameJSON);
                games.add(new Game(game, gameID, whiteUsername, blackUsername, gameName));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return games;
    }

    private ChessGameImpl gameFromJSON(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(ChessBoardImpl.class, new ChessBoardAdapter());

        Gson gson = builder.create();
        return gson.fromJson(json, ChessGameImpl.class);
    }

    private String gameToJson(ChessGameImpl game) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(game);
    }
}
