package ui;

public class Repl {

    private ClientState state;

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public enum ClientState {
        LOGGED_OUT,
        LOGGED_IN
    }
}
