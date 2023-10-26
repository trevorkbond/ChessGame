package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ChessPieceImpl implements chess.ChessPiece {
    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPieceImpl(ChessGame.TeamColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    public ArrayList<ChessMove> moveUntil(ChessBoard board, ChessPositionImpl position, ChessGame.TeamColor color, int rowMove, int colMove) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPositionImpl ogPosition = position;
        while (position.getMixedMove(rowMove, colMove) != null) {
            if (board.getPiece(position.getMixedMove(rowMove, colMove)) == null) {
                position = (ChessPositionImpl) position.getMixedMove(rowMove, colMove);
                moves.add(new ChessMoveImpl(ogPosition, position, null));
            } else if (board.getPiece(position.getMixedMove(rowMove, colMove)).getTeamColor() != color) {
                position = (ChessPositionImpl) position.getMixedMove(rowMove, colMove);
                moves.add(new ChessMoveImpl(ogPosition, position, null));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    @Override
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
