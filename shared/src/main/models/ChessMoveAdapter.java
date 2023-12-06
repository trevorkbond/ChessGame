package models;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessMoveImpl;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ChessMoveAdapter implements JsonDeserializer<ChessMove> {

    @Override
    public ChessMove deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        return ctx.deserialize(el, ChessMoveImpl.class);
    }
}
