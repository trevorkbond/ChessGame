package client;

import chess.ChessBoardImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ChessBoardAdapter;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginRegisterResult;
import result.Result;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private final String BASE_URL = "http://localhost:8080";
    private final int HTTP_OK = 200;

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    public LoginRegisterResult register(RegisterRequest request) throws IOException {
        HttpURLConnection connection = makeRequest(request, "/user", "POST", null);
        return (LoginRegisterResult) getResponse(connection, LoginRegisterResult.class);
    }

    public LoginRegisterResult login(LoginRequest request) throws IOException {
        HttpURLConnection connection = makeRequest(request, "/session", "POST", null);
        return (LoginRegisterResult) getResponse(connection, LoginRegisterResult.class);
    }

    public Result logout(String authToken) throws IOException {
        HttpURLConnection connection = makeRequest(null, "/session", "DELETE", authToken);
        return (Result) getResponse(connection, Result.class);
    }

    public CreateGameResult createGame(CreateGameRequest request, String authToken) throws IOException {
        HttpURLConnection connection = makeRequest(request, "/game", "POST", authToken);
        return (CreateGameResult) getResponse(connection, CreateGameResult.class);
    }

    public ListGamesResult listGames(String authToken) throws IOException {
        HttpURLConnection connection = makeRequest(null, "/game", "GET", authToken);
        return (ListGamesResult) getResponse(connection, ListGamesResult.class);
    }

    public Result joinGame(JoinGameRequest request, String authToken) throws IOException {
        HttpURLConnection connection = makeRequest(request, "/game", "PUT", authToken);
        return (Result) getResponse(connection, Result.class);
    }

    public void clear() throws IOException {
        HttpURLConnection connection = makeRequest(null, "/db", "DELETE", null);
        getResponse(connection, LoginRegisterResult.class);
    }

    private HttpURLConnection makeRequest(Object request, String path, String method, String authToken) throws IOException {
        URL url = new URL(BASE_URL + path);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }
        connection.setDoOutput(true);
        if (request != null) {
            writeBody(request, connection);
        }
        connection.connect();

        if (connection.getResponseCode() != HTTP_OK) {
            throw new IOException(String.valueOf(connection.getResponseCode()));
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
        builder.registerTypeAdapter(ChessBoardImpl.class, new ChessBoardAdapter());

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
}
