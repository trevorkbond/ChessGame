package services.request;

import chess.ChessGame;

/**
 * JoinGameRequest represents a request to join a game
 */
public class JoinGameRequest {
    private ChessGame.TeamColor playerColor;
    private int gameID;

    /**
     * Constructor for a JoinGameRequest
     * @param playerColor, the given color of type TeamColor
     * @param gameID, the given game's ID
     */
    public JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
