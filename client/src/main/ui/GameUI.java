package ui;

import chess.*;
import client.ChessClient;
import client.InvalidResponseException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameUI extends Repl {

    private static final int BOARD_SIZE_IN_SQUARES = 9;
    private static ChessGameImpl clientGame;

    public GameUI() throws Exception {
        super();
        validLengths.put("move", 3);
        validLengths.put("quit", 1);
        validLengths.put("resign", 1);
    }

    public static void setClientGame(ChessGameImpl passedClientGame, ChessGame.TeamColor teamColor) {
        clientGame = passedClientGame;
        printBoard(clientGame.getBoard(), teamColor);
    }

    public static ChessGameImpl getClientGame() {
        return clientGame;
    }

    public static void printBoard(ChessBoard board, ChessGame.TeamColor teamView) {
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
                printSquare(board.getPiece(position), row, col);
            }
            resetBackground();
            printRowNumber(teamView, row);
            System.out.println();
        }
        printHeader(teamView);
    }

    private static void printSquare(ChessPiece chessPiece, int row, int col) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String background = getBackground(row, col);
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

    private static String getBackground(int row, int column) {
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
                }
            } catch (InvalidResponseException e) {
                System.out.println(e.getMessage());
                System.out.println(help());
            }
        }
    }

    private String help() {
        return "TODO: implement help in GameUI";
    }

}
