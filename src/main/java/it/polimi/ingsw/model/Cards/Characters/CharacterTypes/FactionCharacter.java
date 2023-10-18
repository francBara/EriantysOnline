package it.polimi.ingsw.model.Cards.Characters.CharacterTypes;

import it.polimi.ingsw.model.Cards.Characters.Exceptions.FactionRequiredException;
import it.polimi.ingsw.model.Cards.Characters.CharacterRequirements.FactionRequirement;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Player.Player;
import org.json.simple.JSONObject;

import java.util.function.Consumer;

/**
 * A character which requires a Faction for his effect to work.
 * @author Francesco Barabino
 */
public class FactionCharacter extends CharacterCard {
    private final Consumer<Faction> effect;

    FactionCharacter(int id, String name, String description, int cost, Consumer<Faction> effect) {
        super(id, name, description, cost);
        this.effect = effect;
        this.factionRequirement = new FactionRequirement();
    }

    public void useEffect(Player player) throws FactionRequiredException {
        if (factionRequirement.getFaction() == null) {
            throw(new FactionRequiredException());
        }
        effect.accept(factionRequirement.getFaction());
        setUsed();
    }
}
