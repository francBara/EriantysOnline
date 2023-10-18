package it.polimi.ingsw.model.Board;

import it.polimi.ingsw.model.Board.Exceptions.FullEntranceException;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Students;
import it.polimi.ingsw.model.Exceptions.EmptyStudentsException;

/**
 * The entrance of the school board, from where students can be moved to a DiningTable or to an Island.
 * @author Francesco Barabino
 */
public class Entrance {
    private final Students studentsPlaced;
    private final int maxStudents = 9;

    public Entrance() {
        studentsPlaced = new Students();
    }

    /**
     * Adds students to the entrance by accepting a Students instance.
     * @param students to add to the entrance
     * @throws FullEntranceException if the maximum number of students is exceeded, if so the students aren't added
     */
    public void addStudents(Students students) throws FullEntranceException {
        if (studentsPlaced.getTotalStudents() + students.getTotalStudents() > maxStudents) {
          throw(new FullEntranceException());
        }
        studentsPlaced.add(students);
    }

    /**
     * Removes one student of a certain faction.
     * @param faction faction of the students to remove.
     * @throws EmptyStudentsException
     */
    public void remove(Faction faction) throws EmptyStudentsException {
       studentsPlaced.remove(faction);
    }

    /**
     * Removes a given number of students of a certain faction.
     * @param faction faction of the students to remove.
     * @param num number of students to remove.
     * @throws EmptyStudentsException
     */
    public void remove(Faction faction, int num) throws EmptyStudentsException {
       studentsPlaced.remove(faction, num);
    }

    /**
     *
     * @param faction the faction to check.
     * @return true if there is at least one student of the given faction in the entrance, false otherwise.
     */
    public boolean canTakeStudent(Faction faction) {
       return studentsPlaced.get(faction) > 0;
    }

    /**
     * @return a copy of the students placed in the Entrance
     */
   public Students getStudents() {
      return new Students(studentsPlaced);
   }
}
