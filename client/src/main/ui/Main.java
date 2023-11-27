package ui;

import client.ChessClient;

public class Main {

    public static void main(String[] args) {
        PreloginRepl prelogin = new PreloginRepl();
        PostLoginRepl postlogin = new PostLoginRepl();
        ChessClient client = ChessClient.getInstance();
        InGameRepl inGame = new InGameRepl();
        while (!client.getState().equals(ChessClient.ClientState.QUIT)) {
            if (client.getState().equals(ChessClient.ClientState.LOGGED_OUT)) {
                prelogin.run(ChessClient.ClientState.QUIT, "Welcome to ChessGame. Please sign in or register to play.\n");
            } else if (client.getState().equals(ChessClient.ClientState.LOGGED_IN)) {
                postlogin.run(ChessClient.ClientState.LOGGED_OUT, "Please select from the following commands.\n");
            } else if (client.getState().equals(ChessClient.ClientState.IN_GAME)) {
                inGame.run();
            }
        }
        System.out.println("Goodbye!");
    }

}
