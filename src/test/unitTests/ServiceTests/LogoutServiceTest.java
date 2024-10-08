package unitTests.ServiceTests;

import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import services.LogoutService;
import services.RegisterService;
import unitTests.UnitTest;

import java.sql.SQLException;
import java.util.HashSet;

class LogoutServiceTest extends UnitTest {
    private LogoutService logoutService;
    private RegisterService registerService;
    private HashSet<AuthToken> authTokens;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        initializeAndClearDAOs();
        logoutService = new LogoutService();
        registerService = new RegisterService();
        authTokens = new HashSet<>();

        // register some users on each startup
        registerService.register(new RegisterRequest("spongebob", "squarepants", "square@pants.net"), userDAO, authDAO);
        registerService.register(new RegisterRequest("patrick", "star", "patrick@star.net"), userDAO, authDAO);
        registerService.register(new RegisterRequest("eugene", "krabs", "eugene@krustykrab.net"), userDAO, authDAO);

        // get all of the authTokens that were just added upon registration
        authTokens.addAll(authDAO.getTokens());
    }

    @Test
    @DisplayName("Logout Success")
    void logoutSuccess() throws DataAccessException, SQLException {
        // test logging out each token in the valid token set
        for (AuthToken token : authTokens) {
            logoutService.logout(token, authDAO);
        }
        Assertions.assertEquals(authDAO.getTokens().size(), 0, "Users weren't logged out properly");
    }

    @Test
    @DisplayName("Logout Failure")
    void logoutFailure() throws DataAccessException {
        // I don't know what authTokens were added in, but if I try to logout with dummy data it won't work
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "authToken11"), authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", ""), authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "32u90fnlsfsfa83le,f-"), authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "*******"), authDAO));
    }

}