package it.polimi.ingsw.model.Board;

import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Students;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The player's school board, containing towers, entrance and dining tables.
 * @author Francesco Barabino
 */
public class SchoolBoard {
	private int numberOfTowers;
	private final int maxTowers;
	private final TowerColor towerColor;
	private final Entrance entrance;
	private final EnumMap<Faction, DiningTable> diningTables;
	
	
	public SchoolBoard(int numberOfTowers, int maxTowers, TowerColor towerColor) {
		this.numberOfTowers = numberOfTowers;
		this.maxTowers = maxTowers;
		this.towerColor = towerColor;
		entrance = new Entrance();
		diningTables = new EnumMap<Faction, DiningTable>(Faction.class);
		for (Faction faction : Faction.values()) {
			diningTables.put(faction, new DiningTable(faction));
		}
	}

	/**
	 * Decrements the number of towers in the SchoolBoard
	 */
	public void removeTower() {
		if (numberOfTowers > 0) {
			numberOfTowers--;
		}
	}

	/**
	 * Increments the number of towers.
	 * @throws RuntimeException if the maximum number of towers is exceeded and the number of towers is not incremented
	 */
	public void addTowers(int towersNumber) throws RuntimeException {
		if (numberOfTowers + towersNumber > maxTowers) {
			throw(new RuntimeException());
		}
		numberOfTowers += towersNumber;
	}

	/**
	 *
	 * @return the number of towers
	 */
	public int getTowers() {
		return numberOfTowers;
	}

	/**
	 *
	 * @return a set of Faction containing all faction for which the SchoolBoard has professors
	 */
	public Set<Faction> getProfessors() {
		Set<Faction> professors = new HashSet<Faction>();
		for (DiningTable diningTable : diningTables.values()) {
			if (diningTable.hasProfessor()) {
				professors.add(diningTable.getFaction());
			}
		}
		return professors;
	}

	/**
	 *
	 * @return the color of the towers in the SchoolBoard
	 */
	public TowerColor getTowersColor() {
		return towerColor;
	}

	/**
	 *
	 * @param faction of the dining table to get
	 * @return the DiningTable with the color of the given faction
	 */
	public DiningTable getDiningTable(Faction faction) {
		return diningTables.get(faction);
	}

	/**
	 *
	 * @return the Entrance of the SchoolBoard
	 */
	public Entrance getEntrance() {
		return entrance;
	}
}


