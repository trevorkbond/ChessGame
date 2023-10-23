package server;

import services.handlers.RegisterHandler;
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

        Spark.post("/user", ((request, response) -> new RegisterHandler().handleRequest(request, response)));
    }
}
