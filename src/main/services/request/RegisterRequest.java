package services.request;

/**
 * RegisterRequest extends from LoginRequest as the only new unique field is an email
 */
public class RegisterRequest extends LoginRequest {
    /**
     * The given login email
     */
    private String email;

    @Override
    public String toString() {
        return super.toString() + "\nRegisterRequest{" +
                "email='" + email + '\'' +
                '}';
    }

    /**
     * Constructor for a RegisterRequest
     * @param username the given username
     * @param password the given password
     * @param email the given email
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
