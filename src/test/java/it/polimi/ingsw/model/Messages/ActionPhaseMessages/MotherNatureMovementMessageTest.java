package it.polimi.ingsw.model.Messages.ActionPhaseMessages;

import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.BadActionPhaseMessage;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.CharacterMessage;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.MotherNatureMovementMessage;
import junit.framework.TestCase;

public class MotherNatureMovementMessageTest extends TestCase {
    public void testMotherNatureMovementMessage() throws Exception {
        try {
            new MotherNatureMovementMessage("skldf");
            fail();
        } catch(BadActionPhaseMessage ignored) {

        }

        MotherNatureMovementMessage motherNatureMovementMessage = new MotherNatureMovementMessage("4");

        assertEquals(4, motherNatureMovementMessage.getMotherNatureSteps());

        assertFalse(motherNatureMovementMessage.isUseCharacter());

        assertEquals("4", motherNatureMovementMessage.toString());

        motherNatureMovementMessage = new MotherNatureMovementMessage(new CharacterMessage("character 1").toString());

        assertTrue(motherNatureMovementMessage.isUseCharacter());
        assertNotNull(motherNatureMovementMessage.getCharacterMessage());

        assertEquals("character 1", motherNatureMovementMessage.toString());
    }
}