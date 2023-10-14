package services;

import models.AuthToken;
import services.result.ListGamesResult;

/**
 * ListGamesService implements the list games ("/games") API functionality
 */
public class ListGamesService {
    /**
     * processes a list games request
     * @param authToken, the authorized user's token
     * @return ListGamesResult, a list of all games in the database
     */
    public ListGamesResult listGames(AuthToken authToken) {
        return null;
    }
}
