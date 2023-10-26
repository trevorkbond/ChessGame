package services.request;

/**
 * LoginRequest represents a login request object containing a username and password
 */
public class LoginRequest {
    /**
     * The username of the login User
     */
    private String username;
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

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
