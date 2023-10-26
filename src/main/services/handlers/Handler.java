package services.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import services.result.Result;
import spark.Request;
import spark.Response;

/**
 * Handler is a superclass that contains code helpful for parsing JSON strings into Java objects and other shared
 * methods among handlers
 */
public class Handler {
    public Object gsonToRequest(Class desiredClass, Request request) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        return gson.fromJson(request.body(), desiredClass);
    }

    /**
     * Converts an object to a JSON string
     *
     * @param object the given object to serialize
     * @return the JSON string
     */
    public String objectToJson(Object object) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(object);
    }

    /**
     * Generates a custom error message JSON string
     *
     * @param message the given message
     * @return the error message formatted as JSON
     */
    public String getErrorMessage(String message) {
        Result resultObject = new Result(message);
        return objectToJson(resultObject);
    }

    /**
     * Generates a bad request error message
     *
     * @param response the http Response
     * @return error message as a JSON string
     */
    public String setBadRequest(Response response) {
        response.status(400);
        response.body(getErrorMessage("Error: bad request"));
        return getErrorMessage("Error: bad request");
    }

    /**
     * Generates an unauthorized error message
     *
     * @param response the http Response
     * @return unauthorized message as JSON string
     */
    public String setUnauthorizedRequest(Response response) {
        response.status(401);
        response.body(getErrorMessage("Error: unauthorized"));
        return getErrorMessage("Error: unauthorized");


    }

    /**
     * Handles a data exception
     *
     * @param response the HTTP response
     * @param e        the exception
     * @return the error message in JSON format
     */
    public String handleDataException(Response response, DataAccessException e) {
        if (e.getMessage().equals("Error: unauthorized")) {
            return setUnauthorizedRequest(response);
        } else if (e.getMessage().equals("Error: bad request")) {
            return setBadRequest(response);
        } else if (e.getMessage().equals("Error: already taken")) {
            response.status(403);
            response.body(getErrorMessage("Error: already taken"));
            return getErrorMessage("Error: already taken");
        } else {
            response.status(500);
            response.body(getErrorMessage("Error: I'm unsure what happened here"));
            return getErrorMessage("Error: I'm unsure what happened here");
        }
    }
}
