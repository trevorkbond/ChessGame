package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import java.io.IOException;

@WebSocket
public class WebSocketHandler extends Endpoint {
    private WebSocketSessions webSocketSessions = new WebSocketSessions();

    @OnWebSocketClose
    public void onClose(Session session, int errorCode, String msg) {
        webSocketSessions.removeSession(session);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Server WS heard this message: " + message);
        session.getRemote().sendString("WebSocket response: " + message);
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {

    }
}
