package server;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;

public class WebSocketSessions {

    private static HashMap<Integer, HashMap<String, Session>> sessionMap;
    private static WebSocketSessions instance;

    private WebSocketSessions() {
        sessionMap = new HashMap<>();
    }

    public static WebSocketSessions getInstance() {
        if (instance == null) {
            instance = new WebSocketSessions();
        }
        return instance;
    }

    @Override
    public String toString() {
        StringBuilder mapString = new StringBuilder("sessionMap: ");
        for (Integer gameID : sessionMap.keySet()) {
            mapString.append("gameID: ").append(gameID);
            mapString.append(", authTokens: ").append(sessionMap.get(gameID).keySet()).append("\n");
        }
        mapString.append("size: ").append(sessionMap.size());
        return mapString.toString();
    }

    public void addSessionToGame(int gameID, String authToken, Session session) {
        if (sessionMap.containsKey(gameID)) {
            HashMap<String, Session> innerMap = sessionMap.get(gameID);
            innerMap.put(authToken, session);
        } else {
            HashMap<String, Session> innerMap = new HashMap<>();
            innerMap.put(authToken, session);
            sessionMap.put(gameID, innerMap);
        }
    }

    public void removeSessionFromGame(int gameID, String authToken) {
        sessionMap.get(gameID).remove(authToken);
    }

    public void removeSession(Session session) {
        Integer foundID = null;
        String foundToken = null;
        for (Integer gameID : sessionMap.keySet()) {
            for (String authToken : sessionMap.get(gameID).keySet()) {
                if (sessionMap.get(gameID).get(authToken).equals(session)) {
                    foundID = gameID;
                    foundToken = authToken;
                }
            }
        }
        sessionMap.get(foundID).remove(foundToken);
    }

    public HashMap<String, Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
