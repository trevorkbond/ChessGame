package unitTests;

import dao.AuthDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ClearService;
import services.LogoutService;
import services.RegisterService;
import services.request.RegisterRequest;

import javax.xml.crypto.Data;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {

    private AuthDAO authDAO;
    private LogoutService logoutService;
    private RegisterService registerService;
    private HashSet<AuthToken> authTokens;

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO = AuthDAO.getInstance();
        logoutService = new LogoutService();
        registerService = new RegisterService();
        authTokens = new HashSet<>();
        ClearService clearService = new ClearService();
        clearService.clear();

        // register some users on each startup
        registerService.register(new RegisterRequest("spongebob", "squarepants", "square@pants.net"));
        registerService.register(new RegisterRequest("patrick", "star", "patrick@star.net"));
        registerService.register(new RegisterRequest("eugene", "krabs", "eugene@krustykrab.net"));

        // get all of the authTokens that were just added upon registration
        authTokens.addAll(authDAO.getTokens());
    }

    @Test
    @DisplayName("Logout Success")
    void logoutSuccess() throws DataAccessException {
        // test logging out each token in the valid token set
        for (AuthToken token : authTokens) {
            logoutService.logout(token);
        }
        Assertions.assertEquals(authDAO.getTokens().size(), 0, "Users weren't logged out properly");
    }

    @Test
    @DisplayName("Logout Failure")
    void logoutFailure() throws DataAccessException {
        // I don't know what authTokens were added in, but if I try to logout with dummy data it won't work
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "authToken11")));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "")));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "32u90fnlsfsfa83le,f-")));
        Assertions.assertThrows(DataAccessException.class, () -> logoutService.logout(
                new AuthToken("Test", "*******")));
    }
}