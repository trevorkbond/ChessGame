package unitTests;

import dao.AuthDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import models.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.RegisterService;
import services.request.RegisterRequest;

import java.util.HashSet;

class RegisterServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private RegisterService registerService;

    @BeforeEach
    void setUp() {
        userDAO = UserDAO.getInstance();
        authDAO = AuthDAO.getInstance();
        registerService = new RegisterService();
    }

    /**
     * This test adds multiple users to a HashSet and tests to see if the actual UserDAO set of users is equal after
     * registering users via the register method. Also tests a set of authTokens with authDAO to see if equal
     */
    @Test
    @DisplayName("Register multiple users")
    void registerMultipleUsers() throws DataAccessException {
        registerService.register(new RegisterRequest("olivercow123", "susquehanna1830", "oliver@restoration.com"));
        registerService.register(new RegisterRequest("lucymacksmith", "vermontiscool", "lucy@restoration.com"));
        registerService.register(new RegisterRequest("porterrockwell", "moderndaysam", "porter@restoration.com"));
        registerService.register(new RegisterRequest("brotherbrigham", "ihavealongbeard", "brigham@restoration.com"));
        registerService.register(new RegisterRequest("joesmith", "pillaroflight", "joe@restoration.com"));

        HashSet<User> testUsers = new HashSet<>();
        testUsers.add(new User("olivercow123", "susquehanna1830", "oliver@restoration.com"));
        testUsers.add(new User("lucymacksmith", "vermontiscool", "lucy@restoration.com"));
        testUsers.add(new User("porterrockwell", "moderndaysam", "porter@restoration.com"));
        testUsers.add(new User("brotherbrigham", "ihavealongbeard", "brigham@restoration.com"));
        testUsers.add(new User("joesmith", "pillaroflight", "joe@restoration.com"));

        Assertions.assertEquals(userDAO.getUsers(), testUsers, "Users weren't properly registered and added into the database");
        Assertions.assertEquals(authDAO.getTokens().size(), 5, "There should be 5 AuthTokens in the database " +
                "after 5 users registered");

    }

    @Test
    @DisplayName("Register duplicate users")
    void registerDuplicateUsers() throws DataAccessException {
        registerService.register(new RegisterRequest("olivercow123", "susquehanna1830", "oliver@restoration.com"));
        registerService.register(new RegisterRequest("lucymacksmith", "vermontiscool", "lucy@restoration.com"));
        registerService.register(new RegisterRequest("porterrockwell", "moderndaysam", "porter@restoration.com"));

        // the following three calls to register should throw an error due to duplication
        Assertions.assertThrows(DataAccessException.class, () -> registerService.register(
                new RegisterRequest("olivercow123", "susquehanna1830", "oliver@restoration.com")));
        Assertions.assertThrows(DataAccessException.class, () -> registerService.register(
                new RegisterRequest("lucymacksmith", "vermontiscool", "lucy@restoration.com")));
        Assertions.assertThrows(DataAccessException.class, () -> registerService.register(
                new RegisterRequest("porterrockwell", "moderndaysam", "porter@restoration.com")));

    }
}