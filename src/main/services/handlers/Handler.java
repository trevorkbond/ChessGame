package services.handlers;
import com.google.gson.GsonBuilder;
import services.result.LoginRegisterResult;
import services.result.Result;
import spark.Request;
import spark.Spark;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
     * @param message the given message
     * @return the error message formatted as JSON
     */
    public String getErrorMessage(String message) {
        Result resultObject = new Result(message);
        return objectToJson(resultObject);
    }
}
