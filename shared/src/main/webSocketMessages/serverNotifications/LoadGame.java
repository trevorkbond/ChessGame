package webSocketMessages.serverNotifications;

import chess.ChessGameImpl;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {
    private ChessGameImpl game;
    public LoadGame(ServerMessageType type, ChessGameImpl game) {
        super(type);
        this.game = game;
    }
    public ChessGameImpl getGame() {
        return game;
    }
}
