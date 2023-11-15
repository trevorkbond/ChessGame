package result;

/**
 * LoginRegisterResult represents a login result or register result object containing a username and authToken (Strings).
 * These are shared because the response body of the login and register API is the same.
 */
public class LoginRegisterResult extends Result {
    /**
     * A String representing the resulting userName
     */
    private final String username;
    /**
     * A String representing the resulting authToken
     */
    private final String authToken;

    public String getAuthToken() {
        return authToken;
    }

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

    public String getUsername() {
        return username;
    }
}
