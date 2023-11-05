package unitTests;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.DataAccessException;
import server.Server;
import services.ClearService;

import java.sql.Connection;
import java.sql.SQLException;


public class UnitTests {
    public Connection connection;
    public UserDAO userDAO;
    public AuthDAO authDAO;
    public GameDAO gameDAO;
    public ClearService clearService;

    public void initializeAndClearDAOs() throws SQLException, DataAccessException {
        connection = Server.database.getConnection();
        userDAO = new UserDAO(connection);
        authDAO = new AuthDAO(connection);
        gameDAO = new GameDAO(connection);
        clearService = new ClearService();
        clearService.clear(userDAO, authDAO, gameDAO);
    }


}
