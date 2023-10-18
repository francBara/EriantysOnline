package it.polimi.ingsw.model.Cards.Characters.CharacterRequirements;

import it.polimi.ingsw.model.Faction;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;

public class FactionRequirementTest extends TestCase {

    public void testSetFaction() {
        FactionRequirement factionRequirement = new FactionRequirement();

        HashSet<Faction> allFactions = new HashSet<Faction>(Arrays.asList(Faction.values()));

        assertTrue(allFactions.containsAll(factionRequirement.getFromFactions()));

        assertNull(factionRequirement.getFaction());

        factionRequirement.setFaction(Faction.RedDragons);

        assertEquals(Faction.RedDragons, factionRequirement.getFaction());
    }

    public void testGetFromFactions() {
        HashSet<Faction> factions = new HashSet<Faction>();

        factions.add(Faction.RedDragons);
        factions.add(Faction.PinkFairies);

        FactionRequirement factionRequirement = new FactionRequirement(new HashSet<Faction>(factions));

        assertEquals(2, factionRequirement.getFromFactions().size());

        assertTrue(factionRequirement.getFromFactions().contains(Faction.RedDragons));
        assertTrue(factionRequirement.getFromFactions().contains(Faction.PinkFairies));
    }
}