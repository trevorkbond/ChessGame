package services.handlers;

import dataAccess.DataAccessException;
import services.JoinGameService;
import services.request.JoinGameRequest;
import services.result.Result;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {

    /**
     * singleton method
     */
    private static JoinGameHandler instance;

    public static JoinGameHandler getInstance() {
        if (instance == null) {
            instance = new JoinGameHandler();
        }
        return instance;
    }

    private JoinGameHandler() {}

    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("put")) {
                JoinGameRequest requestObject = (JoinGameRequest) gsonToRequest(JoinGameRequest.class, request);
                JoinGameService service = new JoinGameService();
                Result result = service.joinGame(requestObject, request);
                response.status(200);
                response.body(objectToJson(result));
                return objectToJson(result);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                return setUnauthorizedRequest(response);
            } else if (e.getMessage().equals("Error: bad request")) {
                return setBadRequest(response);
            } else if (e.getMessage().equals("Error: already taken")) {
                response.status(403);
                response.body(getErrorMessage("Error: already taken"));
                return getErrorMessage("Error: already taken");
            } else {
                response.status(500);
                response.body(getErrorMessage("Error: I'm unsure what happened here"));
                return getErrorMessage("Error: I'm unsure what happened here");
            }
        }
    }
}
