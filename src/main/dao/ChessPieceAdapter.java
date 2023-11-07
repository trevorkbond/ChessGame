package dao;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
    public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject jsonObject = el.getAsJsonObject();
        String pieceType = jsonObject.get("type").getAsString();
        String trimmedType = pieceType.replaceAll("\"", "");
        switch (trimmedType) {
            case "PAWN":
                return new Gson().fromJson(el, Pawn.class);
            case "KNIGHT":
                return new Gson().fromJson(el, Knight.class);
            case "KING":
                return new Gson().fromJson(el, King.class);
            case "QUEEN":
                return new Gson().fromJson(el, Queen.class);
            case "ROOK":
                return new Gson().fromJson(el, Rook.class);
            case "BISHOP":
                return new Gson().fromJson(el, Bishop.class);
            default:
                return null;
        }
    }
}
