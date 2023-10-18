package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Exceptions.EmptyStudentsException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Random;

public class StudentsTest extends TestCase {

    public void testAdd() {
        Students students = new Students();

        for (Faction faction : Faction.values()) {
            int studentsNumber = new Random().nextInt(120);
            students.add(faction, studentsNumber);
            assertEquals(studentsNumber, students.get(faction));
        }
    }

    public void testAddStudents() {
        Students students1 = new Students();
        Students students2 = new Students();
        Students students3 = new Students();

        for (Faction faction : Faction.values()) {
            students1.add(faction, new Random().nextInt(120));

            int studentsNumber = new Random().nextInt(120);

            students2.add(faction, studentsNumber);
            students3.add(faction, studentsNumber);
        }

        students3.add(students1);

        for (Faction faction : Faction.values()) {
            students3.get(faction);
            assertEquals(students1.get(faction) + students2.get(faction), students3.get(faction));
        }
    }

    public void testRemove() {
        Students students = new Students();

        for (Faction faction : Faction.values()) {
            int studentsNumber = new Random().nextInt(120);

            students.remove(faction, 0);

            assertEquals(0, students.get(faction));

            students.add(faction, studentsNumber);

            int studentsToSubtract = studentsNumber - 5;

            students.remove(faction, studentsToSubtract);

            assertEquals(studentsNumber - studentsToSubtract, students.get(faction));

            students.remove(faction, 5);

            assertEquals(0, students.get(faction));

            try {
                students.remove(faction);
                fail();
            } catch(EmptyStudentsException e) {

            }
        }
    }

    public void testGet() {
        Students students = new Students();

        for (Faction faction : Faction.values()) {
            int studentsNumber = new Random().nextInt(120);
            students.add(faction, studentsNumber);
            assertEquals(studentsNumber, students.get(faction));
        }
    }

    public void testGetTotalStudents() {
        Students students = new Students();
        int totalStudents = 0;

        for (Faction faction : Faction.values()) {
            int studentsNumber = new Random().nextInt(120);
            totalStudents += studentsNumber;
            students.add(faction, studentsNumber);
        }

        assertEquals(totalStudents, students.getTotalStudents());
    }

    public void testIsEmpty() {
        Students students = new Students();
        assertTrue(students.isEmpty());
    }

    public void testEquals() {
        Students students1 = new Students();
        Students students2 = new Students();

        assertEquals(students1, students2);

        for (Faction faction : Faction.values()) {
            students1.add(faction, 10);
            students2.add(faction, 10);
        }

        assertEquals(students1, students2);

        students1.add(Faction.RedDragons);

        assertFalse(students1.equals(students2));

        assertFalse(students1.equals("Gianluca"));
        assertEquals(students1, students1);
    }

    public void testToArray() {
        Students students = new Students();

        assertTrue(students.toArray().isEmpty());

        students.add(Faction.GreenFrogs, 3);
        students.add(Faction.BlueUnicorns);

        ArrayList<Faction> studentsArray = students.toArray();

        assertEquals(4, studentsArray.size());

        assertEquals(3, studentsArray.stream().filter((student) -> student == Faction.GreenFrogs).count());
        assertEquals(1, studentsArray.stream().filter((student) -> student == Faction.BlueUnicorns).count());
    }
}