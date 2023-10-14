package models;

import chess.ChessGame;

/**
 * Game represents a chess game that has been played or is being played. Each game has a unique game ID and contains
 * information about the teams, along with a Game name
 */
public class Game extends services.result.GameResult{
    private ChessGame game;

    /**
     * A constructor for a Game model
     *
     * @param gameID,        the given gameID (int)
     * @param whiteUsername, the username of the white team User
     * @param blackUsername, the username of the black team User
     * @param gameName,      the game name (String)
     * @param game,          the given ChessGame object
     */
    public Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        super(gameID, whiteUsername, blackUsername, gameName);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}
