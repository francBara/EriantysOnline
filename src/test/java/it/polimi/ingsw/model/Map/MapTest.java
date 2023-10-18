package it.polimi.ingsw.model.Map;

import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Students;
import junit.framework.TestCase;

public class MapTest extends TestCase {
    public void testMergeIslands() {
        GameMap map = new GameMap(0, 2);

        map.mergeIslands();

        assertEquals(12, map.getIslands().size());

        for (Island island : map.getIslands()) {
            assertEquals(0, island.getTowersNumber());
        }

        map.getIslands().get(11).placeTower(TowerColor.Grey);
        map.getIslands().get(11).placeStudent(Faction.PinkFairies);
        map.getIslands().get(0).placeTower(TowerColor.Grey);
        map.getIslands().get(0).placeStudent(Faction.RedDragons);

        map.moveMotherNature(5);

        assertEquals(5, map.getMotherNaturePosition());

        map.mergeIslands();

        assertEquals(12, map.getIslands().size());

        assertEquals(5, map.getMotherNaturePosition());

        map.moveMotherNature(7);

        assertEquals(0, map.getMotherNaturePosition());

        map.mergeIslands();

        assertEquals(11, map.getIslands().size());
        assertEquals(0, map.getMotherNaturePosition());
        assertEquals(2, map.getIslands().get(0).getTowersNumber());

        Students students = new Students();
        students.add(Faction.PinkFairies);
        students.add(Faction.RedDragons);

        assertEquals(students, map.getIslands().get(0).getStudents());

        assertTrue(map.getIslands().get(0).getIsMotherNaturePlaced());

        for (int i = 1; i < 11; i++) {
            assertFalse(map.getIslands().get(i).getIsMotherNaturePlaced());
        }

        map.getIslands().get(1).placeTower(TowerColor.Grey);
        map.getIslands().get(10).placeTower(TowerColor.Grey);

        map.mergeIslands();

        assertEquals(9, map.getIslands().size());
        assertEquals(0, map.getMotherNaturePosition());
        assertEquals(4, map.getIslands().get(0).getTowersNumber());

        assertTrue(map.getIslands().get(0).getIsMotherNaturePlaced());

        for (int i = 1; i < 9; i++) {
            assertFalse(map.getIslands().get(i).getIsMotherNaturePlaced());
        }

        map.getIslands().get(5).placeTower(TowerColor.Black);
        map.getIslands().get(6).placeTower(TowerColor.Black);
        map.moveMotherNature(6);

        map.mergeIslands();

        assertEquals(8, map.getIslands().size());
        assertEquals(5, map.getMotherNaturePosition());
        assertEquals(2, map.getIslands().get(5).getTowersNumber());

        for (int i = 0; i < 8; i++) {
            if (i == 5) {
                assertTrue(map.getIslands().get(i).getIsMotherNaturePlaced());
            }
            else {
                assertFalse(map.getIslands().get(i).getIsMotherNaturePlaced());
            }
        }
    }

    /**
     * - There must be only one mother nature between all islands
     * - Mother nature must be correctly moved of the required number of steps on the islands map, which is represented by an array
     * - moveMotherNature() throws a RuntimeException if a negative number is given a negative number of steps
     */
    public void testMoveMotherNature() {
        GameMap map = new GameMap(11, 2);

        assertEquals(11, map.getMotherNaturePosition());
        assertTrue(map.getIslands().get(11).getIsMotherNaturePlaced());

        for (int i = 0; i < 12; i++) {
            map.moveMotherNature(i);

            int islandsWithMotherNature = 0;

            for (Island island : map.getIslands()) {
                if (island.getIsMotherNaturePlaced()) {
                    islandsWithMotherNature++;
                }
            }

            assertEquals(1, islandsWithMotherNature);
        }

        map = new GameMap(0, 2);

        map.moveMotherNature(11);

        assertEquals(11, map.getMotherNaturePosition());

        map.moveMotherNature(7);

        assertEquals(6, map.getMotherNaturePosition());

        map.moveMotherNature(0);

        assertEquals(6, map.getMotherNaturePosition());

        try {
            map.moveMotherNature(-1);
            fail();
        } catch(RuntimeException ignored) {}
    }

    public void testGetIslandNecessarySteps() {
        GameMap gameMap = new GameMap(2, 2);

        assertEquals(8, gameMap.getIslandNecessarySteps(10));

        gameMap.moveMotherNature(3);

        assertEquals(9, gameMap.getIslandNecessarySteps(2));

        gameMap.moveMotherNature(4);

        assertEquals(4, gameMap.getIslandNecessarySteps(1));
    }
}