import client.ChessClient;
import ui.PreloginRepl;
import ui.Repl;

public class Main {

    public static void main(String[] args) {
        PreloginRepl prelogin = new PreloginRepl();
        ChessClient client = ChessClient.getInstance();
        while (!client.getState().equals(ChessClient.ClientState.QUIT)) {
            if (client.getState().equals(ChessClient.ClientState.LOGGED_OUT)) {
                prelogin.run();
            } if (client.getState().equals(ChessClient.ClientState.LOGGED_IN)) {
                //TODO: implement postlogin and have it run here
            }
        }
        System.out.println("Goodbye!");
    }
}
