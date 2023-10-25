package services.request;

/**
 * CreateGameRequest represents a request to create a game
 */
public class CreateGameRequest {
    /**
     * The game's name, a String
     */
    private String gameName;

    /**
     * Constructor for a CreateGameRequest
     *
     * @param gameName the given name to assign to the game
     */
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
