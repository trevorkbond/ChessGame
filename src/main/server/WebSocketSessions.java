package server;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;

public class WebSocketSessions {

    private HashMap<Integer, HashMap<String, Session>> sessionMap = new HashMap<>();

    public void addSessionToGame(int gameID, String username, Session session) {
        sessionMap.put(gameID, new HashMap<>(){{put(username, session);}});
    }

    public void removeSessionFromGame(int gameID, String username) {
        sessionMap.get(gameID).remove(username);
    }

    public void removeSession(Session session) {
        for (Integer gameID : sessionMap.keySet()) {
            for (String username : sessionMap.get(gameID).keySet()) {
                if (sessionMap.get(gameID).get(username).equals(session)) {
                    sessionMap.get(gameID).remove(username);
                }
            }
        }
    }

    public HashMap<String, Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
