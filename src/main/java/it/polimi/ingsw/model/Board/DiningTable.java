package it.polimi.ingsw.model.Board;

import it.polimi.ingsw.model.Board.Exceptions.FullDiningTableException;
import it.polimi.ingsw.model.Faction;

/**
 * The dining table where a certain Faction of students seats.
 * @author Francesco Barabino
 */
public class DiningTable {
  private int studentsPlaced;
  private final Faction colorTable;
  private boolean hasProfessor; 
  private final boolean[] coinsGot = new boolean[3];
  protected int coins = 0;

  /**
   *
   * @param colorTable the Faction of this Dining Table, it cannot be changed afterwards
   */
  public DiningTable(Faction colorTable) {
    this.colorTable = colorTable;
  }

  /**
   * Adds one student to the DiningTable.
   * @throws FullDiningTableException if the DiningTable is full and can't accept more students
   */
  public void addStudent() {
    if (studentsPlaced >= 10) {
      throw(new FullDiningTableException());
    }
    studentsPlaced++;
    if (studentsPlaced == 3 && !coinsGot[0]) {
      coinsGot[0] = true;
      coins++;
    }
    else if (studentsPlaced == 6 && !coinsGot[1]) {
      coinsGot[1] = true;
      coins++;
    }
    else if (studentsPlaced == 9 && !coinsGot[2]) {
      coinsGot[2] = true;
      coins++;
    }
  }

  /**
   * Attempts the removal of a certain number of students, if there are fewer students than the ones to remove the DiningTable gets emptied.
   * @param num of students to remove
   * @return the actual number of students removed
   */
  public int removeStudents(int num) {
    if (studentsPlaced - num < 0) {
      int removedStudents = studentsPlaced;
      studentsPlaced = 0;
      return removedStudents;
    }
    else {
      studentsPlaced -= num;
      return num;
    }
  }

  /**
   *
   * @return the number of students on the DiningTable
   */
  public int getStudents() {
    return studentsPlaced;
  }

  /**
   * @param value true to place the professor, false to remove him
   */
  public void updateProfessor(boolean value){
    hasProfessor = value;
  }

  /**
   * @return the faction of the DiningTable
   */
  public Faction getFaction() {
    return colorTable;
  }

  /**
   *
   * @return true if there is a professor in the DiningTable, false if not
   */
  public boolean hasProfessor() {
    return hasProfessor;
  }

  /**
   *
   * @return true if the DiningTable is full and can't accept more students
   */
  public boolean isFull() {
    return studentsPlaced == 10;
  }

  /**
   * Takes all coins on the dining table and returns them.
   * @return the coins
   */
  public int takeAllCoins() {
    int tmpCoins = coins;
    coins = 0;
    return tmpCoins;
  }
}
