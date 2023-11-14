package unitTests.ServiceTests;

import chess.ChessGameImpl;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ListGamesService;
import unitTests.UnitTest;

import java.sql.SQLException;
import java.util.HashSet;

class ListGamesServiceTest extends UnitTest {

    private ListGamesService listGamesService;
    private HashSet<Game> testGames;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        initializeAndClearDAOs();
        listGamesService = new ListGamesService();
        testGames = new HashSet<>();
        gameDAO.resetIDCounter();

        // add list of test games to hashset
        testGames.add(new Game(new ChessGameImpl(), 1, "whiteTeam", "blackTeam", "test1"));
        testGames.add(new Game(new ChessGameImpl(), 2, "whiteTeam", "blackTeam", "test2"));
        testGames.add(new Game(new ChessGameImpl(), 3, "whiteTeam", "blackTeam", "test3"));

        // add same test games to gameDAO
        gameDAO.createGame(new Game(new ChessGameImpl(), "whiteTeam", "blackTeam", "test1"));
        gameDAO.createGame(new Game(new ChessGameImpl(), "whiteTeam", "blackTeam", "test2"));
        gameDAO.createGame(new Game(new ChessGameImpl(), "whiteTeam", "blackTeam", "test3"));

        // add an authToken to authenticate action
        authDAO.addToken(new AuthToken("username", "complexToken"));
    }

    @Test
    @DisplayName("List Games Success")
    void listGamesSuccess() throws DataAccessException, SQLException {
        // test if games in test set equal to games in DAO retrieved from listGames API
        Assertions.assertEquals(listGamesService.listGames(new AuthToken("username", "complexToken"), "complexToken", authDAO, gameDAO).getGames()
                , testGames, "List games returned different list of games");

        // add a game to both
        testGames.add(new Game(new ChessGameImpl(), 4, "whiteTeam", "blackTeam", "test4"));
        gameDAO.createGame(new Game(new ChessGameImpl(), 4, "whiteTeam", "blackTeam", "test4"));
        Assertions.assertEquals(listGamesService.listGames(new AuthToken("username", "complexToken"), "complexToken", authDAO, gameDAO).getGames()
                , testGames, "List games returned different list of games");

        // clear both and then list
        testGames.clear();
        gameDAO.clearGames();
        Assertions.assertEquals(listGamesService.listGames(new AuthToken("username", "complexToken"), "complexToken", authDAO, gameDAO).getGames()
                , testGames, "List games returned different list of games after clearing");
    }

    @Test
    @DisplayName("List Games Failure")
    void listGamesFailure() {
        // try with multiple invalid authtokens and assure exception is thrown
        Assertions.assertThrows(DataAccessException.class, () -> listGamesService.listGames(new AuthToken("thisone", "doesn't match"),
                "doesn't match", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> listGamesService.listGames(new AuthToken("thisoneisempty", ""),
                "", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> listGamesService.listGames(new AuthToken("", "authToken"),
                "authToken", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> listGamesService.listGames(new AuthToken("", "Complex token"),
                "Complex token", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> listGamesService.listGames(new AuthToken("thisone", "u80jemdi"),
                "u80jemdi", authDAO, gameDAO));
    }

}