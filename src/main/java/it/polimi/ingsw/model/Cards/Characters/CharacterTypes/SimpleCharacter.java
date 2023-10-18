package it.polimi.ingsw.model.Cards.Characters.CharacterTypes;

import it.polimi.ingsw.model.Player.Player;
import org.json.simple.JSONObject;

import java.util.function.Consumer;

/**
 * A simple character with no requirements for his effect to work.
 * @author Francesco Barabino
 */
public class SimpleCharacter extends CharacterCard {
    Consumer<Player> effect;

    SimpleCharacter(int id, String name, String description, int cost, Consumer<Player> effect) {
        super(id, name, description, cost);
        this.effect = effect;
    }

    public void useEffect(Player player) {
        effect.accept(player);
        setUsed();
    }
}
