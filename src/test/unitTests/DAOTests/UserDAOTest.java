package unitTests.DAOTests;

import dataAccess.DataAccessException;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.RegisterService;
import services.request.RegisterRequest;
import unitTests.UnitTests;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest extends UnitTests {

    private HashSet<User> testUsers;

    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        initializeAndClearDAOs();
        testUsers = new HashSet<>();
    }

    @Test
    @DisplayName("Successful User Creation")
    void createUserSuccess() throws DataAccessException, SQLException {
        userDAO.createUser(new User("spongebob", "squarepants", "garycomehome@gmail.com"));
        testUsers.add(new User("spongebob", "squarepants", "garycomehome@gmail.com"));
        Assertions.assertEquals(testUsers, userDAO.getAllUsers(), "User not added to database");
        Assertions.assertEquals(1, userDAO.getAllUsers().size(), "User not added to database");

        // test through RegisterService
        RegisterService registerService = new RegisterService();
        registerService.register(new RegisterRequest("patrick", "star", "liveunderarock@gmail.com"),
                userDAO, authDAO);
        testUsers.add(new User("patrick", "star", "liveunderarock@gmail.com"));
        Assertions.assertEquals(testUsers, userDAO.getAllUsers(), "User not added through register");
        Assertions.assertEquals(2, userDAO.getAllUsers().size(), "User not added through register");
    }

    @Test
    @DisplayName("User Creation Failure")
    void createUserFailure() throws DataAccessException, SQLException {
        // try adding duplicate users
        userDAO.createUser(new User("spongebob", "squarepants", "garycomehome@gmail.com"));
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(new User("spongebob",
                "squarepants", "garycomehome@gmail.com")));
        // adding with same primary key (username)
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(new User("spongebob",
                "circlepants", "krustykrab@gmail.com")));
        // ensure any attempt with empty fields will be rejected
        RegisterService registerService = new RegisterService();
        Assertions.assertThrows(DataAccessException.class, () -> registerService.register(new RegisterRequest(null ,
                "squarepants", "garycomehome@gmail.com"), userDAO, authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> registerService.register(new RegisterRequest("spongebob",
                null, "garycomehome@gmail.com"), userDAO, authDAO));
        Assertions.assertThrows(DataAccessException.class, () -> registerService.register(new RegisterRequest("spongebob",
                "squarepants", null), userDAO, authDAO));
    }

    @Test
    @DisplayName("Find User Success")
    void findUserSuccess() throws DataAccessException, SQLException {
        userDAO.createUser(new User("spongebob", "squarepants", "garycomehome@gmail.com"));
        User temp = new User("spongebob", "squarepants", "garycomehome@gmail.com");
        Assertions.assertEquals(userDAO.findUser("spongebob"), temp, "User not found");

        // try through register and then find
        RegisterService registerService = new RegisterService();
        registerService.register(new RegisterRequest("patrick", "star", "liveunderarock@gmail.com"),
                userDAO, authDAO);
        User patrick = new User("patrick", "star", "liveunderarock@gmail.com");
        Assertions.assertEquals(userDAO.findUser("patrick"), patrick, "User not added through Register");
    }

    @Test
    @DisplayName("Clear Users")
    void clearUsers() throws DataAccessException {
        // populate with dummy data
        for (int i = 0; i < 100; i++) {
            userDAO.createUser(new User(UUID.randomUUID().toString().substring(0, 24), UUID.randomUUID().toString().substring(0, 24)
                    , UUID.randomUUID().toString().substring(0, 24)));
        }
        userDAO.clearUsers();
        Assertions.assertEquals(0, userDAO.getAllUsers().size(), "Users not cleared");
    }
}