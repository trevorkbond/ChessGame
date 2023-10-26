package dao;

import dataAccess.DataAccessException;
import models.AuthToken;

import java.util.HashSet;

/**
 * AuthDAO is responsible for handling and retrieving the database's AuthTokens
 */

public class AuthDAO {

    /**
     * Using Singleton method, only instance of AuthDAO that will ever exist
     */
    private static AuthDAO instance;
    /**
     * The set of AuthTokens in the database
     */
    private final HashSet<AuthToken> tokens;

    /**
     * Default constructor for AuthDAO with no given hash set and private to ensure no outside instantiation
     */
    private AuthDAO() {
        tokens = new HashSet<>();
    }

    /**
     * Get instance for singleton pattern
     *
     * @return the sole instance of AuthDAO
     */
    public static AuthDAO getInstance() {
        if (instance == null) {
            instance = new AuthDAO();
        }
        return instance;
    }

    public HashSet<AuthToken> getTokens() {
        return tokens;
    }

    /**
     * Adds the given token to the database
     *
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
     *
     * @param token the given token to delete
     * @throws DataAccessException if given token isn't in database
     */
    public void deleteToken(AuthToken token) throws DataAccessException {
        if (!tokens.contains(token)) {
            throw new DataAccessException("Error: description");
        } else {
            tokens.remove(token);
        }
    }

    /**
     * Finds and retrieves the given token from the database
     *
     * @param token the given token to retrieve
     * @return the found AuthToken
     * @throws DataAccessException if given token isn't in database
     */
    public AuthToken findToken(AuthToken token) throws DataAccessException {
        if (!tokens.contains(token)) {
            throw new DataAccessException("Error: unauthorized");
        }
        for (AuthToken tempToken : tokens) {
            if (tempToken.equals(token)) {
                return tempToken;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    /**
     * Clears all tokens from the database
     */
    public void clearTokens() {
        tokens.clear();
    }
}
