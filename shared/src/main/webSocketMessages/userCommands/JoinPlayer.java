package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

    private int gameID;
    private ChessGame.TeamColor teamColor;
    private String username;

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    public String getUsername() {
        return username;
    }

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor, String username) {
        super(authToken);
        this.gameID = gameID;
        this.teamColor = teamColor;
        commandType = CommandType.JOIN_PLAYER;
        this.username = username;
    }


}
