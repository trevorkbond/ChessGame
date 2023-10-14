package models;

/**
 * AuthToken represents an authentication token containing a username and authToken (both Strings)
 */
public class AuthToken {
    /**
     * The user's username
     */
    private String username;
    /**
     * The user's authToken
     */
    private String authToken;

    /**
     * Constructor for an AuthToken
     *
     * @param username  the given username
     * @param authToken the given authToken (as a String)
     */
    public AuthToken(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
