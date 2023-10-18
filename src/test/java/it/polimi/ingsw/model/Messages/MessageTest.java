package it.polimi.ingsw.model.Messages;

import junit.framework.TestCase;

public class MessageTest extends TestCase {

    /**
     * isActionPhase() must respect the game specifics.
     */
    public void testIsActionPhase() {
        for (Message message : Message.values()) {
            if (message == Message.MoveStudent || message == Message.MoveMotherNature || message == Message.ChooseCloud) {
                assertTrue(message.isActionPhase());
            }
            else {
                assertFalse(message.isActionPhase());
            }
        }
    }
}