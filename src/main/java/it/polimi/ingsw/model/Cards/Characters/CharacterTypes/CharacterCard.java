package it.polimi.ingsw.model.Cards.Characters.CharacterTypes;

import it.polimi.ingsw.model.Cards.Characters.CharacterRequirements.FactionRequirement;
import it.polimi.ingsw.model.Cards.Characters.CharacterRequirements.IslandRequirement;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;
import org.json.simple.JSONObject;

/**
 * The character card, which can apply effects to game and players.
 * @author Francesco Barabino
 */
public abstract class CharacterCard {
    public final int id;
    private final String name;
    private final String description;
    private final int cost;
    private boolean wasUsed = false;
    protected IslandRequirement islandRequirement;
    protected FactionRequirement factionRequirement;

    protected Students students;

    CharacterCard(int id, String name, String description, int cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }
    CharacterCard(int id, String name, String description, int cost, Students students) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.students = students;
    }

    /**
     * Indicates that the character was used, if so, the cost is incremented of 1.
     */
    public void setUsed() {
        wasUsed = true;
    }

    /**
     *
     * @return true if the character was used
     */
    public boolean isUsed() {
        return wasUsed;
    }

    /**
     * Activates the effect of the character, all specific character requirements must be satisfied before calling this method.
     * @param player the Player who is playing the card
     */
    public abstract void useEffect(Player player);

    public int getCost() {
        if (wasUsed) {
            return cost + 1;
        }
        return cost;
    }

    /**
     *
     * @return a copy of the students on the card, null if there are no students on the card.
     */
    public Students getStudents() {
        if (this.students == null) {
            return null;
        }
        return new Students(students);
    }

    public void setStudents(Students students) {
        this.students = students;
    }

    public String getName() {
        return String.valueOf(name);
    }

    public String getDescription() {
        return String.valueOf(description);
    }

    /**
     *
     * @return true if the character requires an island for his effect, false if not
     */
    public boolean requiresIsland() {
        return islandRequirement != null;
    }

    /**
     *
     * @return true if the character requires a faction for his effect, false if not
     */
    public boolean requiresFaction() {
        return factionRequirement != null;
    }

    public IslandRequirement getIslandRequirement() {
        return islandRequirement;
    }

    public FactionRequirement getFactionRequirement() {
        return factionRequirement;
    }
}
