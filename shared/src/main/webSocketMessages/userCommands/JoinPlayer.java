package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

    private int gameID;
    private ChessGame.TeamColor teamColor;

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor) {
        super(authToken);
        this.gameID = gameID;
        this.teamColor = teamColor;
        commandType = CommandType.JOIN_PLAYER;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }


}
