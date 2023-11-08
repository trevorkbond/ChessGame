package chess;

import java.util.ArrayList;

public class Pawn extends ChessPieceImpl {

    public Pawn(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }

    @Override
    public ArrayList<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPositionImpl position = (ChessPositionImpl) myPosition;
        ArrayList<ChessMove> moves = pieceMovesHelper(board, position, getTeamColor(), 1);
        return moves;
    }

    private ArrayList<ChessMove> pieceMovesHelper(ChessBoard board, ChessPositionImpl position, ChessGame.TeamColor color, int rowMove) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int notMovedRow = 2; // this will change if we find it is actually a black piece
        int colMove = 1;
        int promoRow = 8;
        if (color == ChessGame.TeamColor.BLACK) {
            rowMove = -1 * rowMove;
            notMovedRow = 7;
            promoRow = 1;
        }
        // single moves, check for being blocked
        if (position.getVerticalMove(rowMove) != null) {
            if (board.getPiece(position.getVerticalMove(rowMove)) == null) {
                if (position.getVerticalMove(rowMove).getRow() == promoRow) { // first check if on promotion threshold
                    moves.add(new ChessMoveImpl(position, position.getVerticalMove(rowMove), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMoveImpl(position, position.getVerticalMove(rowMove), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMoveImpl(position, position.getVerticalMove(rowMove), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMoveImpl(position, position.getVerticalMove(rowMove), ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMoveImpl(position, position.getVerticalMove(rowMove), null));
                }
            }
        }
        // double moves, check for being in correct position (haven't moved) then not being blocked
        if (position.getRow() == notMovedRow) {
            if (board.getPiece(position.getVerticalMove(rowMove * 2)) == null && board.getPiece(position.getVerticalMove(rowMove)) == null) {
                moves.add(new ChessMoveImpl(position, position.getVerticalMove(rowMove * 2), null));
            }
        }
        // capture moves
        capturePromoHelper(board, position, color, rowMove, moves, colMove, promoRow);
        colMove *= -1;
        capturePromoHelper(board, position, color, rowMove, moves, colMove, promoRow);
        return moves;
    }

    private void capturePromoHelper(ChessBoard board, ChessPositionImpl position, ChessGame.TeamColor color, int rowMove, ArrayList<ChessMove> moves, int colMove, int promoRow) {
        if (position.getMixedMove(rowMove, colMove) != null) {
            if (board.getPiece(position.getMixedMove(rowMove, colMove)) != null && board.getPiece(position.getMixedMove(rowMove, colMove)).getTeamColor() != color) {
                if (position.getVerticalMove(rowMove).getRow() == promoRow) { // if on promotion threshold
                    moves.add(new ChessMoveImpl(position, position.getMixedMove(rowMove, colMove), PieceType.BISHOP));
                    moves.add(new ChessMoveImpl(position, position.getMixedMove(rowMove, colMove), PieceType.QUEEN));
                    moves.add(new ChessMoveImpl(position, position.getMixedMove(rowMove, colMove), PieceType.ROOK));
                    moves.add(new ChessMoveImpl(position, position.getMixedMove(rowMove, colMove), PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMoveImpl(position, position.getMixedMove(rowMove, colMove), null));
                }
            }
        }
    }
}
