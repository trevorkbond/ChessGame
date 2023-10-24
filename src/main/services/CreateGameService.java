package services;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import services.request.CreateGameRequest;
import services.result.CreateGameResult;
import spark.Request;

/**
 * CreateGameService implements the create game API functionality
 */
public class CreateGameService {
    /**
     * CreateGameService has access to games in database via DAO
     */
    private GameDAO gameDAO;

    /**
     * CreateGameService needs access to authTokens to verify Users are authorized to perform actions
     */
    private AuthDAO authDAO;

    /**
     * Constructor for service that gets instance of GameDAO
     */
    public CreateGameService() {
        gameDAO = GameDAO.getInstance();
        authDAO = AuthDAO.getInstance();
    }

    /**
     * Creates a game from a CreateGameRequest
     *
     * @param request the given request
     * @param request the given request
     * @return the CreateGameResult of the operation
     */
    public CreateGameResult createGame(CreateGameRequest request, Request httpRequest) throws DataAccessException {
        authDAO.findToken(new AuthToken(null, httpRequest.headers("Authorization")));
        Game addGame = new Game(request.getGameName());
        gameDAO.createGame(addGame);
        return new CreateGameResult(null, addGame.getGameID());
    }
}
