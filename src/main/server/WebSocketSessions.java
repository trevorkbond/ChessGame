package server;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;

public class WebSocketSessions {

    private static final HashMap<Integer, HashMap<String, Session>> sessionMap = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder mapString = new StringBuilder("sessionMap: ");
        for (Integer gameID : sessionMap.keySet()) {
            mapString.append("gameID: ").append(gameID);
            mapString.append(", usernames: ").append(sessionMap.get(gameID).keySet()).append("\n");
        }
        return mapString.toString();
    }

    public void addSessionToGame(int gameID, String username, Session session) {
        sessionMap.put(gameID, new HashMap<>() {{
            put(username, session);
        }});
        System.out.println("After addSessionToGame:\n" + this);
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
