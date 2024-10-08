package dao;

import dataAccess.DataAccessException;
import models.AuthToken;

import java.sql.SQLException;

/**
 * This is a superclass that contains shared functions between DAO's
 */
public class DAO {
    public void handleSQLException(SQLException e) throws DataAccessException {
        if (e.getMessage().contains("Duplicate entry")) {
            throw new DataAccessException("Error: already taken");
        } else {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void confirmAuthorization(AuthToken token, AuthDAO authDAO) throws DataAccessException {
        AuthToken foundToken = authDAO.findToken(token);
        if (foundToken == null) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
