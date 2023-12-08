package ui;

import chess.*;
import client.ChessClient;
import client.InvalidResponseException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameUI extends Repl {

    private static final int BOARD_SIZE_IN_SQUARES = 9;
    private static ChessGameImpl clientGame;

    public GameUI() throws Exception {
        super();
        validLengths.put("move", 3);
        validLengths.put("leave", 1);
        validLengths.put("resign", 1);
        validLengths.put("help", 1);
        validLengths.put("redraw", 1);
        validLengths.put("highlight", 2);
    }

    public static void setClientGame(ChessGameImpl passedClientGame, ChessGame.TeamColor teamColor) {
        clientGame = passedClientGame;
        if (teamColor == null) {
            teamColor = ChessGame.TeamColor.WHITE;
        }
        printBoard(clientGame.getBoard(), teamColor, new HashSet<>());
    }

    public static ChessGameImpl getClientGame() {
        return clientGame;
    }

    public static void printBoard(ChessBoard board, ChessGame.TeamColor teamView, HashSet<ChessPositionImpl> endPositions) {
        printHeader(teamView);
        for (int row = 1; row < BOARD_SIZE_IN_SQUARES; row++) {
            printRowNumber(teamView, row);
            for (int col = 1; col < BOARD_SIZE_IN_SQUARES; col++) {
                ChessPositionImpl position = new ChessPositionImpl(0, 0);
                if (teamView.equals(ChessGame.TeamColor.BLACK)) {
                    position.setRow(row);
                    position.setColumn(BOARD_SIZE_IN_SQUARES - col);
                } else if (teamView.equals(ChessGame.TeamColor.WHITE)) {
                    position.setRow(BOARD_SIZE_IN_SQUARES - row);
                    position.setColumn(col);
                }
                boolean isHighlight = false;
                if (endPositions.contains(position)) {
                    isHighlight = true;
                }
                printSquare(board.getPiece(position), row, col, isHighlight);
            }
            resetBackground();
            printRowNumber(teamView, row);
            System.out.println();
        }
        printHeader(teamView);
    }

    private static void printSquare(ChessPiece chessPiece, int row, int col, boolean isHighlight) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String background = getBackground(row, col, isHighlight);
        setBackground(background);
        out.print(getPieceSequence(chessPiece));
    }

    private static void printRowNumber(ChessGame.TeamColor color, int row) {
        if (color.equals(ChessGame.TeamColor.WHITE)) {
            System.out.print(" " + (BOARD_SIZE_IN_SQUARES - row) + " ");
        } else {
            System.out.print(" " + row + " ");
        }
    }

    private static void printHeader(ChessGame.TeamColor team) {
        if (team.equals(ChessGame.TeamColor.WHITE)) {
            System.out.print(EscapeSequences.EMPTY);
            for (char c = 'a'; c <= 'h'; c++) {
                System.out.print(" " + c + " ");
            }
            System.out.println();
        } else if (team.equals(ChessGame.TeamColor.BLACK)) {
            System.out.print(EscapeSequences.EMPTY);
            for (char c = 'h'; c >= 'a'; c--) {
                System.out.print(" " + c + " ");
            }
            System.out.println();
        }
    }

    private static String getBackground(int row, int column, boolean isHighlight) {
        if (!isHighlight) {
            if (row % 2 == 0) {
                if (column % 2 == 0) {
                    return EscapeSequences.SET_BG_COLOR_DARK_GREY;
                } else {
                    return EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
                }
            } else {
                if (column % 2 == 0) {
                    return EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
                } else {
                    return EscapeSequences.SET_BG_COLOR_DARK_GREY;
                }
            }
        } else {
            return EscapeSequences.SET_BG_COLOR_DARK_GREEN;
        }
    }

    private static String getPieceSequence(ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        ChessGame.TeamColor color = piece.getTeamColor();
        if (color.equals(ChessGame.TeamColor.WHITE)) {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
        }
        if (color.equals(ChessGame.TeamColor.BLACK)) {
            return switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        }
        return null;
    }

    public void run(ChessGame.TeamColor teamColor, ChessClient.ClientState quitState) throws InterruptedException {
        while (clientGame == null) {
            TimeUnit.MILLISECONDS.sleep(400);
        }
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (true) {
            String selection = scanner.nextLine();
            try {
                result = eval(selection, quitState);

                if (result.equals("quit")) {
                    client.setState(ChessClient.ClientState.LOGGED_IN);
                    break;
                } else if (result.contains("redraw") || result.contains("highlight")) {
                    redrawOrHighlight(result);
                } else if (result.equals("help")) {
                    System.out.println(help());
                }
            } catch (InvalidResponseException e) {
                System.out.println(e.getMessage());
                System.out.println(help());
            }
        }
    }

    private void redrawOrHighlight(String selection) throws InvalidResponseException {
        String[] params = selection.split(" ");
        ArrayList<String> stringParams = new ArrayList<>(Arrays.asList(params));
        String command = params[0];
        if (command.equals("redraw")) {
            if (stringParams.size() > 2) {
                throw new InvalidResponseException("Error: invalid command. Try again");
            }
            redraw(stringParams.get(1));
        } else if (command.equals("highlight")) {
            if (stringParams.size() > 3) {
                throw new InvalidResponseException("Error: invalid command. Try again");
            }
            highlight(stringParams);
        }
    }

    private void highlight(ArrayList<String> params) throws InvalidResponseException {
        String stringColor = params.get(1);
        ChessGame.TeamColor drawColor = null;
        if (stringColor.equals("WHITE")) {
            drawColor = ChessGame.TeamColor.WHITE;
        } else {
            drawColor = ChessGame.TeamColor.BLACK;
        }

        ChessPositionImpl position = ChessClient.getMoveHelper(params.get(2));
        ChessGame.TeamColor pieceColor = clientGame.getBoard().getPiece(position).getTeamColor();
        if (!clientGame.getTeamTurn().equals(pieceColor)) {
            throw new InvalidResponseException("Error: it is not this team's turn, can't highlight moves");
        }

        try {
            HashSet<ChessMove> validMoves = new HashSet<>(clientGame.validMoves(position));
            HashSet<ChessPositionImpl> endPositions = getEndPositions(validMoves);
            printBoard(clientGame.getBoard(), drawColor, endPositions);
        } catch (NullPointerException e) {
            throw new InvalidResponseException("Error: no piece where you wanted to highlight");
        }
    }

    private HashSet<ChessPositionImpl> getEndPositions(HashSet<ChessMove> moves) {
        HashSet<ChessPositionImpl> endPositions = new HashSet<>();
        for (ChessMove move : moves) {
            endPositions.add((ChessPositionImpl) move.getEndPosition());
        }
        return endPositions;
    }

    private void redraw(String stringColor) {
        ChessGame.TeamColor drawColor = null;
        if (stringColor.equals("WHITE")) {
            drawColor = ChessGame.TeamColor.WHITE;
        } else {
            drawColor = ChessGame.TeamColor.BLACK;
        }
        printBoard(clientGame.getBoard(), drawColor, new HashSet<>());
    }

    private String help() {
        return """
                move <startColRow> <endColRow> - make a move
                highlight <colRow> - highlight valid moves for piece
                redraw - redraw board
                resign - forfeit game
                leave - leave game
                help - display command options
                """;
    }

}
