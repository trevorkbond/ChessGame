package services;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import services.request.LoginRequest;
import services.result.LoginRegisterResult;

/**
 * LoginService implements the login API functionality
 */
public class LoginService {

    /**
     * LoginService needs access to users in the database
     */
    private final UserDAO userDAO;

    /**
     * LoginService needs access to authTokens in the database
     */
    private final AuthDAO authDAO;

    /**
     * Constructor that initializes DAO's to static instances
     */
    public LoginService() {
        userDAO = UserDAO.getInstance();
        authDAO = AuthDAO.getInstance();
    }

    /**
     * Processes a login request
     *
     * @param request a LoginRequest
     * @return LoginResult containing username and authToken
     */
    public LoginRegisterResult login(LoginRequest request) throws DataAccessException {
        User foundUser = userDAO.findUser(request.getUsername());
        if (!foundUser.getUsername().equals(request.getUsername()) || !foundUser.getPassword().equals(request.getPassword())) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthToken token = new AuthToken(foundUser.getUsername());
        authDAO.addToken(token);
        return new LoginRegisterResult(null, token.getAuthToken(), token.getUsername());
    }
}
