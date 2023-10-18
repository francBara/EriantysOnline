package it.polimi.ingsw.model.Map;

import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Map.Cloud;
import it.polimi.ingsw.model.Students;
import junit.framework.TestCase;

import java.util.Random;

public class CloudTest extends TestCase {

    /**
     * When Students are taken from the Cloud, the returned instance must be equal to the Students that
     * were previously set, and the Cloud needs to be emptied.
     */
    public void testTakeStudents() {
        Cloud cloud = new Cloud();

        Students students = new Students();

        for (Faction faction : Faction.values()) {
            students.add(faction, new Random().nextInt(120));
        }

        cloud.setStudents(students);

        Students takenStudents = cloud.takeStudents();

        assertEquals(students, takenStudents);
        assertTrue(cloud.takeStudents().isEmpty());
    }

    public void testPeekStudents() {
        Cloud cloud = new Cloud();

        Students students = new Students();

        for (Faction faction : Faction.values()) {
            students.add(faction, new Random().nextInt(120));
        }

        cloud.setStudents(students);

        assertEquals(students, cloud.peekStudents());
        assertEquals(students, cloud.peekStudents());
    }
}