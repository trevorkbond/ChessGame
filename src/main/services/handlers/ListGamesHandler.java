package services.handlers;

import chess.ChessPiece;
import dataAccess.DataAccessException;
import models.AuthToken;
import services.ListGamesService;
import services.result.ListGamesResult;
import spark.Request;
import spark.Response;

import java.util.List;

public class ListGamesHandler extends Handler {
    /**
     * singleton method
     */
    private static ListGamesHandler instance;

    public static ListGamesHandler getInstance() {
        if (instance == null) {
            instance = new ListGamesHandler();
        }
        return instance;
    }

    private ListGamesHandler() {}

    public String handleRequest(Request request, Response response) {
        try {
            if (request.requestMethod().equalsIgnoreCase("get")) {
                AuthToken tempToken = new AuthToken(null, request.headers("Authorization"));
                ListGamesService service = new ListGamesService();
                ListGamesResult result = service.listGames(tempToken, request);
                response.status(200);
                response.body(objectToJson(result));
                return objectToJson(result);
            } else {
                return setBadRequest(response);
            }
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                return setUnauthorizedRequest(response);
            } else {
                response.status(500);
                response.body(getErrorMessage("Error: I'm unsure what happened here"));
                return getErrorMessage("Error: I'm unsure what happened here");
            }
        }
    }
}
