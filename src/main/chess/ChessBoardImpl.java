package chess;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Arrays;

public class ChessBoardImpl implements chess.ChessBoard {
    private ChessPiece[][] boardArray;
    public ChessBoardImpl() {
        boardArray = new ChessPiece[8][8];
    }
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardArray[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return boardArray[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public void resetBoard() {
        clearBoard();
        // set white pieces
        for (int i = 1; i < 9; i++) {
            ChessPositionImpl temp = new ChessPositionImpl(2, i);
            Pawn pawn = new Pawn(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(temp, pawn);
        }
        addPiece(new ChessPositionImpl(1, 1), new Rook(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPositionImpl(1, 2), new Knight(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPositionImpl(1, 3), new Bishop(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPositionImpl(1, 4), new Queen(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPositionImpl(1, 5), new King(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPositionImpl(1, 6), new Bishop(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPositionImpl(1, 7), new Knight(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPositionImpl(1, 8), new Rook(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        // set black pieces
        for (int i = 1; i < 9; i++) {
            ChessPositionImpl temp = new ChessPositionImpl(7, i);
            Pawn pawn = new Pawn(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(temp, pawn);
        }
        addPiece(new ChessPositionImpl(8, 1), new Rook(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPositionImpl(8, 2), new Knight(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPositionImpl(8, 3), new Bishop(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPositionImpl(8, 4), new Queen(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPositionImpl(8, 5), new King(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPositionImpl(8, 6), new Bishop(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPositionImpl(8, 7), new Knight(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPositionImpl(8, 8), new Rook(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }
    private void clearBoard() {
        for (ChessPiece[] chessPieces : boardArray) {
            Arrays.fill(chessPieces, null);
        }
    }
}
