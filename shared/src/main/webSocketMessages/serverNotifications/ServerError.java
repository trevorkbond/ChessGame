package webSocketMessages.serverNotifications;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerError extends ServerMessage {
    private String errorMessage;

    public ServerError(ServerMessageType type, String message) {
        super(type);
        errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
