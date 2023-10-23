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
     * Constructor for an AuthDAO
     * @param tokens the given set of tokens
     */
    public AuthDAO(HashSet<AuthToken> tokens) {
        this.tokens = tokens;
    }

    public HashSet<AuthToken> getTokens() {
        return tokens;
    }

    /**
     * Default constructor for AuthDAO with no given hash set
     */
    public AuthDAO() {
        tokens = new HashSet<>();
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
            throw new DataAccessException("The given token isn't in the database");
        } else {
            return token;
        }
    }
}
