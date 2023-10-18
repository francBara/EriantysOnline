package it.polimi.ingsw.model.Messages.ActionPhaseMessages;

import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.BadActionPhaseMessage;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.CharacterMessage;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.StudentsMovementMessage;
import junit.framework.TestCase;

public class StudentsMovementMessageTest extends TestCase {
    public void testStudentsMovementMessage() throws Exception {
        try {
            new StudentsMovementMessage("sldfk");
            fail();
        } catch(BadActionPhaseMessage ignored) {}

        StudentsMovementMessage studentsMovementMessage = new StudentsMovementMessage("GreenFrogs");

        assertTrue(studentsMovementMessage.isMoveStudentsToHall());
        assertFalse(studentsMovementMessage.isMoveStudentsToIsland());
        assertFalse(studentsMovementMessage.isUseCharacter());

        assertEquals(Faction.GreenFrogs, studentsMovementMessage.getFaction());

        assertEquals("GreenFrogs", studentsMovementMessage.toString());

        studentsMovementMessage = new StudentsMovementMessage("BlueUnicorns 4");

        assertFalse(studentsMovementMessage.isMoveStudentsToHall());
        assertTrue(studentsMovementMessage.isMoveStudentsToIsland());
        assertFalse(studentsMovementMessage.isUseCharacter());

        assertEquals(Faction.BlueUnicorns, studentsMovementMessage.getFaction());
        assertEquals(4, studentsMovementMessage.getIslandIndex());

        assertEquals("BlueUnicorns 4", studentsMovementMessage.toString());

        studentsMovementMessage = new StudentsMovementMessage("character 5 RedDragons");

        assertFalse(studentsMovementMessage.isMoveStudentsToHall());
        assertFalse(studentsMovementMessage.isMoveStudentsToIsland());
        assertTrue(studentsMovementMessage.isUseCharacter());

        assertNotNull(studentsMovementMessage.getCharacterMessage());

        assertEquals("character 5 RedDragons", studentsMovementMessage.toString());
    }
}