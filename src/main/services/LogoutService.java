package services;

import dao.AuthDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import services.result.Result;

/**
 * LogoutService implements the logout API functionality
 */
public class LogoutService {

    /**
     * LogoutService needs access to authToken database
     */
    private AuthDAO authDAO;

    /**
     * Constructs a logout service and retrieves static instance of DAO
     */
    public LogoutService() {
        authDAO = AuthDAO.getInstance();
    }

    /**
     * Logs out the user represented by the authToken
     *
     * @param authToken the given user (AuthToken)
     * @return Result of the operation
     * @throws DataAccessException if request doesn't have proper authorization
     */
    public Result logout(AuthToken authToken) throws DataAccessException {
        authDAO.findToken(authToken);
        AuthToken tempToken = new AuthToken(authToken.getUsername(), authToken.getAuthToken());
        authDAO.deleteToken(tempToken);
        return new Result(null);
    }
}
