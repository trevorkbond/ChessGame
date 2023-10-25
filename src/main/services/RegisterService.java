package services;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import services.request.RegisterRequest;
import services.result.LoginRegisterResult;

/**
 * RegisterService implements the register API functionality
 */
public class RegisterService {
    /**
     * RegisterService has access to the set of users represented by UserDAO
     */
    private UserDAO userDAO;
    /**
     * RegisterService has access to the set of authTokens represented by authDAO
     */
    private AuthDAO authDAO;

    /**
     * Default constructor for RegisterService that initializes the userDAO and authDAO object
     */
    public RegisterService() {
        userDAO = UserDAO.getInstance();
        authDAO = AuthDAO.getInstance();
    }

    /**
     * Processes a register request
     *
     * @param request the given RegisterRequest
     * @return LoginRegisterResult containing a username, message, and authToken
     */
    public LoginRegisterResult register(RegisterRequest request) throws DataAccessException {
        User user = new User(request);
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new DataAccessException("Error: bad request");
        }
        userDAO.createUser(user);
        AuthToken token = new AuthToken(user.getUsername());
        authDAO.addToken(token);
        return new LoginRegisterResult(null, token.getAuthToken(), token.getUsername());
    }
}
