package services.request;

/**
 * LoginRequest represents a login request object containing a username and password
 */
public class LoginRequest {
    /**
     * The username of the login User
     */
    private String username;

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * The password of the login User
     */
    private String password;

    /**
     * Constructor for a LoginRequest
     *
     * @param username the given username
     * @param password the given password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
}
