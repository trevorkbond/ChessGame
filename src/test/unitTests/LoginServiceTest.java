package unitTests;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ClearService;
import services.LoginService;
import services.RegisterService;
import services.request.LoginRequest;
import services.request.RegisterRequest;

class LoginServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private LoginService loginService;
    private RegisterService registerService;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = UserDAO.getInstance();
        authDAO = AuthDAO.getInstance();
        loginService = new LoginService();
        registerService = new RegisterService();
        ClearService clearService = new ClearService();
        clearService.clear();

        // register some users on each startup manually to retain authTokens
        registerService.register(new RegisterRequest("spongebob", "squarepants", "square@pants.net"));
        registerService.register(new RegisterRequest("patrick", "star", "patrick@star.net"));
        registerService.register(new RegisterRequest("eugene", "krabs", "eugene@krustykrab.net"));
    }

    @Test
    @DisplayName("Login Success Tests")
    void loginSuccess() throws DataAccessException {
        // clear out all authTokens to log all users out
        authDAO.clearTokens();

        loginService.login(new LoginRequest("spongebob", "squarepants"));
        Assertions.assertEquals(authDAO.getTokens().size(), 1, "User wasn't logged in");
        loginService.login(new LoginRequest("patrick", "star"));
        Assertions.assertEquals(authDAO.getTokens().size(), 2, "User wasn't logged in");
        loginService.login(new LoginRequest("eugene", "krabs"));
        Assertions.assertEquals(authDAO.getTokens().size(), 3, "User wasn't logged in");

        // test logging out and then logging in again
        authDAO.getTokens().removeIf(auth -> auth.getUsername().equals("spongebob"));
        loginService.login(new LoginRequest("spongebob", "squarepants"));
        Assertions.assertEquals(authDAO.getTokens().size(), 3, "User wasn't logged in after logging out");
    }

    @Test
    @DisplayName("Login Failure Tests")
    void loginFailure() throws DataAccessException {
        // clear out all authTokens to log all users out
        authDAO.clearTokens();

        // incorrect username
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("spongbob", "squarepants")));
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("", "krabs")));
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("patrick ", "star")));

        // incorrect password
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("spongebob", "squrepants")));
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("patrick", "")));
        Assertions.assertThrows(DataAccessException.class, () -> loginService.login(new LoginRequest("eugene", "krabs ")));
    }
}