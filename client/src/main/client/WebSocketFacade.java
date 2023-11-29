package client;

import org.eclipse.jetty.websocket.api.annotations.*;
import ui.Repl;
import webSocketMessages.userCommands.JoinPlayer;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@WebSocket
public class WebSocketFacade extends Endpoint {
    private Session session;

    public WebSocketFacade() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println("Client heard this: " + message);
            }
        });
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
    }

    @OnWebSocketClose
    public void onClose(Session session) {
    }

    @OnWebSocketError
    public void onError(Throwable t) {
    }

    public void joinPlayer(JoinPlayer message) throws IOException {
        this.session.getBasicRemote().sendText(Repl.objectToJson(message));
    }

    public void sendMessage() throws IOException {
        this.session.getBasicRemote().sendText("This is a message");
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
