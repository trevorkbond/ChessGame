package unitTests;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ClearService;
import services.CreateGameService;
import services.request.CreateGameRequest;

class CreateGameServiceTest {

    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private CreateGameService createGameService;

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO = AuthDAO.getInstance();
        gameDAO = GameDAO.getInstance();
        createGameService = new CreateGameService();
        ClearService clearService = new ClearService();
        clearService.clear();

        // add manually created valid authTokens
        authDAO.addToken(new AuthToken("username", "authToken17"));
        authDAO.addToken(new AuthToken("username2", "anotherToken22"));
        authDAO.addToken(new AuthToken("username3", "lastToken49"));
    }

    @Test
    @DisplayName("Create Game Success")
    void createGameSuccess() {
        Assertions.assertDoesNotThrow(() -> createGameService.createGame(
                new CreateGameRequest("fungame"), "authToken17"));
        Assertions.assertEquals(gameDAO.getGames().size(), 1, "Game wasn't created");
        Assertions.assertDoesNotThrow(() -> createGameService.createGame(
                new CreateGameRequest("fungame2"), "anotherToken22"));
        Assertions.assertEquals(gameDAO.getGames().size(), 2, "Game wasn't created");
        Assertions.assertDoesNotThrow(() -> createGameService.createGame(
                new CreateGameRequest("fungame3"), "lastToken49"));
        Assertions.assertEquals(gameDAO.getGames().size(), 3, "Game wasn't created");
    }

    @Test
    @DisplayName("Create Game Failure")
    void setCreateGameFailure() {
        // incorrect authTokens
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "authToken18"));
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), ""));
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "invalid"));
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "****"));

        // delete an authToken to simulate logging out and then try creating game
        authDAO.getTokens().removeIf(auth -> auth.getUsername().equals("username"));
        Assertions.assertThrows(DataAccessException.class, () -> createGameService.createGame(
                new CreateGameRequest("fungame"), "authToken17"));
    }
}