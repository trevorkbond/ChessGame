package dao;

import chess.ChessBoard;
import chess.ChessBoardImpl;
import chess.ChessPiece;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessBoardAdapter implements JsonDeserializer<ChessBoard> {
    public ChessBoard deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        Gson gson = builder.create();
        return gson.fromJson(el, ChessBoardImpl.class);
    }
}
