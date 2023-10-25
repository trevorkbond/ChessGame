package unitTests;

import chess.ChessGame;
import chess.ChessGameImpl;
import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ClearService;
import services.JoinGameService;
import services.ListGamesService;
import services.request.JoinGameRequest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest {

    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private JoinGameService joinGameService;

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = GameDAO.getInstance();
        authDAO = AuthDAO.getInstance();
        joinGameService = new JoinGameService();
        ClearService clearService = new ClearService();
        clearService.clear();

        // add a few new games, some with players and some without spots reserved
        gameDAO.createGame(new Game("nospotsreserved"));
        gameDAO.createGame(new Game(new ChessGameImpl(), 100, null, "blackTeam", "blackTeamTaken"));
        gameDAO.createGame(new Game(new ChessGameImpl(), 101, "whiteTeam", null, "whiteTeamTaken"));

        // get valid authToken added
        authDAO.addToken(new AuthToken("token", "authToken"));
    }

    @Test
    @DisplayName("Join Game Success")
    void joinGameSuccess() throws DataAccessException {
        // ensure both spots can be joined in nospotsreserved
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), "authToken"));
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), "authToken"));

        // ensure white team can be joined in blackTeamTaken
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "authToken"));

        // ensure black team can be joined in whiteTeamTaken
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 101), "authToken"));

        // ensure team spots are actually taken for all
        Assertions.assertNotNull(gameDAO.findGame(1).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(1).getBlackUsername());
        Assertions.assertNotNull(gameDAO.findGame(100).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(100).getBlackUsername());
        Assertions.assertNotNull(gameDAO.findGame(101).getWhiteUsername());
        Assertions.assertNotNull(gameDAO.findGame(101).getBlackUsername());
    }

    @Test
    @DisplayName("Join Game Failure")
    void joinGameFailure() throws DataAccessException {
        // ensure black can't be taken in blackTeamTaken
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 100), "authToken"));

        // ensure white can't be taken in whiteTeamTaken
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 101), "authToken"));

        // ensure request with invalid ID can't join a team
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 99), "authToken"));

        // ensure request with invalid authToken can't join game
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "authTokenWrong"));

        // ensure neither can be taken in blackTeamTaken after someone joins whiteTeam
        joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "authToken");
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 100), "authToken"));
        Assertions.assertThrows(DataAccessException.class, () -> joinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 100), "authToken"));

    }
}