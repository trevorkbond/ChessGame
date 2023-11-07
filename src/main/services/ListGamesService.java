package services;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import services.result.ListGamesResult;

/**
 * ListGamesService implements the list games ("/games") API functionality
 */
public class ListGamesService {

    /**
     * processes a list games request
     *
     * @param authToken the authorized user's token
     * @return ListGamesResult, a list of all games in the database
     */
    public ListGamesResult listGames(AuthToken authToken, String httpRequest, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        if (authDAO.findToken(new AuthToken(null, httpRequest)) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new ListGamesResult(null, gameDAO.getGames());
    }
}
