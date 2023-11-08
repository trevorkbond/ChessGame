package services.handlers;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import services.JoinGameService;
import request.JoinGameRequest;
import result.Result;
import spark.Request;
import spark.Response;

import java.sql.Connection;

public class JoinGameHandler extends Handler {

    /**
     * singleton method
     */
    private static JoinGameHandler instance;

    private JoinGameHandler() {
    }

    public static JoinGameHandler getInstance() {
        if (instance == null) {
            instance = new JoinGameHandler();
        }
        return instance;
    }

    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("put")) {
                JoinGameRequest requestObject = (JoinGameRequest) gsonToRequest(JoinGameRequest.class, request);
                JoinGameService service = new JoinGameService();
                Connection connection = getDatabaseConnection();
                AuthDAO authDAO = new AuthDAO(connection);
                GameDAO gameDAO = new GameDAO(connection);
                Result result = service.joinGame(requestObject, request.headers("Authorization"), authDAO, gameDAO);
                response.status(200);
                response.body(objectToJson(result));
                return objectToJson(result);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException e) {
            return handleDataException(response, e);
        }
    }
}
