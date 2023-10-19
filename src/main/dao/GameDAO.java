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
     * Constructor for a GameDAO
     * @param games the given set of games
     */
    public GameDAO(HashSet<Game> games) {
        this.games = games;
    }

    /**
     * Inserts a game into the database
     * @param game the game to insert
     * @throws DataAccessException if game with same ID already exists within the database
     */
    public void createGame(Game game) throws DataAccessException {

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
}
