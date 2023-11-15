package server;

import facade.ServerFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.LoginRegisterResult;

import javax.xml.crypto.Data;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServerFacadeTest {

    private ServerFacade serverFacade;

    @BeforeEach
    void setUp() throws IOException {
        serverFacade = new ServerFacade();
        serverFacade.clear();
    }

    @Test
    @DisplayName("Successful Registration")
    public void registerSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        LoginRegisterResult exptectedResult = new LoginRegisterResult(null, "someToken", "Trevor");
        LoginRegisterResult actualResult = serverFacade.register(request);

        Assertions.assertEquals(exptectedResult.getUsername(), actualResult.getUsername(), "User wasn't created properly");
        Assertions.assertNotNull(actualResult.getAuthToken(), "Registered user wasn't given an authToken");
    }

    @Test
    @DisplayName("Registration Failure")
    public void registerFailure() throws IOException {
        // make duplicate login requests
        RegisterRequest request = new RegisterRequest("Trevor", "Bond", "email@gmail.com");
        serverFacade.register(request);
        Assertions.assertThrows(IOException.class, () -> serverFacade.register(request));
    }
}