package services.handlers;

import dataAccess.DataAccessException;
import services.CreateGameService;
import services.request.CreateGameRequest;
import services.result.CreateGameResult;
import spark.Request;
import spark.Response;

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
                CreateGameResult result = service.createGame(requestObject, request);
                response.status(200);
                response.body(objectToJson(result));
                return objectToJson(result);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                return setUnauthorizedRequest(response);
            } else {
                response.status(500);
                response.body(getErrorMessage("Error: I'm unsure what happened here"));
                return getErrorMessage("Error: I'm unsure what happened here");
            }
        }
    }

}
