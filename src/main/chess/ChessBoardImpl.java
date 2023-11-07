package chess;

import java.util.Arrays;

public class ChessBoardImpl implements chess.ChessBoard {
    private final ChessPiece[][] boardArray;

    public ChessBoardImpl() {
        boardArray = new ChessPiece[8][8];
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardArray[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public ChessBoardImpl myClone(ChessBoardImpl board) {
        ChessBoardImpl cloneBoard = new ChessBoardImpl();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (board.getPiece(new ChessPositionImpl(i, j)) != null) {
                    ChessPositionImpl position = new ChessPositionImpl(i, j);
                    ChessPiece piece = board.getPiece(position);
                    switch (piece.getPieceType()) {
                        case PAWN ->
                                cloneBoard.addPiece(position, new Pawn(piece.getTeamColor(), ChessPiece.PieceType.PAWN));
                        case ROOK ->
                                cloneBoard.addPiece(position, new Rook(piece.getTeamColor(), ChessPiece.PieceType.ROOK));
                        case BISHOP ->
                                cloneBoard.addPiece(position, new Bishop(piece.getTeamColor(), ChessPiece.PieceType.BISHOP));
                        case KNIGHT ->
                                cloneBoard.addPiece(position, new Knight(piece.getTeamColor(), ChessPiece.PieceType.KNIGHT));
                        case QUEEN ->
                                cloneBoard.addPiece(position, new Queen(piece.getTeamColor(), ChessPiece.PieceType.QUEEN));
                        case KING ->
                                cloneBoard.addPiece(position, new King(piece.getTeamColor(), ChessPiece.PieceType.KING));
                    }
                }
            }
        }
        return cloneBoard;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return boardArray[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (getPiece(new ChessPositionImpl(i, j)) != null) {
                    boardString.append("(").append(i).append(", ").append(j).append(") -> ").append(getPiece(new ChessPositionImpl(i, j)).getPieceType()).append("\n");
                }
            }
        }
        return boardString.toString();
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

    public void removePiece(ChessPosition position) {
        boardArray[position.getRow() - 1][position.getColumn() - 1] = null;
    }
}
