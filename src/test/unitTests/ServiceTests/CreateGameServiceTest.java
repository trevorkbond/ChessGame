package unitTests.ServiceTests;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.*;
import server.Server;
import services.ClearService;
import services.CreateGameService;
import services.request.CreateGameRequest;
import unitTests.UnitTests;

import java.sql.Connection;
import java.sql.SQLException;

class CreateGameServiceTest extends UnitTests {

    private CreateGameService createGameService;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        initializeAndClearDAOs();
        createGameService = new CreateGameService();

        // add manually created valid authTokens
        authDAO.addToken(new AuthToken("username", "authToken17"));
        authDAO.addToken(new AuthToken("username2", "anotherToken22"));
        authDAO.addToken(new AuthToken("username3", "lastToken49"));
    }

    @Test
    @DisplayName("Create Game Success")
    void createGameSuccess() throws SQLException {
        Assertions.assertDoesNotThrow(() -> createGameService.createGame(
                new CreateGameRequest("fungame"), "authToken17", authDAO, gameDAO));
        Assertions.assertEquals(gameDAO.getGames().size(), 1, "Game wasn't created");
        Assertions.assertDoesNotThrow(() -> createGameService.createGame(
                new CreateGameRequest("fungame2"), "anotherToken22", authDAO, gameDAO));
        Assertions.assertEquals(gameDAO.getGames().size(), 2, "Game wasn't created");
        Assertions.assertDoesNotThrow(() -> createGameService.createGame(
                new CreateGameRequest("fungame3"), "lastToken49", authDAO, gameDAO));
        Assertions.assertEquals(gameDAO.getGames().size(), 3, "Game wasn't created");
    }

    @Test
    @DisplayName("Create Game Failure")
    void setCreateGameFailure() {
        // incorrect authTokens
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "authToken18", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "invalid", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "****", authDAO, gameDAO));

        // delete an authToken to simulate logging out and then try creating game
//        authDAO.getTokens().removeIf(auth -> auth.getUsername().equals("username"));
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "authToken17", authDAO, gameDAO));
    }

}