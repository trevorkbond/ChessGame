package services.request;

import chess.ChessGame;

/**
 * JoinGameRequest represents a request to join a game
 */
public class JoinGameRequest {
    /**
     * The playerColor specified in the request
     */
    private final ChessGame.TeamColor playerColor;
    /**
     * The gameID specified in the request
     */
    private final int gameID;

    /**
     * Constructor for a JoinGameRequest
     *
     * @param playerColor the given color of type TeamColor
     * @param gameID      the given game's ID
     */
    public JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

}
