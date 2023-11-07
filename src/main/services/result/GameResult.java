package services.result;

/**
 * GameResult represents a single game in the database, upon which ListGameResult will draw for a list of all games
 */
public class GameResult extends Result {
    /**
     * An integer representing gameID
     */
    private final int gameID;
    /**
     * The username of the User for the white team
     */
    private final String whiteUsername;
    /**
     * The username of the User for the black team
     */
    private final String blackUsername;
    /**
     * The resulting gameName as a String
     */
    private final String gameName;

    /**
     * A constructor for a GameResult
     *
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

}
