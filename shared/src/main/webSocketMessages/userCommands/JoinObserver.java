package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {
    private int gameID;
    private String username;

    public int getGameID() {
        return gameID;
    }

    public String getUsername() {
        return username;
    }

    public JoinObserver(String authToken, int gameID, String username) {
        super(authToken);
        this.gameID = gameID;
        this.username = username;
        commandType = CommandType.JOIN_OBSERVER;
    }
}
