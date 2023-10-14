package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessGameImpl implements ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;
    public ChessGameImpl() {
        board = new ChessBoardImpl();
        teamTurn = TeamColor.WHITE;
    }
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        } else if (isInCheck(board.getPiece(startPosition).getTeamColor())) {
            ArrayList<ChessMove> pieceMoves = (ArrayList<ChessMove>) board.getPiece(startPosition).pieceMoves(board, startPosition);
            ArrayList<ChessMove> validMoves = new ArrayList<>();
            for (ChessMove move : pieceMoves) {
                if (!wouldBeInCheck(move)) {
                    validMoves.add(move);
                }
            }
            return validMoves;
        } else {
            ArrayList<ChessMove> pieceMoves = (ArrayList<ChessMove>) board.getPiece(startPosition).pieceMoves(board, startPosition);
            pieceMoves.removeIf(this::wouldBeInCheck);
            return pieceMoves;
        }
    }

    private boolean wouldBeInCheck(ChessMove move) {
        boolean wouldBeInCheck = false;
        ChessBoardImpl tempBoard = (ChessBoardImpl) board;
        ChessBoardImpl cloneBoard = tempBoard.myClone(tempBoard);
        ChessPiece movePiece = getNewPiece(cloneBoard.getPiece(move.getStartPosition()).getTeamColor(), cloneBoard.getPiece(move.getStartPosition()).getPieceType());
        if (cloneBoard.getPiece(move.getEndPosition()) != null && cloneBoard.getPiece(move.getEndPosition()).getTeamColor() != movePiece.getTeamColor()) {
            cloneBoard.removePiece(move.getEndPosition());
        }
        cloneBoard.addPiece(move.getEndPosition(), movePiece);
        cloneBoard.removePiece(move.getStartPosition());
        if (isInCheck(movePiece.getTeamColor(), cloneBoard)) {
            wouldBeInCheck = true;
        }
        return wouldBeInCheck;
    }
    public boolean isInCheck(TeamColor teamColor, ChessBoard cloneBoard) {
        TeamColor enemyColor = getOtherColor(teamColor);
        ArrayList<ChessMove> enemyMoves = getAllTeamMoves(enemyColor, cloneBoard);
        ChessPosition kingPosition = findKing(teamColor, cloneBoard);
        return kingInEnemyMoves(enemyMoves, kingPosition);
    }

    public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        return switch (type) {
            case PAWN -> new Pawn(pieceColor, type);
            case KNIGHT -> new Knight(pieceColor, type);
            case KING -> new King(pieceColor, type);
            case QUEEN -> new Queen(pieceColor, type);
            case ROOK -> new Rook(pieceColor, type);
            case BISHOP -> new Bishop(pieceColor, type);
        };
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("There isn't a piece at the start position of this move");
        } else {
            TeamColor movingColor = board.getPiece(move.getStartPosition()).getTeamColor();
            if (getTeamTurn() != movingColor) {
                throw new InvalidMoveException("It isn't this team's turn");
            }
            ChessBoardImpl tempBoard = (ChessBoardImpl) board;
            ChessPiece movePiece = board.getPiece(move.getStartPosition());
            ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) movePiece.pieceMoves(board, move.getStartPosition());
            boolean moveInValid = false;
            for (ChessMove validMove : validMoves) {
                if (move.equals(validMove)) {
                    moveInValid = true;
                    break;
                }
            }
            if (!moveInValid) {
                throw new InvalidMoveException("This piece can't move in that way");
            }
            if (move.getPromotionPiece() != null) {
                tempBoard.removePiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), getNewPiece(movingColor, move.getPromotionPiece()));
                tempBoard.removePiece(move.getStartPosition());
            } else {
                tempBoard.removePiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(), movePiece);
                tempBoard.removePiece(move.getStartPosition());
            }
            if (isInCheck(movingColor)) {
                tempBoard.removePiece(move.getEndPosition());
                tempBoard.addPiece(move.getStartPosition(), movePiece);
                throw new InvalidMoveException("Your king will be in check with this move");
            }
            setTeamTurn(getOtherColor(getTeamTurn()));
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemyColor = getOtherColor(teamColor);
        ArrayList<ChessMove> enemyMoves = getAllTeamMoves(enemyColor, board);
        ChessPosition kingPosition = findKing(teamColor, board);
        return kingInEnemyMoves(enemyMoves, kingPosition);
    }
    private TeamColor getOtherColor(TeamColor ogColor) {
        if (ogColor == TeamColor.WHITE) {
            return TeamColor.BLACK;
        } else {
            return TeamColor.WHITE;
        }
    }
    private ArrayList<ChessMove> getAllTeamMoves(TeamColor teamColor, ChessBoard board) {
        ArrayList<ChessPosition> teamPositions = getAllTeamPositions(teamColor);
        ArrayList<ChessMove> enemyMoves = new ArrayList<>();
        for (ChessPosition position : teamPositions) {
            ChessPiece enemyPiece = board.getPiece(position);
            enemyMoves.addAll(enemyPiece.pieceMoves(board, position));
        }
//        System.out.println(enemyMoves);
        return enemyMoves;
    }
    private boolean kingInEnemyMoves(ArrayList<ChessMove> enemyMoves, ChessPosition kingPosition) {
        for (ChessMove move : enemyMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }
    private ChessPosition findKing(TeamColor teamColor, ChessBoard board) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPositionImpl position = new ChessPositionImpl(i,j);
                if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == teamColor && board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
                    return position;
                }
            }
        }
        return null;
    }
    private ArrayList<ChessPosition> getAllTeamPositions(TeamColor teamColor) {
        ArrayList<ChessPosition> teamPositions = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (board.getPiece(new ChessPositionImpl(i, j)) != null && board.getPiece(new ChessPositionImpl(i, j)).getTeamColor() == teamColor) {
                    teamPositions.add(new ChessPositionImpl(i,j));
                }
            }
        }
//        System.out.println(enemyPositions);
        return teamPositions;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        ArrayList<ChessMove> pieceMoves = getAllTeamMoves(teamColor, board);
        ArrayList<ChessMove> validatedMoves = new ArrayList<>();
        for (ChessMove move : pieceMoves) {
            validatedMoves.addAll(validMoves(move.getStartPosition()));
        }
        return (isInCheck(teamColor) && validatedMoves.isEmpty());
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        ArrayList<ChessMove> pieceMoves = getAllTeamMoves(teamColor, board);
        ArrayList<ChessMove> validatedMoves = new ArrayList<>();
        for (ChessMove move : pieceMoves) {
            validatedMoves.addAll(validMoves(move.getStartPosition()));
        }
        return (validatedMoves.isEmpty());
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
