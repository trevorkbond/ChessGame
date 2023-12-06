package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

    private int gameID;
    private ChessGame.TeamColor playerColor;
    private String username;

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor, String username) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = teamColor;
        commandType = CommandType.JOIN_PLAYER;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }


    @Override
    public String toString() {
        return "JoinPlayer{" +
                "gameID=" + gameID +
                ", teamColor=" + playerColor +
                '}';
    }
}
