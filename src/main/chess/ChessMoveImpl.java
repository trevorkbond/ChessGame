package chess;

import java.util.Objects;

public class ChessMoveImpl implements chess.ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMoveImpl(ChessPosition start, ChessPosition end, ChessPiece.PieceType piece) {
        startPosition = start;
        endPosition = end;
        promotionPiece = piece;
    }

    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public String toString() {
        return startPosition.toString() + " -> " + endPosition.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessMoveImpl chessMove)) return false;
        return Objects.equals(getStartPosition(), chessMove.getStartPosition()) && Objects.equals(getEndPosition(), chessMove.getEndPosition()) && getPromotionPiece() == chessMove.getPromotionPiece();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartPosition(), getEndPosition(), getPromotionPiece());
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }
}
