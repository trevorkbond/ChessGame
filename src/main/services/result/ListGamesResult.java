package services.result;

import java.util.HashSet;

/**
 * ListGamesResult represents the result of a call to the list games ("\game") API call, containing
 * a list of all games
 */
public class ListGamesResult {
    private HashSet<GameResult> games;

    /**
     * Cosntructor for ListGameResult
     * @param games, the given set of games
     */
    public ListGamesResult(HashSet<GameResult> games) {
        this.games = games;
    }

    public HashSet<GameResult> getGames() {
        return games;
    }

    public void setGames(HashSet<GameResult> games) {
        this.games = games;
    }
}
