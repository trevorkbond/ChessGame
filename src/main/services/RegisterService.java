package services;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import services.request.RegisterRequest;
import services.result.LoginRegisterResult;

import java.sql.SQLException;

/**
 * RegisterService implements the register API functionality
 */
public class RegisterService {

    /**
     * Processes a register request
     *
     * @param request the given RegisterRequest
     * @return LoginRegisterResult containing a username, message, and authToken
     */
    public LoginRegisterResult register(RegisterRequest request, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException, SQLException {
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
