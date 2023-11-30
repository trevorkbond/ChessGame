package client;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ui.Repl;
import webSocketMessages.userCommands.JoinPlayer;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@WebSocket
public class WebSocketFacade extends Endpoint {
    private Session session;
    private ChessClient client;

    public WebSocketFacade(ChessClient client) throws Exception {
        setClient(client);
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println("Client heard this: " + message);
            }
        });
    }

    public void setClient(ChessClient client) {
        this.client = client;
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
//
//    @OnWebSocketMessage
//    public void onMessage(Session session, String message) {
//        //FIXME: fix this to actually receive json String and figure out what message type it is.
//        System.out.println(message);
//    }

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
