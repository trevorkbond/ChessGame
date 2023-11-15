package server;

import chess.ChessGame;
import facade.ServerFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.LoginRegisterResult;
import result.Result;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerFacadeTest {

    private ServerFacade serverFacade;

    @BeforeEach
    void setUp() throws IOException {
        serverFacade = new ServerFacade();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Successful Registration")
    public void registerSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        LoginRegisterResult exptectedResult = new LoginRegisterResult(null, "someToken", "Trevor");
        LoginRegisterResult actualResult = serverFacade.register(request);

        Assertions.assertEquals(exptectedResult.getUsername(), actualResult.getUsername(), "User wasn't created properly");
        Assertions.assertNotNull(actualResult.getAuthToken(), "Registered user wasn't given an authToken");
    }

    @Test
    @DisplayName("Registration Failure")
    public void registerFailure() throws IOException {
        // make duplicate registration requests
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        serverFacade.register(request);
        Assertions.assertThrows(IOException.class, () -> serverFacade.register(request));
        // ensure the response code is 403 already taken
        try {
            serverFacade.register(request);
        } catch (IOException e) {
            Assertions.assertEquals("403", e.getMessage(), "Incorrect response code thrown");
        }

        // attempt bad request with null parameter(s)
        RegisterRequest badRequest = new RegisterRequest(null, "havepasswordtho", null);
        Assertions.assertThrows(IOException.class, () -> serverFacade.register(badRequest));
        // ensure the response code is 400 bad request
        try {
            serverFacade.register(badRequest);
        } catch (IOException e) {
            Assertions.assertEquals("400", e.getMessage(), "Incorrect response code thrown");
        }
    }

    @Test
    @DisplayName("Successful Login")
    public void loginSuccess() throws IOException {
        // register user then login (current system allows user to be logged in multiple times concurrently
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        serverFacade.register(request);
        LoginRegisterResult exptectedResult = new LoginRegisterResult(null, "someToken", "Trevor");
        LoginRequest loginRequest = new LoginRequest("Trevor", "Bond");
        LoginRegisterResult actualResult = serverFacade.login(loginRequest);

        Assertions.assertEquals(exptectedResult.getUsername(), actualResult.getUsername(), "User not logged in");
        Assertions.assertNotNull(actualResult.getAuthToken(), "Logged in user not given authToken");
    }

    @Test
    @DisplayName("Login Failure")
    public void loginFailure() throws IOException {
        // attempt to login with user that doesn't exist yet
        LoginRequest loginRequest = new LoginRequest("Trevor", "Bond");
        Assertions.assertThrows(IOException.class, () -> serverFacade.login(loginRequest));
        // ensure response code is 401 unauthorized
        try {
            serverFacade.login(loginRequest);
        } catch (IOException e) {
            Assertions.assertEquals("401", e.getMessage(), "Incorrect response code thrown");
        }

        // register a user then try login with bad credentials
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        serverFacade.register(request);
        LoginRequest badRequest = new LoginRequest("trevor", "Bond");
        Assertions.assertThrows(IOException.class, () -> serverFacade.login(badRequest));
    }

    @Test
    @DisplayName("Logout Success")
    public void logoutSuccess() throws IOException {
        // register (and login) a user then log the user out
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        LoginRegisterResult result = serverFacade.register(request);
        String authToken = result.getAuthToken();
        Result logoutResult = Assertions.assertDoesNotThrow(() -> serverFacade.logout(authToken));

        // assert logoutResult message is null as it should be (no error codes)
        Assertions.assertNull(logoutResult.getMessage());
    }

    @Test
    @DisplayName("Logout Failure")
    public void logoutFailure() throws IOException {
        // attempt to logout a user that isn't registered or logged in
        Assertions.assertThrows(IOException.class, () -> serverFacade.logout("someToken"));
        try {
            serverFacade.logout("someToken");
        } catch (IOException e) {
            Assertions.assertEquals("401", e.getMessage(), "Response code on invalid login wasn't 401");
        }

        // register a user but attempt logout with bad authToken
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        serverFacade.register(request);
        Assertions.assertThrows(IOException.class, () -> serverFacade.logout("IDKwhatTokengotaddedbfjdksfds"));
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGamesSuccess() throws IOException {
        // register a user
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        LoginRegisterResult result = serverFacade.register(request);
        String authToken = result.getAuthToken();

        CreateGameRequest gameRequest = new CreateGameRequest("Cool Game");
        CreateGameResult gameResult = Assertions.assertDoesNotThrow(() -> serverFacade.createGame(gameRequest, authToken));
        Assertions.assertNull(gameResult.getMessage());
    }

    @Test
    @DisplayName("Create Game Failure")
    public void createGameFailure() throws IOException {
        // try to create game without authentication
        Assertions.assertThrows(IOException.class, () -> serverFacade.createGame(new CreateGameRequest("yay"), null));
        try {
            serverFacade.createGame(new CreateGameRequest("yay"), "someToken");
        } catch (IOException e) {
            Assertions.assertEquals("401", e.getMessage(), "Response code on invalid create game wasn't 401");
        }
    }

    @Test
    @DisplayName("List Games Success")
    public void listGameSuccess() throws IOException {
        // register a user
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        LoginRegisterResult result = serverFacade.register(request);
        String authToken = result.getAuthToken();

        CreateGameRequest request1 = new CreateGameRequest("Game 1");
        CreateGameRequest request2 = new CreateGameRequest("Game 2");
        CreateGameRequest request3 = new CreateGameRequest("Game 3");
        serverFacade.createGame(request1, authToken);
        serverFacade.createGame(request2, authToken);
        serverFacade.createGame(request3, authToken);

        ListGamesResult gameResult = Assertions.assertDoesNotThrow(() -> serverFacade.listGames(authToken));
        Assertions.assertEquals(3, gameResult.getGames().size(), "All 3 games not listed");
    }

    @Test
    @DisplayName("List Game Failure")
    public void listGameFailure() throws IOException {
        // try to list games without authentication
        Assertions.assertThrows(IOException.class, () -> serverFacade.listGames("Bad Token"));
        try {
            serverFacade.listGames("Bad Token");
        } catch (IOException e) {
            Assertions.assertEquals("401", e.getMessage(), "Response code on invalid list games wasn't 401");
        }
    }

    @Test
    @DisplayName("Join Game Success")
    public void joinGameSuccess() throws IOException {
        // register a user
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        LoginRegisterResult result = serverFacade.register(request);
        String authToken = result.getAuthToken();

        CreateGameResult gameResult = serverFacade.createGame(new CreateGameRequest("yay"), authToken);

        int gameID = gameResult.getGameID();
        JoinGameRequest joinRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
        JoinGameRequest joinRequest2 = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
        Result joinResult = Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(joinRequest, authToken));
        Assertions.assertNull(joinResult.getMessage());
        joinResult = Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(joinRequest2, authToken));
        Assertions.assertNull(joinResult.getMessage());
    }

    @Test
    @DisplayName("Join Game Failure")
    public void joinGameFailure() throws IOException {
        // try to join game without game existing
        assertThrows(IOException.class, () -> serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 0), null));
        try {
            serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 0), "someToken");
        } catch (IOException e) {
            assertEquals("400", e.getMessage(), "Incorrect error thrown (not 400 bad request)");
        }

        // try to join game without authentication
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        LoginRegisterResult result = serverFacade.register(request);
        String authToken = result.getAuthToken();

        CreateGameResult gameResult = serverFacade.createGame(new CreateGameRequest("yay"), authToken);
        int gameID = gameResult.getGameID();
        assertThrows(IOException.class, () -> serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), "someToken"));
        try {
            serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), "someToken");
        } catch (IOException e) {
            assertEquals("401", e.getMessage(), "Incorrect error thrown (not 401 unauthorized)");
        }

        // then try to join spots that have been filled
        serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), authToken);
        serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID), authToken);

        assertThrows(IOException.class, () -> serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), authToken));
        assertThrows(IOException.class, () -> serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), authToken));

        // assert correct error codes thrown
        try {
            serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID), authToken);
        } catch (IOException e) {
            assertEquals("403", e.getMessage(), "Incorrect error thrown (not 403 already taken)");
        }

        try {
            serverFacade.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID), authToken);
        } catch (IOException e) {
            assertEquals("403", e.getMessage(), "Incorrect error thrown (not 403 already  taken)");
        }


    }
}