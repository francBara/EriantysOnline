package it.polimi.ingsw.model.Game;
import com.google.gson.Gson;

/**
 * Helper class to easily serialize and deserialize a game.
 * @author Francesco Barabino
 */
public class GameSerializer {
    public String serialize(Game game) {
        Gson gson = new Gson();
        game.cacheCharacters();
        return gson.toJson(game);
    }

    public Game deserialize(String gameJSON) {
        Gson gson = new Gson();
        Game game = gson.fromJson(gameJSON, Game.class);
        game.buildCharacters();
        return game;
    }
}
