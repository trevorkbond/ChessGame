package services.result;

import chess.ChessGame;

/**
 * GameResult represents a single game in the database, upon which ListGameResult will draw for a list of all games
 */
public class GameResult {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;

    /**
     * A constructor for a GameResult
     *
     * @param gameID,        the given gameID (int)
     * @param whiteUsername, the username of the white team User
     * @param blackUsername, the username of the black team User
     * @param gameName,      the game name (String)
     */
    public GameResult(int gameID, String whiteUsername, String blackUsername, String gameName) {
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
