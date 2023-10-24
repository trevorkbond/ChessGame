package services.handlers;


import dataAccess.DataAccessException;
import org.eclipse.jetty.util.log.Log;
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
                return objectToJson(resultObject);
            } else {
                response.status(400);
                return getErrorMessage("Error: bad request");
            }
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: already taken")) {
                response.status(403);
                return getErrorMessage("Error: already taken");
            } else {
                response.status(500);
                return getErrorMessage("Error: I'm unsure what happened here");
            }
        }
    }
}
