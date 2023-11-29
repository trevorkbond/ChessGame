package services;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import request.JoinGameRequest;
import result.Result;

/**
 * JoinGameService implements the join game API functionality. Verifies that the specified game exists, and, if a
 * color is specified, adds the caller as the requested color to the game. If no color is specified the user is
 * joined as an observer.
 */
public class GameService {

    /**
     * Processes a join game request
     *
     * @param request the given JoinGameRequest
     * @return the Result of the operation
     */
    public Result joinGame(JoinGameRequest request, String httpRequest, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        if (gameDAO.findGame(request.getGameID()) == null) {
            throw new DataAccessException("Error: bad request");
        }
        if (authDAO.findToken(new AuthToken(null, httpRequest)) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthToken authToken = authDAO.findToken(new AuthToken(null, httpRequest));
        gameDAO.claimSpot(request.getGameID(), request.getPlayerColor(), authToken.getUsername());
        return new Result(null);
    }


}
