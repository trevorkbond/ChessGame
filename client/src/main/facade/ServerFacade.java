package facade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.RegisterRequest;
import result.LoginRegisterResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private final String BASE_URL = "http://localhost:8080";
    private final int HTTP_OK = 200;


    public LoginRegisterResult register(RegisterRequest request) throws IOException {
        HttpURLConnection connection = makeRequest(request, "/user", "POST");
        return (LoginRegisterResult) getResponse(connection, LoginRegisterResult.class);
    }

    public void clear() throws IOException {
        HttpURLConnection connection = makeRequest(null, "/db", "DELETE");
        getResponse(connection, LoginRegisterResult.class);
    }

    private HttpURLConnection makeRequest(Object request, String path, String method) throws IOException {
        URL url = new URL(BASE_URL + path);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        if (request != null) {
            writeBody(request, connection);
        }
        connection.connect();

        if (connection.getResponseCode() != HTTP_OK) {
            throw new IOException("Failure. HTTP response code: " + connection.getResponseCode());
        }
        return connection;
    }

    private Object getResponse(HttpURLConnection connection, Class desiredClass) throws IOException {
        String response = readBody(connection);
        return responseToObject(response, desiredClass);
    }

    private Object responseToObject(String response, Class desiredClass) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        return gson.fromJson(response, desiredClass);
    }

    private String readBody(HttpURLConnection connection) throws IOException {
        try (InputStream responseBody = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
}
