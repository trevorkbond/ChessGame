package services.handlers;

import dataAccess.DataAccessException;
import services.ClearService;
import services.result.LoginRegisterResult;
import services.result.Result;
import spark.Request;
import spark.Response;

/**
 * ClearHandler handles calls to the clear API
 */
public class ClearHandler extends Handler {
    /**
     * Employing singleton method
     */
    private static ClearHandler instance;

    /**
     * Private constructor to ensure no direct instantiation
     */
    private ClearHandler() {}

    /**
     * Retrieves static instance of ClearHandler
     * @return the sole instance of the class
     */
    public static ClearHandler getInstance() {
        if (instance == null) {
            instance = new ClearHandler();
        }
        return instance;
    }

    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("delete")) {
                ClearService service = new ClearService();
                Result result = service.clear();
                response.status(200);
                return objectToJson(result);
            } else {
                response.status(400);
                LoginRegisterResult resultObject = new LoginRegisterResult("Error: bad request", null, null);
                return objectToJson(resultObject);
            }
        } catch (DataAccessException e) {
            response.status(500);
            Result result = new Result("Error: There was a DAO exception somewhere in clear methods");
            return objectToJson(result);
        }
    }
}
