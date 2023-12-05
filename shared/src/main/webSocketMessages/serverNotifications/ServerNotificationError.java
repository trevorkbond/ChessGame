package webSocketMessages.serverNotifications;

import webSocketMessages.serverMessages.ServerMessage;

public class ServerNotificationError extends ServerMessage {
    private String message;
    public ServerNotificationError(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
}
