package services.handlers;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import services.ClearService;
import services.result.Result;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.SQLException;

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
    private ClearHandler() {
    }

    /**
     * Retrieves static instance of ClearHandler
     *
     * @return the sole instance of the class
     */
    public static ClearHandler getInstance() {
        if (instance == null) {
            instance = new ClearHandler();
        }
        return instance;
    }

    /**
     * Handles a clear API request
     *
     * @param request  the passed in request
     * @param response the passed in response
     * @return a JSON string detailing success or error of result
     */
    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("delete")) {
                ClearService service = new ClearService();
                Connection connection = getDatabaseConnection();
                UserDAO userDAO = new UserDAO(connection);
                AuthDAO authDAO = new AuthDAO(connection);
                GameDAO gameDAO = new GameDAO(connection);
                Result result = service.clear(userDAO, authDAO, gameDAO);
                response.status(200);
                response.body(objectToJson(result));
                return objectToJson(result);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException | SQLException e) {
            return handleDataException(response, e);
        }
    }

}
