package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import junit.framework.TestCase;

public class AssistantTest extends TestCase {
    public void testGetMotherNatureMovement() {
        Assistant assistant = new Assistant(5, 5, "a");
        assertEquals(5, assistant.motherNatureMovement);

        try {
            new Assistant(-1, 0, "a");
            fail();
        } catch(RuntimeException e) {

        }
    }

    public void testGetPlayerTurnValue() {
        Assistant assistant = new Assistant(5, 5, "Gianluca");
        assertEquals(5, assistant.turnValue);
        assertEquals("Gianluca", assistant.name);
    }
}