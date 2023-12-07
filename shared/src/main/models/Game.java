package models;

import chess.ChessGameImpl;

import java.util.Objects;

/**
 * Game represents a chess game that has been played or is being played. Each game has a unique game ID and contains
 * information about the teams, along with a Game name
 */
public class Game {

    /**
     * The ChessGame object to perform game functions on
     */
    private final ChessGameImpl game;
    /**
     * The game's name, a String
     */
    private final String gameName;
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
    private boolean gameOver;

    /**
     * A constructor for a Game model
     *
     * @param gameID        the given gameID (int)
     * @param whiteUsername the username of the white team User
     * @param blackUsername the username of the black team User
     * @param gameName      the game name (String)
     * @param game          the given ChessGame object
     */
    public Game(ChessGameImpl game, int gameID, String whiteUsername, String blackUsername, String gameName) {
        this.game = game;
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Game(ChessGameImpl game, String whiteUsername, String blackUsername, String gameName) {
        this.game = game;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }

    /**
     * Constructor that is only passed in a gameName and assigned a unique ID from static private member
     *
     * @param gameName the given gameName as a String
     */
    public Game(String gameName) {
        game = new ChessGameImpl();
        this.gameName = gameName;
        this.gameOver = false;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGameImpl getGame() {
        return game;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game game)) return false;
        return gameID == game.gameID;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public String toString() {
        return gameName + ":\n" +
                "\tWhite Player: " + (whiteUsername == null ? "not taken" : whiteUsername) + '\n' +
                "\tBlack Player: " + (blackUsername == null ? "not taken" : blackUsername) + '\n' +
                (gameOver ? "This game is over and cannot be played further\n" : "");
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID);
    }

}
