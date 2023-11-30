package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.userCommands.JoinPlayer;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler extends Endpoint {
    private static WebSocketSessions webSocketSessions;

    public WebSocketHandler() {
        webSocketSessions = WebSocketSessions.getInstance();
    }

    public static Object jsonToObject(Class desiredClass, String message) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        return gson.fromJson(message, desiredClass);
    }

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

    private void joinPlayer(Session session, JoinPlayer command) throws IOException {
        webSocketSessions.addSessionToGame(command.getGameID(), command.getUsername(), session);
        String broadcast = command.getUsername() + " joined the game.";
        System.out.println("sessions after join:\n" + webSocketSessions);
        broadcastMessage(command.getGameID(), broadcast, command.getUsername());
    }

    private JoinPlayer getCommand(String message) {
        //FIXME: need to figure out how to deal with other commandTypes
        return (JoinPlayer) jsonToObject(JoinPlayer.class, message);
    }

    private void broadcastMessage(int gameID, String message, String exceptThisUser) throws IOException {
        HashMap<String, Session> sessions = webSocketSessions.getSessionsForGame(gameID);
        for (String user : sessions.keySet()) {
            if (!user.equals(exceptThisUser)) {
                sessions.get(user).getRemote().sendString(message);
            }
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
    }
}
