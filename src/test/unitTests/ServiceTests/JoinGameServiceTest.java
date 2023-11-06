package unitTests.ServiceTests;

import chess.ChessGame;
import chess.ChessGameImpl;
import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import org.junit.jupiter.api.*;
import server.Server;
import services.ClearService;
import services.JoinGameService;
import services.ListGamesService;
import services.request.JoinGameRequest;
import unitTests.UnitTests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest extends UnitTests {

    private JoinGameService joinGameService;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        initializeAndClearDAOs();
        joinGameService = new JoinGameService();

        // add a few new games, some with players and some without spots reserved
        gameDAO.createGame(new Game(new ChessGameImpl(), 99, null, null, "noTeamsTaken"));
        gameDAO.createGame(new Game(new ChessGameImpl(), 100, null, null, "blackTeamTaken"));
        gameDAO.createGame(new Game(new ChessGameImpl(), 101, null, null, "whiteTeamTaken"));

        // get valid authToken added
        authDAO.addToken(new AuthToken("token", "authToken"));
    }

    @Test
    @DisplayName("Join Game Success")
    void joinGameSuccess() throws DataAccessException {
        // ensure both spots can be joined in nospotsreserved
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 99), "authToken", authDAO, gameDAO));
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 99), "authToken", authDAO, gameDAO));

        // ensure white team can be joined in blackTeamTaken after add to blackTeam
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 100), "authToken", authDAO, gameDAO));
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "authToken", authDAO, gameDAO));

        // ensure black team can be joined in whiteTeamTaken after add to whiteTeam
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 101), "authToken", authDAO, gameDAO));
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 101), "authToken", authDAO, gameDAO));

//         ensure team spots are actually taken for all
        Assertions.assertNotNull(gameDAO.findGame(99).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(99).getBlackUsername());
        Assertions.assertNotNull(gameDAO.findGame(100).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(100).getBlackUsername());
        Assertions.assertNotNull(gameDAO.findGame(101).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(101).getBlackUsername());
    }

    @Test
    @DisplayName("Join Game Failure")
    void joinGameFailure() throws DataAccessException {
        // ensure black can't be taken in blackTeamTaken
        joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 100), "authToken", authDAO, gameDAO);
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 100), "authToken", authDAO, gameDAO));

        // ensure white can't be taken in whiteTeamTaken
        joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 101), "authToken", authDAO, gameDAO);
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 101), "authToken", authDAO, gameDAO));

        // ensure request with invalid ID can't join a team
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 0), "authToken", authDAO, gameDAO));

        // ensure request with invalid authToken can't join game
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "authTokenWrong", authDAO, gameDAO));

        // ensure neither can be taken in blackTeamTaken after someone joins whiteTeam
        joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "authToken", authDAO, gameDAO);
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 100), "authToken", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "authToken", authDAO, gameDAO));

    }

}