package services.request;

/**
 * RegisterRequest extends from LoginRequest as the only new unique field is an email
 */
public class RegisterRequest extends LoginRequest {
    private String email;

    /**
     * Constructor for a RegisterRequest
     * @param username, the given username
     * @param password, the given password
     * @param email, the given email
     */
    public RegisterRequest(String username, String password, String email) {
        super(username, password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
