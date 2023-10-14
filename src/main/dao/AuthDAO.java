package dao;

import dataAccess.DataAccessException;
import models.AuthToken;

import java.util.HashSet;

/**
 * AuthDAO is responsible for handling and retrieving the database's AuthTokens
 */

public class AuthDAO {

    /**
     * The set of AuthTokens in the database
     */
    private HashSet<AuthToken> tokens;

    /**
     * Constructor for an AuthDAO
     * @param tokens the given set of tokens
     */
    public AuthDAO(HashSet<AuthToken> tokens) {
        this.tokens = tokens;
    }

    /**
     * Adds the given token to the database
     * @param token the given AuthToken
     * @throws DataAccessException if given token already in database
     */
    public void addToken(AuthToken token) throws DataAccessException {

    }

    /**
     * Deletes the given token from the database
     * @param token the given token to delete
     * @throws DataAccessException if given token isn't in database
     */
    public void deleteToken(AuthToken token) throws DataAccessException {

    }
}
