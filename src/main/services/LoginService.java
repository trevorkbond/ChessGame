package services;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import request.LoginRequest;
import result.LoginRegisterResult;

import java.sql.SQLException;

/**
 * LoginService implements the login API functionality
 */
public class LoginService {

    /**
     * Processes a login request
     *
     * @param request a LoginRequest
     * @return LoginResult containing username and authToken
     */
    public LoginRegisterResult login(LoginRequest request, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException, SQLException {
        User foundUser = userDAO.findUser(request.getUsername());
        if (foundUser == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (!foundUser.getUsername().equals(request.getUsername()) || !foundUser.getPassword().equals(request.getPassword())) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthToken token = new AuthToken(foundUser.getUsername());
        authDAO.addToken(token);
        return new LoginRegisterResult(null, token.getAuthToken(), token.getUsername());
    }
}
