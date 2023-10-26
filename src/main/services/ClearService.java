package services;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import services.result.Result;

/**
 * ClearService implements the clear ("/db") API functionality
 */
public class ClearService {
    /**
     * ClearService has access to authToken database via DAO
     */
    private final AuthDAO authDAO;
    /**
     * ClearService has access to user database via DAO
     */
    private final UserDAO userDAO;
    /**
     * ClearService has access to game database via DAO
     */
    private final GameDAO gameDAO;

    /**
     * constructor for ClearService
     */

    public ClearService() {
        authDAO = AuthDAO.getInstance();
        userDAO = UserDAO.getInstance();
        gameDAO = GameDAO.getInstance();
    }

    public Result clear() throws DataAccessException {
        authDAO.clearTokens();
        userDAO.clearUsers();
        gameDAO.clearGames();
        return new Result("");
    }
}
