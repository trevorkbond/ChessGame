package unitTests.ServiceTests;

import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.CreateGameService;
import request.CreateGameRequest;
import unitTests.UnitTest;

import java.sql.SQLException;

class CreateGameServiceTest extends UnitTest {

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
    void createGameSuccess() throws DataAccessException {
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

    }

}