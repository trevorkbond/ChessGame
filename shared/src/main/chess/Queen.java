package chess;

import java.util.ArrayList;

public class Queen extends ChessPieceImpl {
    public Queen(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }

    @Override
    public ArrayList<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPositionImpl position = (ChessPositionImpl) myPosition;
        ArrayList<ChessMove> moves = getMoves(board, position, getTeamColor());
        return moves;
    }

    private ArrayList<ChessMove> getMoves(ChessBoard board, ChessPositionImpl position, ChessGame.TeamColor color) {
        // all possibilities with rows increasing
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(moveUntil(board, position, color, 1, 0));
        moves.addAll(moveUntil(board, position, color, -1, 0));
        moves.addAll(moveUntil(board, position, color, 0, -1));
        moves.addAll(moveUntil(board, position, color, 0, 1));
        moves.addAll(moveUntil(board, position, color, 1, 1));
        moves.addAll(moveUntil(board, position, color, -1, 1));
        moves.addAll(moveUntil(board, position, color, 1, -1));
        moves.addAll(moveUntil(board, position, color, -1, -1));
        return moves;
    }
}
