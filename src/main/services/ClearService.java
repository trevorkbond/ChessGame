package services;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import result.Result;

import java.sql.SQLException;

/**
 * ClearService implements the clear ("/db") API functionality
 */
public class ClearService {

    public Result clear(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException, SQLException {
        userDAO.clearUsers();
        authDAO.clearTokens();
        gameDAO.clearGames();
        return new Result("");
    }
}
