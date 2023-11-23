package ui;

import chess.ChessBoardImpl;
import chess.ChessGame;
import chess.ChessPiece;
import chess.Rook;
import client.ChessClient;
import ui.EscapeSequences;
import ui.InGameRepl;
import ui.PreloginRepl;
import ui.Repl;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
//        PreloginRepl prelogin = new PreloginRepl();
//        ChessClient client = ChessClient.getInstance();
//        while (!client.getState().equals(ChessClient.ClientState.QUIT)) {
//            if (client.getState().equals(ChessClient.ClientState.LOGGED_OUT)) {
//                prelogin.run();
//            } if (client.getState().equals(ChessClient.ClientState.LOGGED_IN)) {
//                //TODO: implement postlogin and have it run here
//            }
//        }
//        System.out.println("Goodbye!");
        InGameRepl repl = new InGameRepl();
        ChessBoardImpl board = new ChessBoardImpl();
        board.resetBoard();
        repl.printBoard(board, ChessGame.TeamColor.BLACK);
        repl.printBoard(board, ChessGame.TeamColor.WHITE);
    }

}
