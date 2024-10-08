package server;

import dataAccess.Database;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import services.handlers.*;
import spark.Spark;

/**
 * The Server class sets up the server with JavaSpark and routes requests to their respective handlers.
 */
@WebSocket
public class Server {
    public static Database database = new Database();

    public static void main(String[] args) {
        run();
    }

    /**
     * run() runs the server on port 8080 and routes all requests to their respective handlers.
     */
    public static void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");
        Spark.webSocket("/connect", WebSocketHandler.class);

        RegisterHandler registerHandler = RegisterHandler.getInstance();
        ClearHandler clearHandler = ClearHandler.getInstance();
        CreateGameHandler createGameHandler = CreateGameHandler.getInstance();
        LogoutHandler logoutHandler = LogoutHandler.getInstance();
        LoginHandler loginHandler = LoginHandler.getInstance();
        JoinGameHandler joinGameHandler = JoinGameHandler.getInstance();
        ListGamesHandler listGamesHandler = ListGamesHandler.getInstance();

        Spark.post("/user", (registerHandler::handleRequest));
        Spark.delete("/db", (clearHandler::handleRequest));
        Spark.post("/game", (createGameHandler::handleRequest));
        Spark.delete("/session", (logoutHandler::handleRequest));
        Spark.post("/session", (loginHandler::handleRequest));
        Spark.put("/game", (joinGameHandler::handleRequest));
        Spark.get("/game", (listGamesHandler::handleRequest));

    }
}
