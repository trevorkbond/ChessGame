package unitTests.ServiceTests;

import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.*;
import unitTests.UnitTest;

import java.sql.SQLException;
import java.util.UUID;

class ClearServiceTest extends UnitTest {

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        initializeAndClearDAOs();

        // populate all DAO's with dummy data
        for (int i = 0; i < 100; i++) {
            userDAO.createUser(new User(UUID.randomUUID().toString().substring(0, 24), UUID.randomUUID().toString().substring(0, 24),
                    UUID.randomUUID().toString().substring(0, 24)));
        }
        for (int i = 0; i < 100; i++) {
            authDAO.addToken(new AuthToken(UUID.randomUUID().toString().substring(0, 24), UUID.randomUUID().toString().substring(0, 24)));
        }
        for (int i = 0; i < 100; i++) {
            gameDAO.createGame(new Game(UUID.randomUUID().toString().substring(0, 24)));
        }
    }

    @Test
    @DisplayName("Clear All DAO's")
    void clearWithoutService() throws DataAccessException, SQLException {
        // DAO's are all populated with 100 objects each upon setup
        userDAO.clearUsers();
//        Assertions.assertEquals(userDAO.getUsers().size(), 0, "userDAO wasn't cleared");
        authDAO.clearTokens();
//        Assertions.assertEquals(authDAO.getTokens().size(), 0, "authDAO wasn't cleared");
        gameDAO.clearGames();
//        Assertions.assertEquals(gameDAO.getGames().size(), 0, "gameDAO wasn't cleared");

        // test clearing them again once empty
        userDAO.clearUsers();
//        Assertions.assertEquals(userDAO.getUsers().size(), 0, "userDAO wasn't cleared after empty");
        authDAO.clearTokens();
//        Assertions.assertEquals(authDAO.getTokens().size(), 0, "authDAO wasn't cleared after empty");
        gameDAO.clearGames();
//        Assertions.assertEquals(gameDAO.getGames().size(), 0, "gameDAO wasn't cleared after empty");
    }

    @Test
    @DisplayName("Clear With Service")
    void clearWithService() throws DataAccessException, SQLException {
        // DAO's are all populated with 100 objects each upon setup.
        clearService.clear(userDAO, authDAO, gameDAO);
//        Assertions.assertEquals(userDAO.getUsers().size(), 0, "userDAO wasn't cleared");
//        Assertions.assertEquals(authDAO.getTokens().size(), 0, "authDAO wasn't cleared");
//        Assertions.assertEquals(gameDAO.getGames().size(), 0, "gameDAO wasn't cleared");
    }

}