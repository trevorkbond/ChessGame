package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    private int gameID;

    public Resign(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.RESIGN;
    }

    public int getGameID() {
        return gameID;
    }
}
