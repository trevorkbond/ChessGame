package services;

import services.request.JoinGameRequest;
import services.result.Result;

/**
 * JoinGameService implements the join game API functionality. Verifies that the specified game exists, and, if a
 * color is specified, adds the caller as the requested color to the game. If no color is specified the user is
 * joined as an observer.
 */
public class JoinGameService {
    /**
     * Processes a join game request
     * @param request the given JoinGameRequest
     * @return the Result of the operation
     */
    public Result joinGame(JoinGameRequest request) {
        return null;
    }
}
