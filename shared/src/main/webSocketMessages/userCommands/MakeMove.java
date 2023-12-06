package webSocketMessages.userCommands;

import chess.ChessMove;
import chess.ChessMoveImpl;

public class MakeMove extends UserGameCommand {
    private int gameID;
    private ChessMoveImpl chessMove;
    private String username;

    public String getUsername() {
        return username;
    }

    public MakeMove(String authToken, int gameID, ChessMoveImpl chessMove, String username, CommandType type) {
        super(authToken);
        this.gameID = gameID;
        this.chessMove = chessMove;
        this.username = username;
        this.commandType = type;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getChessMove() {
        return chessMove;
    }


}
