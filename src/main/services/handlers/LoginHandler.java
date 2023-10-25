package services.handlers;

import dataAccess.DataAccessException;
import services.LoginService;
import services.request.LoginRequest;
import services.result.LoginRegisterResult;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {

    /**
     * Using singleton method
     */
    private static LoginHandler instance;

    /**
     * Private constructor to ensure no outside instantiation
     */
    private LoginHandler() {
    }

    /**
     * Get instance of LoginHandler
     *
     * @return sole static instance of class
     */
    public static LoginHandler getInstance() {
        if (instance == null) {
            instance = new LoginHandler();
        }
        return instance;
    }

    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("post")) {
                LoginRequest requestObject = (LoginRequest) gsonToRequest(LoginRequest.class, request);
                LoginService service = new LoginService();
                LoginRegisterResult result = service.login(requestObject);
                response.status(200);
                return objectToJson(result);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException e) {
            return handleDataException(response, e);
        }
    }
}
