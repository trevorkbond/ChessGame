package services.result;

/**
 * The Result class is a superclass that is extended on by other Result classes. All results share an error message as a field,
 * so that message (String) is stored here
 */
public class Result {
    /**
     * The message of the Result as a string is shared by every Result subobject.
     */
    private final String message;

    /**
     * Result constructor will be used by the other Results as all contain a message
     *
     * @param message, the given error message (String)
     */
    public Result(String message) {
        this.message = message;
    }

}
