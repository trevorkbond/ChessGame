package webSocketMessages.userCommands;

import chess.ChessMove;
import chess.ChessMoveImpl;

public class MakeMove extends UserGameCommand {
    private int gameID;
    private ChessMoveImpl move;
    private String username;

    public MakeMove(String authToken, int gameID, ChessMoveImpl move, String username, CommandType type) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        this.username = username;
        this.commandType = type;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }


}
