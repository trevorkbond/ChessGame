package result;

/**
 * CreateGameResult represents the result of a create game API call, returning the gameID
 */
public class CreateGameResult extends Result {
    /**
     * An integer that represents the gameID of the result
     */
    private final int gameID;

    /**
     * Constructor for a CreateGameResult
     *
     * @param message, the given error message
     * @param gameID,  the given integer to make the gameID from
     */
    public CreateGameResult(String message, int gameID) {
        super(message);
        this.gameID = gameID;
    }

}
