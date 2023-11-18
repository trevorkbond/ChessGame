package client;

import request.RegisterRequest;
import result.LoginRegisterResult;

import java.io.IOException;
import java.util.ArrayList;

public class ChessClient {
    private final ServerFacade serverFacade;

    public ChessClient() {
        serverFacade = new ServerFacade();
    }

    public String executeCommand(String command, ArrayList<String> params) throws IOException {
        return switch (command) {
            case "register" -> register(params);
            default -> throw new IllegalStateException("Unexpected value: " + command);
        };
    }

    public String register(ArrayList<String> params) throws IOException {
        String username = params.get(0);
        String password = params.get(1);
        String email = params.get(2);
        RegisterRequest request = new RegisterRequest(username, password, email);
        LoginRegisterResult result = serverFacade.register(request);
        return String.format("You have logged in as %s.", result.getUsername());
    }

}
