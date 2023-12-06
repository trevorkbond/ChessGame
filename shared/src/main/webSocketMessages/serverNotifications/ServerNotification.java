package webSocketMessages.serverNotifications;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerNotification extends ServerMessage {
    private String message;

    public ServerNotification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
