package it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;

/**
 * An island of the map, where players can put students, towers and move mother nature.
 * An island can be merged with another island, resulting in another island.
 */
public class Island {
    protected Students students = new Students();
    private boolean isMotherNaturePlaced = false;
    private boolean ignoreTowers = false;
    private Faction ignoredFaction;
    private TowerColor towerColor;
    private int towersNumber = 0;
    public final int id;

    /**
     * An island with a unique id.
     * @param id
     */
    public Island(int id) {
        this.id = id;
    }

    /**
     * An island built from two different islands, merging students, towers and inheriting the id of one of them.
     * @param island1
     * @param island2
     */
    public Island(Island island1, Island island2) {
        students.add(island1.students);
        students.add(island2.students);
        isMotherNaturePlaced = island1.getIsMotherNaturePlaced() || island2.getIsMotherNaturePlaced();
        ignoreTowers = island1.ignoreTowers || island2.ignoreTowers;
        towerColor = island1.towerColor;
        towersNumber = island1.getTowersNumber() + island2.getTowersNumber();
        this.id = island1.id;
    }

    /**
     * Places a single student of the given faction on the Island.
     * @param student to place
     */
    public void placeStudent(Faction student){
        students.add(student);
    }

    /**
     * Gets the total influence of the given player on the Island.
     * @param player to calculate the influence of
     * @return the influence on the Island of the given player
     */
    public int getInfluence(Player player){
        int totalInfluence = player.getBonusInfluence();

        for (Faction faction : player.getSchoolBoard().getProfessors()) {
            if (faction == ignoredFaction) {
                continue;
            }
            totalInfluence += students.get(faction);
        }

        if (!ignoreTowers && player.getSchoolBoard().getTowersColor() == getTowersColor()) {
            totalInfluence += getTowersNumber();
        }

        return totalInfluence;
    }

    /**
     * Places a tower of the given color on the Island.
     * @param tower to place
     */
    public void placeTower(TowerColor tower) {
        this.towerColor = tower;
        if (towersNumber == 0) {
            towersNumber++;
        }
    }

    /**
     * @return the color of towers on the Island
     */
    //New method, to add in UML
    public TowerColor getTowersColor() {
        return towerColor;
    }

    /**
     *
     * @return the number of towers on the Island
     */
    //New method, to add in UML
    public int getTowersNumber() {
        return towersNumber;
    }

    /**
     *
     * @return a copy of the students on the Island
     */
    public Students getStudents(){
        return new Students(students);
    }

    /**
     *
     * @return true if mother nature is on the island, false otherwise
     */
    public boolean getIsMotherNaturePlaced(){
        return isMotherNaturePlaced;
    }

    public void placeMotherNature(){
        isMotherNaturePlaced = true;
    }

    public void removeMotherNature(){
        isMotherNaturePlaced = false;
    }

    public void setIgnoreTowers(boolean value) {
        this.ignoreTowers = value;
    }

    public boolean getIgnoreTowers() {
        return ignoreTowers;
    }

    /**
     * Ignores a certain faction for influence calculation.
     * @param faction to ignore for influence calculation
     */
    public void setIgnoredFaction(Faction faction) {
        ignoredFaction = faction;
    }

    public Faction getIgnoredFaction() {
        return ignoredFaction;
    }

    public void removeIgnoredFaction() {
        ignoredFaction = null;
    }

    @Override
    public boolean equals(Object island) {
        if (island == null) {
            return false;
        }
        if (island == this) {
            return true;
        }

        if (!(island instanceof Island)) {
            return false;
        }

        Island islandToCompare = (Island) island;



        if (islandToCompare.students.equals(students) && islandToCompare.isMotherNaturePlaced == isMotherNaturePlaced && islandToCompare.towersNumber == towersNumber && islandToCompare.towerColor == towerColor) {
            return true;
        }

        return false;
    }
}
