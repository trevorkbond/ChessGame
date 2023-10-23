package services.handlers;
import com.google.gson.GsonBuilder;
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
}
