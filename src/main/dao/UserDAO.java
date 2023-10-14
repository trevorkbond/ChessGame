package dao;

import dataAccess.DataAccessException;
import models.User;

import java.util.HashSet;

/**
 * UserDAO is responsible for handling and retrieving the database's Users
 */

public class UserDAO {

    /**
     * The set of Users in the database
     */
    private HashSet<User> users;

    /**
     * Constructor for a UserDAO
     * @param users the given set of users
     */
    public UserDAO(HashSet<User> users) {
        this.users = users;
    }

    /**
     * Adds a user to the database
     * @param user the given User to add
     * @throws DataAccessException if another User with same username exists in database
     */
    public void createUser(User user) throws DataAccessException {

    }

    /**
     * Gets a user with the given username from the database
     * @param username the given username
     * @throws DataAccessException if User with given username isn't in database
     * @return the found User
     */
    public User findUser(String username) throws DataAccessException {
        return null;
    }

    /**
     * Deletes the user with the given username from the database
     * @param username the given username
     * @throws DataAccessException if User with given username isn't in database
     */
    public void deleteUser(String username) throws DataAccessException {

    }

    /**
     * If User exists with username matching passed in User, all of found User's information is updated to that of passed
     * in user
     * @param user the passed in User with desired information to update to
     * @throws DataAccessException if User with given username isn't in database
     */
    public void updateUser(User user) throws DataAccessException{

    }
}
