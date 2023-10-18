package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Exceptions.EmptyBagException;
import junit.framework.TestCase;

public class BagTest extends TestCase {

    public void testTakeStudent() {
        Bag bag = new Bag();

        for (Faction faction : Faction.values()) {
            bag.addBackStudents(faction, 26);
        }

        for (int i = 0; i < 130; i++) {
            int totalStudents = bag.getStudents().getTotalStudents();
            Faction faction = bag.takeStudent();
            assertEquals(totalStudents - 1, bag.getStudents().getTotalStudents());
            assertNotNull(faction);
        }
        assertTrue(bag.getStudents().isEmpty());

        try {
            bag.takeStudent();
            fail();
        } catch(EmptyBagException e) {

        }
    }
}