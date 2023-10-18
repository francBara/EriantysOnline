package it.polimi.ingsw.model.Cards.Characters.CharacterTypes;

import it.polimi.ingsw.model.Cards.Characters.CharacterRequirements.FactionRequirement;
import it.polimi.ingsw.model.Cards.Characters.CharacterRequirements.IslandRequirement;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;
import org.json.simple.JSONObject;

import java.util.function.BiConsumer;

/**
 * A character which requires a Faction and an Island for his effect to work.
 * @author Francesco Barabino
 */
public class FactionIslandCharacter extends CharacterCard {
    private final BiConsumer<Faction, Island> effect;
    private final BiConsumer<Faction, Students> endEffect;


    FactionIslandCharacter(int id, String name, String description, int cost, Students students, BiConsumer<Faction, Island> effect, BiConsumer<Faction, Students> endEffect) {
        super(id, name, description, cost, students);
        this.effect = effect;
        this.endEffect = endEffect;
        factionRequirement = new FactionRequirement(students.getFactions());
        islandRequirement = new IslandRequirement();
    }


    public void useEffect(Player player) {
        effect.accept(factionRequirement.getFaction(), islandRequirement.getIsland());
        endEffect.accept(factionRequirement.getFaction(), students);
        setUsed();
    }
}
