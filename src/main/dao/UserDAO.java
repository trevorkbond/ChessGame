package dao;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import dataAccess.DataAccessException;
import models.User;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.HashSet;

/**
 * UserDAO is responsible for handling and retrieving the database's Users
 */

public class UserDAO extends DAO {

    private Connection connection;

    /**
     * Default constructor for a UserDAO with no parameters private to ensure no direct instantiation
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a user to the database
     *
     * @param user the given User to add
     * @throws DataAccessException if another User with same username exists in database
     */
    public void createUser(User user) throws DataAccessException {
        String insertSQL = "insert into user (username, password, email) values (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }


    /**
     * Gets a user with the given username from the database
     *
     * @param username the given username
     * @return the found User
     * @throws DataAccessException if User with given username isn't in database
     */
    public User findUser(String username) throws DataAccessException {
        String selectSQL = "select * from user where username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String foundUsername = rs.getString(1);
                String foundPassword = rs.getString(2);
                String foundEmail = rs.getString(3);

                return new User(foundUsername, foundPassword, foundEmail);
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    /**
     * Clears all users from the database
     */
    public void clearUsers() throws DataAccessException {
        String dropSQL = "delete from user";
        try (PreparedStatement stmt = connection.prepareStatement(dropSQL)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    /**
     * This function returns a set of all users from the database for testing purposes.
     * @return A set of all users
     * @throws DataAccessException if there's an SQL exception
     */
    public HashSet<User> getAllUsers() throws DataAccessException {
        String selectSQL = "select * from user";
        HashSet<User> users = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String username = rs.getString(1);
                String password = rs.getString(2);
                String email = rs.getString(3);

                users.add(new User(username, password, email));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return users;
    }
}
