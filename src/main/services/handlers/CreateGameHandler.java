package services.handlers;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import services.CreateGameService;
import request.CreateGameRequest;
import result.CreateGameResult;
import spark.Request;
import spark.Response;

import java.sql.Connection;

public class CreateGameHandler extends Handler {

    /**
     * Employing singleton method to ensure one instance is ever created
     */
    private static CreateGameHandler instance;

    /**
     * private constructor to ensure no direct instantiation outside of getInstance
     */
    private CreateGameHandler() {
    }

    /**
     * getInstance method for singleton pattern
     *
     * @return the sole instance of CreateGameHandler
     */
    public static CreateGameHandler getInstance() {
        if (instance == null) {
            instance = new CreateGameHandler();
        }
        return instance;
    }

    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("post")) {
                CreateGameRequest requestObject = (CreateGameRequest) gsonToRequest(CreateGameRequest.class, request);
                CreateGameService service = new CreateGameService();
                Connection connection = getDatabaseConnection();
                AuthDAO authDAO = new AuthDAO(connection);
                GameDAO gameDAO = new GameDAO(connection);
                CreateGameResult result = service.createGame(requestObject, request.headers("Authorization"), authDAO, gameDAO);
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
