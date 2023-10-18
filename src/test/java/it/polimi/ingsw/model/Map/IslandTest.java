package it.polimi.ingsw.model.Map;

import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Player.Player;
import junit.framework.TestCase;

public class IslandTest extends TestCase {
    public void testPlaceStudent() {
        Island island = new Island(0);

        for (int i = 0; i < 50; i++) {
            assertEquals(i, island.getStudents().getTotalStudents());
            island.placeStudent(Faction.getRandom());
        }
    }

    /**
     * Influence must account:
     * - The number of students on an Island for a certain player
     * - The number of towers of a certain color on an Island
     * - Eventual characters effects
     */
    public void testGetInfluence() {
        Island island = new Island(1);

        SchoolBoard schoolBoard = new SchoolBoard(6, 6, TowerColor.Black);

        Player player = new Player("");
        player.setSchoolBoard(schoolBoard);

        assertEquals(0, island.getInfluence(player));

        island.placeStudent(Faction.PinkFairies);
        island.placeStudent(Faction.PinkFairies);

        assertEquals(0, island.getInfluence(player));

        schoolBoard.getDiningTable(Faction.PinkFairies).updateProfessor(true);

        assertEquals(2, island.getInfluence(player));

        island.placeTower(TowerColor.White);

        assertEquals(2, island.getInfluence(player));

        island.placeTower(TowerColor.Black);

        assertEquals(3, island.getInfluence(player));

        Island island2 = new Island(0);

        island2.placeTower(TowerColor.Black);
        island2.placeStudent(Faction.PinkFairies);

        Island groupOfIslands = new Island(island, island2);

        assertEquals(5, groupOfIslands.getInfluence(player));

        for (int i = 0; i < 32; i++) {
            groupOfIslands.placeStudent(Faction.PinkFairies);
        }
        for (int i = 0; i < 32; i++) {
            groupOfIslands.placeStudent(Faction.RedDragons);
        }

        assertEquals(37, groupOfIslands.getInfluence(player));

        schoolBoard.getDiningTable(Faction.RedDragons).updateProfessor(true);

        assertEquals(69, groupOfIslands.getInfluence(player));
    }

    /**
     * placeTower() will:
     * - Increment the towersNumber from 0 to 1 if there are no towers on the Island
     * - Change the towerColor if a tower which is different from the existent tower is placed
     */
    public void testPlaceTower() {
        Island island = new Island(0);

        assertEquals(0, island.getTowersNumber());

        island.placeTower(TowerColor.Black);

        assertEquals(1, island.getTowersNumber());
        assertEquals(TowerColor.Black, island.getTowersColor());

        island.placeTower(TowerColor.White);

        assertEquals(1, island.getTowersNumber());
        assertEquals(TowerColor.White, island.getTowersColor());

        Island island2 = new Island(0);
        island2.placeTower(TowerColor.White);

        Island groupOfIslands = new Island(island, island2);

        groupOfIslands.placeTower(TowerColor.Black);

        assertEquals(2, groupOfIslands.getTowersNumber());
        assertEquals(TowerColor.Black, groupOfIslands.getTowersColor());
    }

    public void testGetIsMotherNaturePlaced() {
        Island island = new Island(0);

        assertFalse(island.getIsMotherNaturePlaced());

        island.removeMotherNature();

        assertFalse(island.getIsMotherNaturePlaced());

        island.placeMotherNature();

        assertTrue(island.getIsMotherNaturePlaced());

        island.removeMotherNature();

        assertFalse(island.getIsMotherNaturePlaced());
    }

    public void testEquals() {
        Island island1 = new Island(0);
        Island island2 = new Island(1);

        assertEquals(island1, island2);

        island1.placeTower(TowerColor.White);
        island2.placeTower(TowerColor.Black);

        assertFalse(island1.equals(island2));

        island1.placeTower(TowerColor.Black);

        assertEquals(island1, island2);

        island1.placeStudent(Faction.RedDragons);

        assertFalse(island1.equals(island2));

        island2.placeStudent(Faction.RedDragons);

        assertEquals(island1, island2);

        island1.placeMotherNature();

        assertFalse(island1.equals(island2));

        island2.placeMotherNature();

        assertEquals(island1, island2);
    }
}