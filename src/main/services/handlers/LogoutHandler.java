package services.handlers;

import dataAccess.DataAccessException;
import models.AuthToken;
import services.LogoutService;
import services.result.Result;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {

    /**
     * Employing singleton method
     */
    private static LogoutHandler instance;

    /**
     * private constructor to ensure no outside instantiation
     */
    private LogoutHandler() {
    }

    /**
     * getInstance function for singleton method
     *
     * @return the only instance of LogoutHandler
     */
    public static LogoutHandler getInstance() {
        if (instance == null) {
            instance = new LogoutHandler();
        }
        return instance;
    }

    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("delete")) {
                AuthToken tempToken = new AuthToken(null, request.headers("Authorization"));
                LogoutService service = new LogoutService();
                Result result = service.logout(tempToken);
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
