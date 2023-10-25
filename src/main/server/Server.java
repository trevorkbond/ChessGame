package server;

import services.handlers.*;
import spark.Spark;

/**
 * The Server class sets up the server with JavaSpark and routes requests to their respective handlers.
 */
public class Server {
    public static void main(String[] args) {
        Server server = new Server();
        run();
    }

    /**
     * run() runs the server on port 8080 and routes all requests to their respective handlers.
     */
    public static void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("/Users/trevorbond/cs240/ChessGame/web");

        RegisterHandler registerHandler = RegisterHandler.getInstance();
        ClearHandler clearHandler = ClearHandler.getInstance();
        CreateGameHandler createGameHandler = CreateGameHandler.getInstance();
        LogoutHandler logoutHandler = LogoutHandler.getInstance();
        LoginHandler loginHandler = LoginHandler.getInstance();

        Spark.post("/user", (registerHandler::handleRequest));
        Spark.delete("/db", (clearHandler::handleRequest));
        Spark.post("/game", (createGameHandler::handleRequest));
        Spark.delete("/session", (logoutHandler::handleRequest));
        Spark.post("/session", (loginHandler::handleRequest));
    }
}
