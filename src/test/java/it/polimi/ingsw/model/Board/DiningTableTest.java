package it.polimi.ingsw.model.Board;

import it.polimi.ingsw.model.Board.DiningTable;
import it.polimi.ingsw.model.Board.Exceptions.FullDiningTableException;
import it.polimi.ingsw.model.Faction;
import junit.framework.TestCase;

public class DiningTableTest extends TestCase {
    /**
     * - A dining table needs to be initialized empty.
     * - Students need to be correctly added to the DiningTable.
     * - A coin needs to be added to the DiningTable when the 3rd, 6th and 9th students are added.
     * - The DiningTable throws a FullDiningTableException when more than 10 students are added.
     */
    public void testAddStudent() {
        DiningTable diningTable = new DiningTable(Faction.getRandom());

        assertEquals(0, diningTable.getStudents());
        assertEquals(0, diningTable.coins);
        for (int i = 0; i < 3; i++) {
            assertEquals(i, diningTable.getStudents());
            diningTable.addStudent();
        }
        assertEquals(1, diningTable.coins);
        for (int i = 3; i < 6; i++) {
            assertEquals(i, diningTable.getStudents());
            diningTable.addStudent();
        }
        assertEquals(2, diningTable.coins);
        for (int i = 6; i < 9; i++) {
            assertEquals(i, diningTable.getStudents());
            diningTable.addStudent();
        }
        assertEquals(3, diningTable.coins);
        assertEquals(3, diningTable.takeAllCoins());
        assertEquals(0, diningTable.takeAllCoins());

        assertEquals(9, diningTable.getStudents());
        diningTable.addStudent();

        try {
            diningTable.addStudent();
            fail();
        } catch(FullDiningTableException ignored) {}
    }

    /**
     * The DiningTable must correctly remove the specified students.
     */
    public void testRemoveStudents() {
        DiningTable diningTable = new DiningTable(Faction.getRandom());

        for (int i = 0; i < 10; i++) {
            diningTable.addStudent();
        }

        diningTable.removeStudents(0);

        assertEquals(10, diningTable.getStudents());

        for (int i = 2; i > 0; i--) {
            assertEquals(i * 5, diningTable.getStudents());
            diningTable.removeStudents(5);
        }

        assertEquals(0, diningTable.getStudents());

        diningTable.removeStudents(1230);

        assertEquals(0, diningTable.getStudents());
    }
}