package services.result;

import models.Game;

import java.util.HashSet;
import java.util.Objects;

/**
 * ListGamesResult represents the result of a call to the list games ("\game") API call, containing
 * a list of all games
 */
public class ListGamesResult extends Result {
    /**
     * This hash set represents the list of games that is returned in the result
     */
    private final HashSet<Game> games;

    /**
     * Constructor for ListGameResult
     *
     * @param message, the given error message
     * @param games,   the given set of games
     */
    public ListGamesResult(String message, HashSet<Game> games) {
        super(message);
        this.games = games;
    }

    public HashSet<Game> getGames() {
        return games;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListGamesResult that)) return false;
        return Objects.equals(getGames(), that.getGames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGames());
    }
}
