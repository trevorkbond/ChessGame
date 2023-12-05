package client;

import chess.ChessBoardImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ChessBoardAdapter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ui.Repl;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverNotifications.LoadGame;
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
                System.out.println("client heard: " + message);
                ServerMessage serverMessage = getMessage(message);
                System.out.println("made it out");
                switch (serverMessage.getServerMessageType()) {
                    case LOAD_GAME -> loadGame((LoadGame) serverMessage);
                    case NOTIFICATION -> System.out.println(message);
                }
            }
        });
    }

    private ServerMessage getMessage(String message) {
        ServerMessage serverMessage = stringToObject(message, ServerMessage.class);
        Class desiredClass = getDesiredClass(serverMessage);
        return stringToObject(message, desiredClass);
    }

    private ServerMessage stringToObject(String message, Class desiredClass) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoardImpl.class, new ChessBoardAdapter());
        Gson gson = builder.create();
        return (ServerMessage) gson.fromJson(message, desiredClass);
    }

    private Class getDesiredClass(ServerMessage message) {
        return switch (message.getServerMessageType()) {
            case LOAD_GAME -> LoadGame.class;
            case ERROR -> null;
            case NOTIFICATION -> null;
        };
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

    public void joinPlayer(JoinPlayer message) throws IOException {
        this.session.getBasicRemote().sendText(Repl.objectToJson(message));
    }

    public void loadGame(LoadGame message) {
        System.out.println("in loadGame");
        client.setClientGame(message.getGame());
    }

    public void sendMessage() throws IOException {
        this.session.getBasicRemote().sendText("This is a message");
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
