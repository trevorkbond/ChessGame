package models;

import chess.ChessGame;

/**
 * Game represents a chess game that has been played or is being played. Each game has a unique game ID and contains
 * information about the teams, along with a Game name
 */
public class Game {
    /**
     * The ChessGame object to perform game functions on
     */
    private ChessGame game;
    /**
     * the gameID, an integer
     */
    private int gameID;
    /**
     * The username for the player on white team
     */
    private String whiteUsername;
    /**
     * The username for the player on black team
     */
    private String blackUsername;
    /**
     * The game's name, a String
     */
    private String gameName;

    /**
     * A constructor for a Game model
     *
     * @param gameID        the given gameID (int)
     * @param whiteUsername the username of the white team User
     * @param blackUsername the username of the black team User
     * @param gameName      the game name (String)
     * @param game          the given ChessGame object
     */
    public Game(ChessGame game, int gameID, String whiteUsername, String blackUsername, String gameName) {
        this.game = game;
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}
