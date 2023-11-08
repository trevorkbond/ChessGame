package unitTests.ServiceTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.LoginService;
import services.RegisterService;
import request.LoginRequest;
import request.RegisterRequest;
import unitTests.UnitTest;

import java.sql.SQLException;

class LoginServiceTest extends UnitTest {

    private LoginService loginService;
    private RegisterService registerService;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        initializeAndClearDAOs();
        loginService = new LoginService();
        registerService = new RegisterService();

        // register some users on each startup manually to retain authTokens
        registerService.register(new RegisterRequest("spongebob", "squarepants", "square@pants.net"), userDAO, authDAO);
        registerService.register(new RegisterRequest("patrick", "star", "patrick@star.net"), userDAO, authDAO);
        registerService.register(new RegisterRequest("eugene", "krabs", "eugene@krustykrab.net"), userDAO, authDAO);
    }

    @Test
    @DisplayName("Login Success Tests")
    void loginSuccess() throws DataAccessException, SQLException {
        // clear out all authTokens to log all users out
        authDAO.clearTokens();

        loginService.login(new LoginRequest("spongebob", "squarepants"), userDAO, authDAO);
//        Assertions.assertEquals(authDAO.getTokens().size(), 1, "User wasn't logged in");
        loginService.login(new LoginRequest("patrick", "star"), userDAO, authDAO);
//        Assertions.assertEquals(authDAO.getTokens().size(), 2, "User wasn't logged in");
        loginService.login(new LoginRequest("eugene", "krabs"), userDAO, authDAO);
//        Assertions.assertEquals(authDAO.getTokens().size(), 3, "User wasn't logged in");

        // test logging out and then logging in again
//        authDAO.getTokens().removeIf(auth -> auth.getUsername().equals("spongebob"));
        loginService.login(new LoginRequest("spongebob", "squarepants"), userDAO, authDAO);
//        Assertions.assertEquals(authDAO.getTokens().size(), 3, "User wasn't logged in after logging out");
    }

    @Test
    @DisplayName("Login Failure Tests")
    void loginFailure() throws DataAccessException, SQLException {
        // clear out all authTokens to log all users out
        authDAO.clearTokens();

        // incorrect username
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("spongbob", "squarepants"), userDAO, authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("", "krabs"), userDAO, authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("patrick ", "star"), userDAO, authDAO));

        // incorrect password
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("spongebob", "squrepants"), userDAO, authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("patrick", ""), userDAO, authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("eugene", "krabs "), userDAO, authDAO));
    }

}