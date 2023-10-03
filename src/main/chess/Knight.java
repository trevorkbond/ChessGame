package chess;

import chess.*;

import java.util.ArrayList;

public class Knight extends ChessPieceImpl {

    public Knight(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }

    @Override
    public ArrayList<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        System.out.println("Current position is " + myPosition.toString());
        System.out.println("Pawn's color is: " + getTeamColor().toString());
        ChessPositionImpl position = (ChessPositionImpl) myPosition;
        ArrayList<ChessMove> moves = pieceMovesHelper(board, position, getTeamColor(), 2, 1);
        moves.addAll(pieceMovesHelper(board, position, getTeamColor(), 2, -1));
        moves.addAll(pieceMovesHelper(board, position, getTeamColor(), 1, 2));
        moves.addAll(pieceMovesHelper(board, position, getTeamColor(), 1, -2));
        moves.addAll(pieceMovesHelper(board, position, getTeamColor(), -1, 2));
        moves.addAll(pieceMovesHelper(board, position, getTeamColor(), -1, -2));
        moves.addAll(pieceMovesHelper(board, position, getTeamColor(), -2, 1));
        moves.addAll(pieceMovesHelper(board, position, getTeamColor(), -2, -1));
        System.out.println("pieceMoves returning: " + moves);
        return moves;
    }

    private ArrayList<ChessMove> pieceMovesHelper(ChessBoard board, ChessPositionImpl position, ChessGame.TeamColor teamColor, int rowMove, int colMove) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (position.getMixedMove(rowMove, colMove) != null) {
            if (board.getPiece(position.getMixedMove(rowMove, colMove)) == null) {
                moves.add(new ChessMoveImpl(position, position.getMixedMove(rowMove, colMove), null));
            } else if (board.getPiece(position.getMixedMove(rowMove, colMove)).getTeamColor() != teamColor) {
                moves.add(new ChessMoveImpl(position, position.getMixedMove(rowMove, colMove), null));
            }
        }
        return moves;
    }
}
