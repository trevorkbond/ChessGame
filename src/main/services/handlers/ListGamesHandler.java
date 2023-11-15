package services.handlers;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import result.ListGamesResult;
import services.ListGamesService;
import spark.Request;
import spark.Response;

import java.sql.Connection;

public class ListGamesHandler extends Handler {
    /**
     * singleton method
     */
    private static ListGamesHandler instance;

    private ListGamesHandler() {
    }

    public static ListGamesHandler getInstance() {
        if (instance == null) {
            instance = new ListGamesHandler();
        }
        return instance;
    }

    public String handleRequest(Request request, Response response) {
        System.out.println("in listgameshandler");
        try {
            if (request.requestMethod().equalsIgnoreCase("get")) {
                AuthToken tempToken = new AuthToken(null, request.headers("Authorization"));
                ListGamesService service = new ListGamesService();
                Connection connection = getDatabaseConnection();
                AuthDAO authDAO = new AuthDAO(connection);
                GameDAO gameDAO = new GameDAO(connection);
                ListGamesResult result = service.listGames(tempToken, request.headers("Authorization"), authDAO, gameDAO);
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
