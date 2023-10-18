package it.polimi.ingsw.model.Messages.ActionPhaseMessages;

import it.polimi.ingsw.model.Faction;
import junit.framework.TestCase;

public class ChooseCloudMessageTest extends TestCase {
    public void testChooseCloudMessage() throws Exception {
        ChooseCloudMessage chooseCloudMessage = new ChooseCloudMessage("4");

        assertEquals(4, chooseCloudMessage.getCloudIndex());

        assertFalse(chooseCloudMessage.isUseCharacter());

        assertEquals("4", chooseCloudMessage.toString());

        chooseCloudMessage = new ChooseCloudMessage(new CharacterMessage("character 1").toString());

        assertTrue(chooseCloudMessage.isUseCharacter());
        assertNotNull(chooseCloudMessage.getCharacterMessage());

        assertEquals("character 1", chooseCloudMessage.toString());
    }

    public void testFromMessage() throws Exception {
        ChooseCloudMessage chooseCloudMessage = new ChooseCloudMessage("4");

        assertFalse(chooseCloudMessage.isUseCharacter());

        assertEquals(4, chooseCloudMessage.getCloudIndex());

        chooseCloudMessage = new ChooseCloudMessage("character 2 RedDragons");

        assertTrue(chooseCloudMessage.isUseCharacter());

        assertEquals(2, chooseCloudMessage.getCharacterMessage().characterIndex);
        assertEquals(Faction.RedDragons, chooseCloudMessage.getCharacterMessage().getFaction());
    }

}