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
     * The set of games in the database
     */
    private HashSet<Game> games;

    /**
     * Using Singleton method
     */
    private static GameDAO instance;
    /**
     * Constructor for a GameDAO, private to ensure no direct instantiation
     */
    private GameDAO() {
        games = new HashSet<>();
    }

    /**
     * getInstance to implement singleton method
     * @return the sole instance of GameDAO
     */
    public static GameDAO getInstance() {
        if (instance == null) {
            instance = new GameDAO();
        }
        return instance;
    }

    /**
     * Inserts a game into the database
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
     * @param gameID the given gameID
     * @return the found Game
     * @throws DataAccessException if Game of given gameID isn't in database
     */
    public Game findGame(int gameID) throws DataAccessException {
        return null;
    }

    /**
     * A method that retrieves all games from the database
     * @return a HashSet of all the games
     * @throws DataAccessException if there are no games in the database
     */
    public HashSet<Game> findAll() throws DataAccessException {
        return null;
    }

    /**
     * Reserves a "spot" for a player in a game - the User with given username takes either whitePlayer or blackPlayer
     * @param game the given game to reserve a spot in
     * @param playerColor the desired color for the User to claim
     * @param username the given username
     * @throws DataAccessException if the game isn't found, if spot is already taken or if a User with given
     *                              username doesn't exist
     */
    public void claimSpot(Game game, ChessGame.TeamColor playerColor, String username) throws DataAccessException {

    }

    /**
     * Updates the game name of the given gameID to the given chessGame name
     * @param gameID the given gameID
     * @param chessGame the new name to update to
     * @throws DataAccessException if no game of the given gameID exists
     */
    public void updateGame(int gameID, String chessGame) throws DataAccessException {

    }

    /**
     * Deletes the game of the given gameID from the database
     * @param gameID the given gameID
     * @throws DataAccessException if no game of the given gameID exists
     */
    public void deleteGame(int gameID) throws DataAccessException {

    }

    /**
     * Clears all of the games from the database
     */
    public void clearGames() throws DataAccessException {
        games.clear();
    }
}
