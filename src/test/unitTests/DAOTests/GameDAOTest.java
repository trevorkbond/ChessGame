package unitTests.DAOTests;

import chess.ChessGame;
import chess.ChessGameImpl;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.CreateGameService;
import services.request.CreateGameRequest;
import unitTests.UnitTest;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

class GameDAOTest extends UnitTest {

    private HashSet<Game> testGames;

    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        initializeAndClearDAOs();
        testGames = new HashSet<>();
        Game.setNextID(1);
    }

    @Test
    @DisplayName("Successful Create Game")
    void createGameSuccess() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(new Game("Trevor")));
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(new Game("Bond")));

        // attempt through services
        authDAO.addToken(new AuthToken("test", "lametoken"));
        CreateGameService service = new CreateGameService();
        Assertions.assertDoesNotThrow(() -> service.createGame(new CreateGameRequest("Kristofer"), "lametoken", authDAO, gameDAO));

        // make sure all got added in
        testGames.add(new Game(new ChessGameImpl(), 1, null, null, "Trevor"));
        testGames.add(new Game(new ChessGameImpl(), 2, null, null, "Bond"));
        testGames.add(new Game(new ChessGameImpl(), 3, null, null, "Kristofer"));
        Assertions.assertEquals(testGames, gameDAO.getGames(), "Games not added");
    }

    @Test
    @DisplayName("Create Game Failure")
    void createGameFailure() throws DataAccessException {
        // check with gameID already taken
        gameDAO.createGame(new Game("Trevor")); // has gameID of 1
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(new Game(
                new ChessGameImpl(), 1, null, null, "Trevor")));

        // check with unauthorized request through service
        authDAO.addToken(new AuthToken("test", "lametoken"));
        CreateGameService service = new CreateGameService();
        Assertions.assertThrows(DataAccessException.class,
                () -> service.createGame(new CreateGameRequest("Kristofer"), "lam1token", authDAO, gameDAO));

    }

    @Test
    @DisplayName("Find Game Success")
    void findGameSuccess() throws DataAccessException {
        // add game
        gameDAO.createGame(new Game("Trevor"));
        Assertions.assertDoesNotThrow(() -> gameDAO.findGame(1));
        Game expectedFoundGame = new Game(new ChessGameImpl(), 1, null, null, "Trevor");
        Assertions.assertEquals(expectedFoundGame, gameDAO.findGame(1), "Correct game not found");
    }

    @Test
    @DisplayName("Find Game Failure")
    void findGameFailure() throws DataAccessException {
        // add game
        gameDAO.createGame(new Game("Trevor"));
        Game nullGame = gameDAO.findGame(2);
        Assertions.assertNull(nullGame);

        // clear games then try to find one that was there
        gameDAO.clearGames();
        nullGame = gameDAO.findGame(1);
        Assertions.assertNull(nullGame);
    }

    @Test
    @DisplayName("Claim Spot Success")
    void claimSpotSuccess() throws DataAccessException {
        gameDAO.createGame(new Game("Trevor"));
        Assertions.assertDoesNotThrow(() -> gameDAO.claimSpot(1, ChessGame.TeamColor.WHITE, "user1"));
        Assertions.assertDoesNotThrow(() -> gameDAO.claimSpot(1, ChessGame.TeamColor.BLACK, "user2"));

        // clear it then do it again
        gameDAO.clearGames();
        gameDAO.createGame(new Game("Trevor"));
        Assertions.assertDoesNotThrow(() -> gameDAO.claimSpot(2, ChessGame.TeamColor.WHITE, "user1"));
        Assertions.assertDoesNotThrow(() -> gameDAO.claimSpot(2, ChessGame.TeamColor.BLACK, "user2"));
    }

    @Test
    @DisplayName("Claim Spot Failure")
    void claimSpotFailure() throws DataAccessException {
        gameDAO.createGame(new Game("Trevor"));
        gameDAO.claimSpot(1, ChessGame.TeamColor.WHITE, "user1");
        gameDAO.claimSpot(1, ChessGame.TeamColor.BLACK, "user2");

        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.claimSpot(1, ChessGame.TeamColor.WHITE, "nada"));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.claimSpot(1, ChessGame.TeamColor.BLACK, "nada"));

        // clear it then do it again
        gameDAO.clearGames();
        gameDAO.createGame(new Game("Trevor"));
        gameDAO.claimSpot(2, ChessGame.TeamColor.WHITE, "user1");
        gameDAO.claimSpot(2, ChessGame.TeamColor.BLACK, "user2");

        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.claimSpot(2, ChessGame.TeamColor.WHITE, "nada"));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.claimSpot(2, ChessGame.TeamColor.BLACK, "nada"));

    }

    @Test
    @DisplayName("Clear Games")
    void clearGames() throws DataAccessException {
        for (int i = 0; i < 100; i++) {
            gameDAO.createGame(new Game(UUID.randomUUID().toString().substring(0, 24)));
        }

        gameDAO.clearGames();
        Assertions.assertEquals(0, gameDAO.getGames().size(), "Games not cleared");
    }
}