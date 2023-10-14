package chess;

import java.util.ArrayList;

public class King extends ChessPieceImpl {

    public King(ChessGame.TeamColor color, PieceType type) {
        super(color, type);
    }

    @Override
    public ArrayList<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPositionImpl position = (ChessPositionImpl) myPosition;
        return getMoves(board, position, getTeamColor());
    }
    private ArrayList<ChessMove> getMoves(ChessBoard board, ChessPositionImpl position, ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0)) {
                    if (!nullOrTeammate(board, position, color, i, j)) {
                        moves.add(new ChessMoveImpl(position, position.getMixedMove(i, j), null));
                    }
                }
            }
        }
        return moves;
    }
    private boolean nullOrTeammate(ChessBoard board, ChessPositionImpl position, ChessGame.TeamColor color, int rowMove, int colMove) {
        ChessPositionImpl newPosition = (ChessPositionImpl) position.getMixedMove(rowMove, colMove);
        if (newPosition == null) {
            return true;
        } else if (board.getPiece(newPosition) == null) {
            return false;
        } else if (board.getPiece(newPosition).getTeamColor() != color) {
            return false;
        } else {
            return true;
        }
    }
}
