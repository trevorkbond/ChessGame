package models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthToken authToken1)) return false;
        return Objects.equals(getUsername(), authToken1.getUsername()) && Objects.equals(getAuthToken(), authToken1.getAuthToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAuthToken());
    }

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
