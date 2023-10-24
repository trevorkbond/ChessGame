package dao;

import dataAccess.DataAccessException;
import models.AuthToken;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Objects;

/**
 * AuthDAO is responsible for handling and retrieving the database's AuthTokens
 */

public class AuthDAO {

    /**
     * The set of AuthTokens in the database
     */
    private HashSet<AuthToken> tokens;

    /**
     * Using Singleton method, only instance of AuthDAO that will ever exist
     */
    private static AuthDAO instance;

    public HashSet<AuthToken> getTokens() {
        return tokens;
    }

    /**
     * Default constructor for AuthDAO with no given hash set and private to ensure no outside instantiation
     */
    private AuthDAO() {
        tokens = new HashSet<>();
    }

    /**
     * Get instance for singleton pattern
     * @return the sole instance of AuthDAO
     */
    public static AuthDAO getInstance() {
        if (instance == null) {
            instance = new AuthDAO();
        }
        return instance;
    }
    /**
     * Adds the given token to the database
     * @param token the given AuthToken to add
     * @throws DataAccessException if given token already in database
     */
    public void addToken(AuthToken token) throws DataAccessException {
        if (tokens.contains(token)) {
            throw new DataAccessException("The given token is already in the database");
        } else {
            tokens.add(token);
        }
    }

    /**
     * Deletes the given token from the database
     * @param token the given token to delete
     * @throws DataAccessException if given token isn't in database
     */
    public void deleteToken(AuthToken token) throws DataAccessException {
        if (!tokens.contains(token)) {
            throw new DataAccessException("The given token isn't in the database");
        } else {
            tokens.remove(token);
        }
    }

    /**
     * Finds and retrieves the given token from the database
     * @param token the given token to retrieve
     * @return the found AuthToken
     * @throws DataAccessException if given token isn't in database
     */
    public AuthToken findToken(AuthToken token) throws DataAccessException {
        if (!tokens.contains(token)) {
            throw new DataAccessException("Error: unauthorized");
        } else {
            return token;
        }
    }

    /**
     * Clears all tokens from the database
     */
    public void clearTokens() throws DataAccessException {
        tokens.clear();
    }
}
