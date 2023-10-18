package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Exceptions.EmptyStudentsException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;

/**
 * ADT representing a group of students.
 */
public class Students {
    private final EnumMap<Faction, Integer> students = new EnumMap<Faction, Integer>(Faction.class);
    
    public Students() {
        for (Faction faction : Faction.values()) {
            students.put(faction, 0);
        }
    }

    /**
     * Builds the object starting from another Students instance, copying all data.
     * @param students to build from
     */
    public Students(Students students) {
        for (Faction faction : Faction.values()) {
            this.students.put(faction, students.get(faction));
        }
    }

    /**
     * Adds a certain number of students of the given faction to the collection.
     * @param faction that you want to add
     * @param num of students that you want to add
     */
    public void add(Faction faction, int num) {
        students.put(faction, students.get(faction) + num);
    }

    /**
     * Adds one student of the given faction to the collection.
     * @param faction that you want to add
     */
    public void add(Faction faction) {
        students.put(faction, students.get(faction) + 1);
    }

    /**
     * Adds another Students instance to the collection.
     * @param students that you want to add
     */
    public void add(Students students) {
        for (Faction faction : Faction.values()) {
            this.students.put(faction, this.students.get(faction) + students.get(faction));
        }
    }

    /**
     * Removes a certain number of the given faction from the collection, throws an exception if more than available are removed.
     * @param faction
     * @param num of students that you want to remove
     * @throws EmptyStudentsException if the given num is greater than already present students
     */
    public void remove(Faction faction, int num) throws EmptyStudentsException {
        if (students.get(faction) - num < 0) {
            throw(new EmptyStudentsException());
        }
        students.put(faction, students.get(faction) - num);
    }

    /**
     * Removes a single student of the given faction from the collection, throws an exception if more than available are removed.
     * @param faction
     * @throws EmptyStudentsException if there are no students of the given faction in the collection
     */
    public void remove(Faction faction) throws EmptyStudentsException {
        if (students.get(faction) - 1 < 0) {
            throw(new EmptyStudentsException());
        }
        students.put(faction, students.get(faction) - 1);
    }

    /**
     * Returns the number of students of a given faction
     * @param faction to retrieve
     * @return the number of students of the given faction
     */
    public int get(Faction faction) {
        return students.get(faction);
    }

    /**
     * @return the total number of students in the collection.
     */
    public int getTotalStudents() {
        int totalStudents = 0;
        for (Integer students : students.values()) {
            totalStudents += students;
        }
        return totalStudents;
    }

    /**
     * @return the factions of the students in this ADT
     */
    public HashSet<Faction> getFactions() {
        HashSet<Faction> factions = new HashSet<Faction>();
        for (Faction faction : students.keySet()) {
            if (students.get(faction) > 0) {
                factions.add(faction);
            }
        }
        return factions;
    }

    /**
     * Checks if the collection has zero students.
     * @return true if the collection has zero students, false if not
     */
    public boolean isEmpty() {
        return getTotalStudents() == 0;
    }

    /**
     * @return An array where every element is a single student contained in this Students instance, represented by his faction
     */
    public ArrayList<Faction> toArray() {
        final ArrayList<Faction> studentsArray = new ArrayList<Faction>();
        for (Faction faction : Faction.values()) {
            for (int i = 0; i < students.get(faction); i++) {
                studentsArray.add(faction);
            }
        }
        return studentsArray;
    }

    @Override
    public boolean equals(Object students) {
        if (students == this) {
            return true;
        }

        if (!(students instanceof Students)) {
            return false;
        }

        Students studentsToCompare = (Students) students;

        for (Faction faction : Faction.values()) {
            if (get(faction) != studentsToCompare.get(faction)) {
                return false;
            }
        }
        return true;
    }
}
