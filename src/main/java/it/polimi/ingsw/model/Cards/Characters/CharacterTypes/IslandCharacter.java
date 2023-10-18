package it.polimi.ingsw.model.Cards.Characters.CharacterTypes;

import it.polimi.ingsw.model.Cards.Characters.Exceptions.IslandRequiredException;
import it.polimi.ingsw.model.Cards.Characters.CharacterRequirements.IslandRequirement;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Player.Player;
import org.json.simple.JSONObject;

import java.util.function.Consumer;

/**
 * A character which requires an Island for his effect to work.
 * @author Francesco Barabino
 */
public class IslandCharacter extends CharacterCard {
    private final Consumer<Island> effect;

    public IslandCharacter(int id, String name, String description, int cost, Consumer<Island> effect) {
        super(id, name, description, cost);
        this.effect = effect;
        this.islandRequirement = new IslandRequirement();
    }

    public void useEffect(Player player) throws IslandRequiredException {
        if (islandRequirement.getIsland() == null) {
            throw(new IslandRequiredException());
        }
        effect.accept(islandRequirement.getIsland());
        setUsed();
    }
}
