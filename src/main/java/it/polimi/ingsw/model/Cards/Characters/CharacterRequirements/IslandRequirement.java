package it.polimi.ingsw.model.Cards.Characters.CharacterRequirements;

import it.polimi.ingsw.model.Map.Island;

/**
 * Useful when a character card requires an island for its effect to work.
 * @author Francesco Barabino
 */
public class IslandRequirement extends CharacterRequirement {
    private Island island;

    /**
     *
     * @param island the island that will be used by the character effect
     */
    public void setIsland(Island island) {
        this.island = island;
    }

    /**
     *
     * @return the chosen island
     */
    public Island getIsland() {
        return island;
    }
}
