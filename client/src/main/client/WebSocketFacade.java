package client;

import chess.ChessBoardImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ChessBoardAdapter;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ui.GameUI;
import ui.Repl;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverNotifications.LoadGame;
import webSocketMessages.serverNotifications.ServerError;
import webSocketMessages.serverNotifications.ServerNotification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;

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
                ServerMessage serverMessage = getMessage(message);
                switch (serverMessage.getServerMessageType()) {
                    case LOAD_GAME -> loadGame((LoadGame) serverMessage);
                    case NOTIFICATION -> displayNotification((ServerNotification) serverMessage);
                    case ERROR -> displayError((ServerError) serverMessage);
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
            case NOTIFICATION -> ServerNotification.class;
            case ERROR -> ServerError.class;
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

    public void joinObserver(JoinObserver message) throws IOException {
        this.session.getBasicRemote().sendText(Repl.objectToJson(message));
    }

    public void makeMove(MakeMove message) throws IOException {
        this.session.getBasicRemote().sendText(Repl.objectToJson(message));
    }

    private void loadGame(LoadGame message) {
        GameUI.setClientGame(message.getGame(), ChessClient.getTeamColor());
    }

    private void displayNotification(ServerNotification notification) {
        System.out.println(notification.getMessage());
    }

    private void displayError(ServerError notification) {
        System.out.println(notification.getErrorMessage());
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
