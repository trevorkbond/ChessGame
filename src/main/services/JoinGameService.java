package services;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import services.request.JoinGameRequest;
import services.result.Result;
import spark.Request;

/**
 * JoinGameService implements the join game API functionality. Verifies that the specified game exists, and, if a
 * color is specified, adds the caller as the requested color to the game. If no color is specified the user is
 * joined as an observer.
 */
public class JoinGameService {

    /**
     * Service needs to access auth database to check if action is authorized
     */
    private AuthDAO authDAO;
    /**
     * Service needs access to game database
     */
    private GameDAO gameDAO;

    /**
     * Constructor for JoinGameService
     */
    public JoinGameService() {
        authDAO = AuthDAO.getInstance();
        gameDAO = GameDAO.getInstance();
    }

    /**
     * Processes a join game request
     * @param request the given JoinGameRequest
     * @return the Result of the operation
     */
    public Result joinGame(JoinGameRequest request, Request httpRequest) throws DataAccessException {
        gameDAO.findGame(request.getGameID());
        AuthToken authToken = authDAO.findToken(new AuthToken(null, httpRequest.headers("Authorization")));
        gameDAO.claimSpot(request.getGameID(), request.getPlayerColor(), authToken.getUsername());
        return new Result(null);
    }
}
