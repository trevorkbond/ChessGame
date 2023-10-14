package services.result;

/**
 * The Result class is a superclass that is extended on by other Result classes. All results share an error message as a field,
 * so that message (String) is stored here
 */
public abstract class Result {
    private String message;

    /**
     * Result constructor will be used by the other Results as all contain a message
     * @param message, the given error message (String)
     */
    public Result(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
