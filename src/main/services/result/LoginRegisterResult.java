package services.result;

/**
 * LoginRegisterResult represents a login result or register result object containing a username and authToken (Strings).
 * These are shared because the response body of the login and register API is the same.
 */
public class LoginRegisterResult extends Result {
    private String authToken;
    private String username;

    /**
     * Constructor for a LoginRegisterResult
     *
     * @param message,   the given error message
     * @param authToken, the given authToken
     * @param username,  the given username
     */
    public LoginRegisterResult(String message, String authToken, String username) {
        super(message);
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
