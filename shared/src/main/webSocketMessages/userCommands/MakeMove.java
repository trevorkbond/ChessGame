package webSocketMessages.userCommands;

import chess.ChessGameImpl;
import chess.ChessMove;
import chess.ChessMoveImpl;
import models.User;

public class MakeMove extends UserGameCommand {
    private int gameID;
    private ChessMoveImpl chessMove;

    public int getGameID() {
        return gameID;
    }

    public ChessMoveImpl getChessMove() {
        return chessMove;
    }

    public MakeMove(String authToken, int gameID, ChessMoveImpl chessMove) {
        super(authToken);
        this.gameID = gameID;
        this.chessMove = chessMove;
    }




}
