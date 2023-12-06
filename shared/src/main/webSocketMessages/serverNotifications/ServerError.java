package webSocketMessages.serverNotifications;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerError extends ServerMessage {
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public ServerError(ServerMessageType type, String message) {
        super(type);
        errorMessage = message;
    }
}
