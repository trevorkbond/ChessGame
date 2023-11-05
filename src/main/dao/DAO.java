package dao;

import dataAccess.DataAccessException;

import javax.xml.crypto.Data;
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
}
