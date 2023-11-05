package services.handlers;


import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import services.RegisterService;
import services.request.RegisterRequest;
import services.result.LoginRegisterResult;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.SQLException;

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
     * Private constructor to prevent direct instantiation
     */
    private RegisterHandler() {
    }

    /**
     * Retrieves the static instance of the RegisterHandler
     *
     * @return instance
     */
    public static RegisterHandler getInstance() {
        if (instance == null) {
            instance = new RegisterHandler();
        }
        return instance;
    }

    /**
     * Handles a register request from the Server
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return a JSON string representing a LoginRegisterResult object
     */
    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("post")) {
                RegisterRequest requestObject = (RegisterRequest) gsonToRequest(RegisterRequest.class, request);
                RegisterService service = new RegisterService();
                Connection connection = getDatabaseConnection();
                UserDAO userDAO = new UserDAO(connection);
                AuthDAO authDAO = new AuthDAO(connection);
                LoginRegisterResult resultObject = service.register(requestObject, userDAO, authDAO);
                response.status(200);
                response.body(objectToJson(resultObject));
                return objectToJson(resultObject);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException | SQLException e) {
            return handleDataException(response, e);
        }
    }


}
