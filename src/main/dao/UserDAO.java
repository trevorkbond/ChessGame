package dao;

import dataAccess.DataAccessException;
import models.User;

import java.util.HashSet;

/**
 * UserDAO is responsible for handling and retrieving the database's Users
 */

public class UserDAO {

    /**
     * Employing Singleton method to ensure only one UserDAO is ever created
     */
    private static UserDAO instance;
    /**
     * The set of Users in the database
     */
    private HashSet<User> users;

    /**
     * Default constructor for a UserDAO with no parameters private to ensure no direct instantiation
     */
    private UserDAO() {
        users = new HashSet<>();
    }

    /**
     * getInstance ensures only one userDAO is ever created
     *
     * @return the sole instance of userDAO
     */
    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    /**
     * Adds a user to the database
     *
     * @param user the given User to add
     * @throws DataAccessException if another User with same username exists in database
     */
    public void createUser(User user) throws DataAccessException {
        if (users.contains(user)) {
            throw new DataAccessException("Error: already taken");
        }
        users.add(user);
    }

    public HashSet<User> getUsers() {
        return users;
    }

    /**
     * Gets a user with the given username from the database
     *
     * @param username the given username
     * @return the found User
     * @throws DataAccessException if User with given username isn't in database
     */
    public User findUser(String username) throws DataAccessException {
        User tempUser = new User(username, null, null);
        if (!users.contains(tempUser)) {
            throw new DataAccessException("Error: unauthorized");
        }
        for (User user : users) {
            if (user.equals(tempUser)) {
                return user;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    /**
     * Deletes the user with the given username from the database
     *
     * @param username the given username
     * @throws DataAccessException if User with given username isn't in database
     */
    public void deleteUser(String username) throws DataAccessException {

    }

    /**
     * If User exists with username matching passed in User, all of found User's information is updated to that of passed
     * in user
     *
     * @param user the passed in User with desired information to update to
     * @throws DataAccessException if User with given username isn't in database
     */
    public void updateUser(User user) throws DataAccessException {

    }

    /**
     * Clears all users from the database
     */
    public void clearUsers() throws DataAccessException {
        users.clear();
    }
}
