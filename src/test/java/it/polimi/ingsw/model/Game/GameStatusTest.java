package it.polimi.ingsw.model.Game;

import junit.framework.TestCase;

public class GameStatusTest extends TestCase {
    /**
     * The GameStatus method isActionPhase() must return true if corresponds to a part of the action phase:
     * students movement, mother nature movement and cloud choice.
     */
    public void testIsActionPhase() {
        for (GameStatus status : GameStatus.values()) {
            if (status == GameStatus.MoveStudent || status == GameStatus.MoveMotherNature || status == GameStatus.ChooseCloud) {
                assertTrue(status.isActionPhase());
            }
            else {
                assertFalse(status.isActionPhase());
            }
        }
    }
}