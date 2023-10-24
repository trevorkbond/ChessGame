package services.handlers;


import dataAccess.DataAccessException;
import services.RegisterService;
import services.request.RegisterRequest;
import services.result.LoginRegisterResult;
import spark.Request;
import spark.Response;

/**
 * RegisterHandler interacts with the RegisterService, which will then be interacting with DAO models and the database
 * to register new Users in the system.
 */
public class RegisterHandler extends Handler {
    /**
     * Employing the Singleton method to ensure only one handler is ever created
     */
    private static RegisterHandler instance;

    /**
     * Retrieves the static instance of the RegisterHandler
     * @return instance
     */
    public static RegisterHandler getInstance() {
        if (instance == null) {
            instance = new RegisterHandler();
        }
        return instance;
    }

    /**
     * Private constructor to prevent direct instantiation
     */
    private RegisterHandler() {}

    /**
     * Handles a register request from the Server
     * @param request the HTTP request
     * @param response the HTTP response
     * @return a JSON string representing a LoginRegisterResult object
     */
    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("post")) {
                RegisterRequest requestObject = (RegisterRequest) gsonToRequest(RegisterRequest.class, request);
                RegisterService service = new RegisterService();
                LoginRegisterResult resultObject = service.register(requestObject);
                response.status(200);
                response.body(objectToJson(resultObject));
                return objectToJson(resultObject);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: already taken")) {
                response.status(403);
                response.body(getErrorMessage("Error: already taken"));
                return getErrorMessage("Error: already taken");
            } else if (e.getMessage().equals("Error: bad request")) {
                return setBadRequest(response);
            } else {
                response.status(500);
                response.body(getErrorMessage("Error: I'm unsure what happened here"));
                return getErrorMessage("Error: I'm unsure what happened here");
            }
        }
    }


}
