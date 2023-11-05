package dao;

import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * AuthDAO is responsible for handling and retrieving the database's AuthTokens
 */

public class AuthDAO {

    private Connection connection;

    public AuthDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds the given token to the database
     *
     * @param token the given AuthToken to add
     * @throws DataAccessException if given token already in database
     */
    public void addToken(AuthToken token) throws DataAccessException, SQLException {
        // TODO: maybe add findToken here if need be?
        String insertSQL = "insert into authToken (authToken, username) values (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, token.getAuthToken());
            stmt.setString(2, token.getUsername());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes the given token from the database
     *
     * @param token the given token to delete
     * @throws DataAccessException if given token isn't in database
     */
    public void deleteToken(AuthToken token) throws DataAccessException {
        // TODO: implement with database
    }

    /**
     * Finds and retrieves the given token from the database
     *
     * @param token the given token to retrieve
     * @return the found AuthToken
     * @throws DataAccessException if given token isn't in database
     */
    public AuthToken findToken(AuthToken token) throws DataAccessException {
        // TODO: implement with database
        return null;
    }

    public void clearTokens() throws SQLException {
        String dropSQL = "delete from authToken";
        try (PreparedStatement stmt = connection.prepareStatement(dropSQL)) {
            stmt.executeUpdate();
        }
    }

    /**
     * Clears all tokens from the database
     */
    public HashSet<AuthToken> getTokens() throws SQLException {
        String selectSQL = "select * from authToken";
        HashSet<AuthToken> tokens = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String authToken = rs.getString(1);
                String username = rs.getString(2);

                tokens.add(new AuthToken(username, authToken));
            }
        }
        return tokens;
    }
}
