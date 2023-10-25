package unitTests;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ClearService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private ClearService clearService;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = UserDAO.getInstance();
        authDAO = AuthDAO.getInstance();
        gameDAO = GameDAO.getInstance();
        clearService = new ClearService();

        // populate all DAO's with dummy data
        for (int i = 0; i < 100; i++) {
            userDAO.createUser(new User(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }
        for (int i = 0; i < 100; i++) {
            authDAO.addToken(new AuthToken(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }
        for (int i = 0; i < 100; i++) {
            gameDAO.createGame(new Game(UUID.randomUUID().toString()));
        }
    }

    @Test
    @DisplayName("Clear All DAO's")
    void clearWithoutService() throws DataAccessException {
        // DAO's are all populated with 100 objects each upon setup
        userDAO.clearUsers();
        Assertions.assertEquals(userDAO.getUsers().size(), 0, "userDAO wasn't cleared");
        authDAO.clearTokens();
        Assertions.assertEquals(authDAO.getTokens().size(), 0, "authDAO wasn't cleared");
        gameDAO.clearGames();
        Assertions.assertEquals(gameDAO.getGames().size(), 0, "gameDAO wasn't cleared");

        // test clearing them again once empty
        userDAO.clearUsers();
        Assertions.assertEquals(userDAO.getUsers().size(), 0, "userDAO wasn't cleared after empty");
        authDAO.clearTokens();
        Assertions.assertEquals(authDAO.getTokens().size(), 0, "authDAO wasn't cleared after empty");
        gameDAO.clearGames();
        Assertions.assertEquals(gameDAO.getGames().size(), 0, "gameDAO wasn't cleared after empty");
    }

    @Test
    @DisplayName("Clear With Service")
    void clearWithService() throws DataAccessException {
        // DAO's are all populated with 100 objects each upon setup.
        clearService.clear();
        Assertions.assertEquals(userDAO.getUsers().size(), 0, "userDAO wasn't cleared");
        Assertions.assertEquals(authDAO.getTokens().size(), 0, "authDAO wasn't cleared");
        Assertions.assertEquals(gameDAO.getGames().size(), 0, "gameDAO wasn't cleared");
    }
}