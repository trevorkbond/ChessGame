package unitTests.DAOTests;

import com.mysql.cj.log.Log;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.LogoutService;
import services.RegisterService;
import services.request.RegisterRequest;
import unitTests.UnitTests;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest extends UnitTests {

    private HashSet<AuthToken> testTokens;

    @BeforeEach
    void setup() throws SQLException, DataAccessException {
        initializeAndClearDAOs();
        testTokens = new HashSet<>();
    }
    @Test
    @DisplayName("Successful Add Token")
    void addTokenSuccess() throws DataAccessException, SQLException {
        authDAO.addToken(new AuthToken("test", "lametoken"));
        testTokens.add(new AuthToken("test", "lametoken"));
        Assertions.assertEquals(testTokens, authDAO.getTokens(), "AuthTokens not added");

        // try through register
        RegisterService registerService = new RegisterService();
        registerService.register(new RegisterRequest("un", "pw", "email@gmail.com"), userDAO, authDAO);
        Assertions.assertEquals(2, authDAO.getTokens().size(), "AuthToken not added through Register");
    }

    @Test
    @DisplayName("Add Token Failure")
    void addTokenFailure() throws DataAccessException {
        // try to add duplicate tokens
        authDAO.addToken(new AuthToken("test", "lametoken"));
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.addToken(new AuthToken("test", "lametoken")));

        // try to add with null authToken value
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.addToken(
                new AuthToken(null, null)));
    }

    @Test
    @DisplayName("Delete Token Success")
    void deleteTokenSuccess() throws DataAccessException, SQLException {
        authDAO.addToken(new AuthToken("test", "lametoken"));
        authDAO.deleteToken(new AuthToken("test", "lametoken"));
        Assertions.assertEquals(0, authDAO.getTokens().size(), "Token not deleted properly");

        // try through register and logout
        RegisterService registerService = new RegisterService();
        LogoutService logoutService = new LogoutService();
        registerService.register(new RegisterRequest("we", "are", "farmers"), userDAO, authDAO);
        HashSet<AuthToken> tokens = authDAO.getTokens();
        for (AuthToken token : tokens) {
            logoutService.logout(token, authDAO);
        }
        Assertions.assertEquals(0, authDAO.getTokens().size(), "didn't logout properly");
    }

    @Test
    @DisplayName("Delete Token Failure")
    void deleteTokenFailure() {
        // try to delete a token not in there
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.deleteToken(new AuthToken("test", "lametoken")));
        // I don't know what authTokens were added in, but if I try to logout with dummy data it won't work
        LogoutService logoutService = new LogoutService();
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "authToken11"), authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", ""), authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "32u90fnlsfsfa83le,f-"), authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "*******"), authDAO));
    }

    @Test
    @DisplayName("Find Token Success")
    void findTokenSuccess() throws DataAccessException, SQLException {
        authDAO.addToken(new AuthToken("test", "lametoken"));
        AuthToken foundToken = authDAO.findToken(new AuthToken("test", "lametoken"));
        Assertions.assertEquals("lametoken", foundToken.getAuthToken(), "Token not found");

        // try through register
        RegisterService registerService = new RegisterService();
        registerService.register(new RegisterRequest("we", "are", "farmers"), userDAO, authDAO);
        HashSet<AuthToken> tokens = authDAO.getTokens();
        for (AuthToken token : tokens) {
            AuthToken tempToken = authDAO.findToken(new AuthToken("we", token.getAuthToken()));
            Assertions.assertEquals(token.getAuthToken(), tempToken.getAuthToken(), "Token not found");
        }
    }

    @Test
    @DisplayName("Find Token Failure")
    void findTokenFailure() throws DataAccessException {
        // populate with dummy data
        for (int i = 0; i < 100; i++) {
            authDAO.addToken(new AuthToken(UUID.randomUUID().toString().substring(0, 24), UUID.randomUUID().toString().substring(0, 24)));
        }

        // None of these should work - I have no idea what authTokens are in there
        AuthToken isNull = authDAO.findToken(new AuthToken("please", "find"));
        AuthToken emptyToken = authDAO.findToken(new AuthToken("please", ""));
        AuthToken oneMore = authDAO.findToken(new AuthToken("please", "whinf8332osdsmlof"));
        Assertions.assertNull(isNull);
        Assertions.assertNull(emptyToken);
        Assertions.assertNull(oneMore);
    }

    @Test
    void clearTokens() throws DataAccessException {
        for (int i = 0; i < 100; i++) {
            authDAO.addToken(new AuthToken(UUID.randomUUID().toString().substring(0, 24), UUID.randomUUID().toString().substring(0, 24)));
        }

        authDAO.clearTokens();
        Assertions.assertEquals(0, authDAO.getTokens().size(), "Auth database not cleared");
    }
}