package services;

import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.Game;
import services.request.CreateGameRequest;
import services.result.CreateGameResult;

/**
 * CreateGameService implements the create game API functionality
 */
public class CreateGameService {

    /**
     * Creates a game from a CreateGameRequest
     *
     * @param request     the given request
     * @param httpRequest the given request
     * @return the CreateGameResult of the operation
     */
    public CreateGameResult createGame(CreateGameRequest request, String httpRequest, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        if (authDAO.findToken(new AuthToken(null, httpRequest)) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        Game addGame = new Game(request.getGameName());
        gameDAO.createGame(addGame);
        return new CreateGameResult(null, addGame.getGameID());
    }
}
