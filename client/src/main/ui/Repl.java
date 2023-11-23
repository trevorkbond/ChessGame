package ui;

import client.ChessClient;

public class Repl {

    private ChessClient client;

    public Repl() {
        client = ChessClient.getInstance();
    }

    public void setInfoPrinting() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
    }

    public void resetBackground() {
        System.out.print("\u001b[0m");
    }

    public void setBackground(String background) {
        System.out.print(background);
    }

    public void setInputPrinting() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "[" + client.getState() + "]>>> ");
    }

}
