package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import services.GameService;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import java.io.IOException;

@WebSocket
public class WebSocketHandler extends Endpoint {
    private WebSocketSessions webSocketSessions = new WebSocketSessions();

    private GameService gameService = new GameService();

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
        //FIXME: need to figure out how to deal with other commandTypes
        JoinPlayer command = getCommand(message);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, (JoinPlayer) command);
        }
    }

    private void joinPlayer(Session session, JoinPlayer command) {
        webSocketSessions.addSessionToGame(command.getGameID(), command.getUsername(), session);
    }

    private JoinPlayer getCommand(String message) {
        //FIXME: need to figure out how to deal with other commandTypes
        return (JoinPlayer) jsonToObject(JoinPlayer.class, message);
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {

    }

    public static Object jsonToObject(Class desiredClass, String message) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        return gson.fromJson(message, desiredClass);
    }
}
