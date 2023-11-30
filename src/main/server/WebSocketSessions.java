package server;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;

public class WebSocketSessions {

    private static HashMap<Integer, HashMap<String, Session>> sessionMap;
    private static WebSocketSessions instance;
    public static WebSocketSessions getInstance() {
        if (instance == null) {
            instance = new WebSocketSessions();
        }
        return instance;
    }

    private WebSocketSessions() {
        sessionMap = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder mapString = new StringBuilder("sessionMap: ");
        for (Integer gameID : sessionMap.keySet()) {
            mapString.append("gameID: ").append(gameID);
            mapString.append(", usernames: ").append(sessionMap.get(gameID).keySet()).append("\n");
        }
        mapString.append("size: ").append(sessionMap.size());
        return mapString.toString();
    }

    public void addSessionToGame(int gameID, String username, Session session) {
        if (sessionMap.containsKey(gameID)) {
            HashMap<String, Session> innerMap = sessionMap.get(gameID);
            innerMap.put(username, session);
        } else {
            HashMap<String, Session> innerMap = new HashMap<>();
            innerMap.put(username, session);
            sessionMap.put(gameID, innerMap);
        }
    }

    public void removeSessionFromGame(int gameID, String username) {
        sessionMap.get(gameID).remove(username);
    }

    public void removeSession(Session session) {
        Integer foundID = null;
        String foundUsername = null;
        for (Integer gameID : sessionMap.keySet()) {
            for (String username : sessionMap.get(gameID).keySet()) {
                if (sessionMap.get(gameID).get(username).equals(session)) {
                    foundID = gameID;
                    foundUsername = username;
                }
            }
        }
        sessionMap.get(foundID).remove(foundUsername);
        System.out.println("sessions after closed connection:\n" + this);
    }

    public HashMap<String, Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
