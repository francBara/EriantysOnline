package it.polimi.ingsw.model;
import it.polimi.ingsw.model.Exceptions.EmptyBagException;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Bag {
    private final Students studentsInTheBag = new Students();

    /**
     * Removes a random student from the bag, throwing an exception if the bag is empty.
     * @return the removed faction
     * @throws EmptyBagException if the bag is empty
     */
    public Faction takeStudent() throws EmptyBagException {
        Set<Faction> availableFactions = new HashSet<Faction>();
        for (Faction faction : Faction.values()) {
            if (studentsInTheBag.get(faction) > 0) {
                availableFactions.add(faction);
            }
        }

        if (availableFactions.isEmpty()) {
            throw(new EmptyBagException());
        }

        int randomIndex = new Random().nextInt(availableFactions.size());
        int i = 0;
        for (Faction faction : availableFactions) {
            if (i == randomIndex) {
                studentsInTheBag.remove(faction);
                return faction;
            }
            i++;
        }
        throw(new EmptyBagException());
    }

    /**
     * Adds a certain number of students to the bag.
     * @param faction of the students to add to the bag
     * @param num of students to add to the bag
     */
    public void addBackStudents(Faction faction, int num) {
        studentsInTheBag.add(faction, num);
    }

    /**
     * @return a copy of the Students in the bag.
     */
    public Students getStudents(){
        return new Students(studentsInTheBag);
    }
}
