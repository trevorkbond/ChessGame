package services;

import dao.AuthDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import result.Result;

/**
 * LogoutService implements the logout API functionality
 */
public class LogoutService {

    /**
     * Logs out the user represented by the authToken
     *
     * @param authToken the given user (AuthToken)
     * @return Result of the operation
     * @throws DataAccessException if request doesn't have proper authorization
     */
    public Result logout(AuthToken authToken, AuthDAO authDAO) throws DataAccessException {
        AuthToken tempToken = new AuthToken(authToken.getUsername(), authToken.getAuthToken());
        authDAO.deleteToken(tempToken);
        return new Result(null);
    }
}
