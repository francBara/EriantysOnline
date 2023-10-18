package it.polimi.ingsw.model.Cards.Characters.CharacterRequirements;

import it.polimi.ingsw.model.Map.Island;
import junit.framework.TestCase;

public class IslandRequirementTest extends TestCase {

    public void testSetIsland() {
        IslandRequirement islandRequirement = new IslandRequirement();

        assertNull(islandRequirement.getIsland());

        islandRequirement.setIsland(new Island(0));

        assertNotNull(islandRequirement.getIsland());
    }
}