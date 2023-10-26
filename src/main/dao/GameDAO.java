package dao;

import chess.ChessGame;
import dataAccess.DataAccessException;
import models.Game;

import java.util.HashSet;

/**
 * GameDAO is responsible for handling and retrieving the database's Games
 */
public class GameDAO {

    /**
     * Using Singleton method
     */
    private static GameDAO instance;
    /**
     * The set of games in the database
     */
    private final HashSet<Game> games;

    /**
     * Constructor for a GameDAO, private to ensure no direct instantiation
     */
    private GameDAO() {
        games = new HashSet<>();
    }

    /**
     * getInstance to implement singleton method
     *
     * @return the sole instance of GameDAO
     */
    public static GameDAO getInstance() {
        if (instance == null) {
            instance = new GameDAO();
        }
        return instance;
    }

    public HashSet<Game> getGames() {
        return games;
    }

    /**
     * Inserts a game into the database
     *
     * @param game the game to insert
     * @throws DataAccessException if game with same ID already exists within the database
     */
    public void createGame(Game game) throws DataAccessException {
        if (games.contains(game)) {
            throw new DataAccessException("Error: game already in database");
        }
        games.add(game);
    }

    /**
     * Finds a game with the given gameID in the database
     *
     * @param gameID the given gameID
     * @return the found Game
     * @throws DataAccessException if Game of given gameID isn't in database
     */
    public Game findGame(int gameID) throws DataAccessException {
        for (Game game : games) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Error: bad request");
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
        games.clear();
    }
}
