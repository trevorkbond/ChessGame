package services;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import services.result.ListGamesResult;
import spark.Request;

/**
 * ListGamesService implements the list games ("/games") API functionality
 */
public class ListGamesService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ListGamesService() {
        authDAO = AuthDAO.getInstance();
        gameDAO = GameDAO.getInstance();
    }

    /**
     * processes a list games request
     *
     * @param authToken the authorized user's token
     * @return ListGamesResult, a list of all games in the database
     */
    public ListGamesResult listGames(AuthToken authToken, Request httpRequest) throws DataAccessException {
        authDAO.findToken(new AuthToken(null, httpRequest.headers("Authorization")));
        return new ListGamesResult(null, gameDAO.getGames());
    }
}
