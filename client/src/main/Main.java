import ui.PreloginRepl;
import ui.Repl;

public class Main {

    public static void main(String[] args) {
        PreloginRepl prelogin = new PreloginRepl();
        prelogin.run();
        if (prelogin.getState() == Repl.ClientState.LOGGED_IN) {
            System.out.println("you logged in good job");
        } else {
            System.out.println("have a nice day goodbye");
        }
    }
}
