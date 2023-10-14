package services.request;

/**
 * CreateGameRequest represents a request to create a game
 */
public class CreateGameRequest {
    private String gameName;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Constructor for a CreateGameRequest
     * @param gameName, the given name to assign to the game
     */
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }
}
