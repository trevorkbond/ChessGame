package services.result;

import chess.ChessGame;

/**
 * GameResult represents a single game in the database, upon which ListGameResult will draw for a list of all games
 */
public class GameResult extends Result {
    /**
     * An integer representing gameID
     */
    private int gameID;
    /**
     * The username of the User for the white team
     */
    private String whiteUsername;
    /**
     * The username of the User for the black team
     */
    private String blackUsername;
    /**
     * The resulting gameName as a String
     */
    private String gameName;

    /**
     * A constructor for a GameResult
     * @param message,       the given error message
     * @param gameID,        the given gameID (int)
     * @param whiteUsername, the username of the white team User
     * @param blackUsername, the username of the black team User
     * @param gameName,      the game name (String)
     */
    public GameResult(String message, int gameID, String whiteUsername, String blackUsername, String gameName) {
        super(message);
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
