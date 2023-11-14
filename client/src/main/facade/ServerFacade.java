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
    private final int HTTP_OK = 400;


    public LoginRegisterResult register(RegisterRequest request) throws IOException {
        URL url = new URL(BASE_URL + "/user");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        writeBody(request, connection);
        connection.connect();


        //FIXME: handle failure responseCode
        //FIXME: refactor methods for use in other areas of the code
        try (InputStream responseBody = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            return gson.fromJson(response.toString(), LoginRegisterResult.class);
        }

    }

    public void clear() throws IOException {
        URL url = new URL(BASE_URL + "/db");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("DELETE");

        connection.connect();

        //FIXME: handle failure responseCode
        int responseCode = connection.getResponseCode();
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
