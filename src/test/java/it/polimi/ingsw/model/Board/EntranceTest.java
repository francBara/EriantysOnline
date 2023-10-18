package it.polimi.ingsw.model.Board;

import it.polimi.ingsw.model.Board.Entrance;
import it.polimi.ingsw.model.Board.Exceptions.FullEntranceException;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Students;
import junit.framework.TestCase;

public class EntranceTest extends TestCase {

    /**
     * - The Entrance needs to correctly accept new students.
     * - The Entrance throws a FullEntranceException when more than 9 students are added.
     */
    public void testAddStudents() {
        Entrance entrance = new Entrance();

        Students students = new Students();

        students.add(Faction.PinkFairies, 4);

        entrance.addStudents(students);

        assertEquals(students, entrance.getStudents());

        entrance.addStudents(students);

        assertEquals(8, entrance.getStudents().getTotalStudents());

        try {
            entrance.addStudents(students);
            fail();
        } catch (FullEntranceException ignored) {}
    }

    /**
     * Students need to be correctly removed from the Entrance.
     */
    public void testRemoveStudent() {
        Entrance entrance = new Entrance();
        Students students = new Students();

        for (int i = 0; i < 9; i++) {
            students.add(Faction.PinkFairies, 1);
        }

        entrance.addStudents(students);

        for (int i = 0; i < 9; i++) {
            entrance.remove(Faction.PinkFairies);
            students.remove(Faction.PinkFairies);
            assertEquals(students.get(Faction.PinkFairies), entrance.getStudents().get(Faction.PinkFairies));
        }
    }

    /**
     * The Entrance must return a safe Students instance with the getStudents() method.
     */
    public void testGetStudents() {
        Entrance entrance = new Entrance();
        Students students = new Students();

        for (Faction faction : Faction.values()) {
            students.add(faction);
        }

        entrance.addStudents(students);

        assertEquals(students, entrance.getStudents());

        entrance.getStudents().remove(Faction.RedDragons);

        assertEquals(students, entrance.getStudents());
    }
}