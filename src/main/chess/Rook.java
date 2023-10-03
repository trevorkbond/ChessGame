package chess;

import java.util.ArrayList;

public class Rook extends ChessPieceImpl {
    public Rook(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }

    @Override
    public ArrayList<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPositionImpl position = (ChessPositionImpl) myPosition;
        return getMoves(board, position, getTeamColor());
    }
    private ArrayList<ChessMove> getMoves(ChessBoard board, ChessPositionImpl position, ChessGame.TeamColor color) {
        // all possibilities with rows increasing
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(moveUntil(board, position, color, 1, 0));
        moves.addAll(moveUntil(board, position, color, -1, 0));
        moves.addAll(moveUntil(board, position, color, 0, -1));
        moves.addAll(moveUntil(board, position, color, 0, 1));
        return moves;
    }
}
