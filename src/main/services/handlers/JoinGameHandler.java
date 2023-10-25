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
                Result result = service.joinGame(requestObject, request.headers("Authorization"));
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
