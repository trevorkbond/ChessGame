package unitTests.ServiceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.JoinGameRequest;
import services.JoinGameService;
import unitTests.UnitTest;

import java.sql.SQLException;

class JoinGameServiceTest extends UnitTest {

    private JoinGameService joinGameService;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        initializeAndClearDAOs();
        joinGameService = new JoinGameService();
        gameDAO.resetIDCounter();

        // add a few new games, some with players and some without spots reserved
        gameDAO.createGame(new Game("noTeamsTaken"));
        gameDAO.createGame(new Game("blackTeamTaken"));
        gameDAO.createGame(new Game("whiteTeamTaken"));

        // get valid authToken added
        authDAO.addToken(new AuthToken("token", "authToken"));
    }

    @Test
    @DisplayName("Join Game Success")
    void joinGameSuccess() throws DataAccessException {
        // ensure both spots can be joined in nospotsreserved
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), "authToken", authDAO, gameDAO));
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), "authToken", authDAO, gameDAO));

        // ensure white team can be joined in blackTeamTaken after add to blackTeam
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 2), "authToken", authDAO, gameDAO));
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 2), "authToken", authDAO, gameDAO));

        // ensure black team can be joined in whiteTeamTaken after add to whiteTeam
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 3), "authToken", authDAO, gameDAO));
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 3), "authToken", authDAO, gameDAO));

//         ensure team spots are actually taken for all
        Assertions.assertNotNull(gameDAO.findGame(1).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(1).getBlackUsername());
        Assertions.assertNotNull(gameDAO.findGame(2).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(2).getBlackUsername());
        Assertions.assertNotNull(gameDAO.findGame(3).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(3).getBlackUsername());
    }

    @Test
    @DisplayName("Join Game Failure")
    void joinGameFailure() throws DataAccessException {
        // ensure black can't be taken in blackTeamTaken
        joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 2), "authToken", authDAO, gameDAO);
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 2), "authToken", authDAO, gameDAO));

        // ensure white can't be taken in whiteTeamTaken
        joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 3), "authToken", authDAO, gameDAO);
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 3), "authToken", authDAO, gameDAO));

        // ensure request with invalid ID can't join a team
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 0), "authToken", authDAO, gameDAO));

        // ensure request with invalid authToken can't join game
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 2), "authTokenWrong", authDAO, gameDAO));

        // ensure neither can be taken in blackTeamTaken after someone joins whiteTeam
        joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 2), "authToken", authDAO, gameDAO);
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 2), "authToken", authDAO, gameDAO));
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 2), "authToken", authDAO, gameDAO));

    }

}