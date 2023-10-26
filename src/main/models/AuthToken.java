package models;

import java.util.Objects;
import java.util.UUID;

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

    /**
     * Constructor for an AuthToken that generates random authToken automatically
     *
     * @param username the given username
     */
    public AuthToken(String username) {
        this.username = username;
        authToken = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthToken authToken1)) return false;
        return Objects.equals(getAuthToken(), authToken1.getAuthToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthToken());
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

}
