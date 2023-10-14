package models;

/**
 * User represents a User or player of a chess game
 */
public class User {
    private String username;
    private String password;
    private String email;

    /**
     * Constructor for a User
     * @param username, the given username
     * @param password, the given password
     * @param email, the given email
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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
