package services.result;

/**
 * CreateGameResult represents the result of a create game API call, returning the gameID
 */
public class CreateGameResult {
    private int gameID;

    /**
     * Constructor for a CreateGameResult
     * @param gameID, the given integer to make the gameID from
     */
    public CreateGameResult(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
