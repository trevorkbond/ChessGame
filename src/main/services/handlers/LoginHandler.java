package services.handlers;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import request.LoginRequest;
import result.LoginRegisterResult;
import services.LoginService;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.SQLException;

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
                Connection connection = getDatabaseConnection();
                UserDAO userDAO = new UserDAO(connection);
                AuthDAO authDAO = new AuthDAO(connection);
                LoginRegisterResult result = service.login(requestObject, userDAO, authDAO);
                response.status(200);
                return objectToJson(result);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException | SQLException e) {
            return handleDataException(response, e);
        }
    }
}
