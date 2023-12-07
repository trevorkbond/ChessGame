package webSocketMessages.userCommands;

import models.User;

public class Leave extends UserGameCommand {
    private int gameID;

    public int getGameID() {
        return gameID;
    }

    public Leave(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.LEAVE;
    }
}
