package it.polimi.ingsw.model.Messages.ActionPhaseMessages;

import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.CharacterMessage;
import junit.framework.TestCase;

public class CharacterMessageTest extends TestCase {

    public void testCharacterMessage() {
        Faction faction = null;
        Integer islandIndex = null;

        CharacterMessage characterMessage = new CharacterMessage(4, faction, islandIndex);

        assertEquals(4, characterMessage.characterIndex);
        assertNull(characterMessage.getFaction());
        assertNull(characterMessage.getIslandIndex());

        assertEquals("character 4", characterMessage.toString());

        faction = Faction.RedDragons;

        characterMessage = new CharacterMessage(6, faction, islandIndex);

        assertEquals(6, characterMessage.characterIndex);
        assertEquals(Faction.RedDragons, characterMessage.getFaction());
        assertNull(characterMessage.getIslandIndex());

        assertEquals("character 6 RedDragons", characterMessage.toString());

        faction = null;
        islandIndex = 3;

        characterMessage = new CharacterMessage(7, faction, islandIndex);

        assertEquals(7, characterMessage.characterIndex);
        assertNull(characterMessage.getFaction());
        assertEquals(new Integer(3), characterMessage.getIslandIndex());

        assertEquals("character 7 3", characterMessage.toString());

        faction = Faction.PinkFairies;
        islandIndex = 0;

        characterMessage = new CharacterMessage(1, faction, islandIndex);

        assertEquals(1, characterMessage.characterIndex);
        assertEquals(Faction.PinkFairies, characterMessage.getFaction());
        assertEquals(new Integer(0), characterMessage.getIslandIndex());

        assertEquals("character 1 PinkFairies 0", characterMessage.toString());
    }

    public void testFromMessage() throws Exception {
        CharacterMessage characterMessage = new CharacterMessage("character 4");

        assertEquals(4, characterMessage.characterIndex);
        assertNull(characterMessage.getFaction());
        assertNull(characterMessage.getIslandIndex());

        characterMessage = new CharacterMessage("character 0 PinkFairies");

        assertEquals(0, characterMessage.characterIndex);
        assertEquals(Faction.PinkFairies, characterMessage.getFaction());
        assertNull(characterMessage.getIslandIndex());

        characterMessage = new CharacterMessage("character 5 2");

        assertEquals(5, characterMessage.characterIndex);
        assertNull(characterMessage.getFaction());
        assertEquals(new Integer(2), characterMessage.getIslandIndex());

        characterMessage = new CharacterMessage("character 0 BlueUnicorns 3");

        assertEquals(0, characterMessage.characterIndex);
        assertEquals(Faction.BlueUnicorns, characterMessage.getFaction());
        assertEquals(new Integer(3), characterMessage.getIslandIndex());
    }
}