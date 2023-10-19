package services.result;

import java.util.HashSet;

/**
 * ListGamesResult represents the result of a call to the list games ("\game") API call, containing
 * a list of all games
 */
public class ListGamesResult extends Result {
    /**
     * This hash set represents the list of games that is returned in the result
     */
    private HashSet<GameResult> games;

    /**
     * Constructor for ListGameResult
     * @param message, the given error message
     * @param games, the given set of games
     */
    public ListGamesResult(String message, HashSet<GameResult> games) {
        super(message);
        this.games = games;
    }

    public HashSet<GameResult> getGames() {
        return games;
    }

    public void setGames(HashSet<GameResult> games) {
        this.games = games;
    }
}
