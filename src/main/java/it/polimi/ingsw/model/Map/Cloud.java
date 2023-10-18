package it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Students;

/**
 * A cloud to place on the map, from which players take students to put on the Entrance.
 */
public class Cloud {
    private Students studentsPlaced = new Students();

    /**
     * @return a copy of the students on the Cloud, without removing them.
     */
    public Students peekStudents() {
        return new Students(studentsPlaced);
    }

    /**
     * Removes all students from the Cloud and returns them.
     * @return the Cloud students.
     */
    public Students takeStudents() {
        Students students = studentsPlaced;
        studentsPlaced = new Students();
        return students;
    }

    /**
     *
     * @param students to place on the Cloud, they overwrite all students placed before.
     */
    public void setStudents(Students students) {
        studentsPlaced = students;
    }
}
