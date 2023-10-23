package models;

import services.request.RegisterRequest;

import java.util.Objects;

/**
 * User represents a User or player of a chess game
 */
public class User {
    /**
     * The user's username
     */
    private String username;
    /**
     * The user's password
     */
    private String password;
    /**
     * The user's email
     */
    private String email;

    /**
     * Constructor for a User
     *
     * @param username the given username
     * @param password the given password
     * @param email    the given email
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Constructor for a User passing in a RegisterRequest
     *
     * @param request which contains same parameters as a User
     */
    public User(RegisterRequest request) {
        username = request.getUsername();
        password = request.getPassword();
        email = request.getEmail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
