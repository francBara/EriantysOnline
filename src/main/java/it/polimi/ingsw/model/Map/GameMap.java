package it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Map.exceptions.DifferentTowersException;
import it.polimi.ingsw.model.Map.exceptions.IslandsNotCloseException;
import it.polimi.ingsw.model.Map.exceptions.TowerNotPresentException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The map of the game containing islands and clouds.
 * @author Francesco Barabino
 */
public class GameMap {
    private final ArrayList<Island> islands = new ArrayList<Island>();
    private int motherNaturePosition;
    private final ArrayList<Cloud> clouds = new ArrayList<Cloud>();

    /**
     * Builds the game map, placing Mother Nature on an island and placing the clouds.
     * @param initialMotherNaturePosition the index of the island in which Mother Nature should be initially placed.
     * @param numberOfPlayers the number of players in the game, the number of clouds placed will be equal to that number.
     */
    public GameMap(int initialMotherNaturePosition, int numberOfPlayers) {
        motherNaturePosition = initialMotherNaturePosition;
        for (int i = 0; i < 12; i++) {
            islands.add(new Island(i));
        }
        islands.get(motherNaturePosition).placeMotherNature();
        for (int i = 0; i < numberOfPlayers; i++) {
            clouds.add(new Cloud());
        }
    }

    /**
     * Merges two or three islands, by removing them from the map and replacing them with one new Island that contains them.
     * Two islands are merged if Mother Nature is placed on one of them, they are adjacent, and they have towers of the same color placed on.
     */
    public void mergeIslands() {
        int nextIslandIndex;
        int previousIslandIndex;

        if (motherNaturePosition == islands.size() - 1) {
            nextIslandIndex = 0;
        }
        else {
            nextIslandIndex = motherNaturePosition + 1;
        }

        if (islands.get(motherNaturePosition).getTowersNumber() > 0 && islands.get(nextIslandIndex).getTowersNumber() > 0 && islands.get(motherNaturePosition).getTowersColor() == islands.get(nextIslandIndex).getTowersColor()) {
            if (nextIslandIndex < motherNaturePosition) {
                int tmp = nextIslandIndex;
                nextIslandIndex = motherNaturePosition;
                motherNaturePosition = tmp;
            }
            Island groupIsland = new Island(islands.get(motherNaturePosition), islands.get(nextIslandIndex));
            groupIsland.placeMotherNature();
            islands.remove(nextIslandIndex);
            islands.remove(motherNaturePosition);
            islands.add(motherNaturePosition,groupIsland);
        }

        if (motherNaturePosition == 0) {
            previousIslandIndex = islands.size() - 1;
        }
        else {
            previousIslandIndex = motherNaturePosition - 1;
        }

        if (islands.get(motherNaturePosition).getTowersNumber() > 0 && islands.get(previousIslandIndex).getTowersNumber() > 0 && islands.get(motherNaturePosition).getTowersColor() == islands.get(previousIslandIndex).getTowersColor()) {
            if (previousIslandIndex > motherNaturePosition) {
                int tmp = previousIslandIndex;
                previousIslandIndex = motherNaturePosition;
                motherNaturePosition = tmp;
            }
            Island groupIsland = new Island(islands.get(motherNaturePosition), islands.get(previousIslandIndex));
            groupIsland.placeMotherNature();
            islands.remove(motherNaturePosition);
            islands.remove(previousIslandIndex);
            islands.add(previousIslandIndex, groupIsland);
            motherNaturePosition = previousIslandIndex;
        }
    }

    /**
     * Moves mother nature through the islands of the given number of steps, one step for each island in the map.
     * Mother moved is moved clockwise, circling around the islands in the map. The steps can't be negative, mother nature can only move forward.
     * @param steps to move mother nature.
     * @throws RuntimeException if steps is negative.
     */
    public void moveMotherNature(int steps) throws RuntimeException {
        if (steps < 0) {
            throw(new RuntimeException());
        }

        islands.get(motherNaturePosition).removeMotherNature();

        int targetIndex = motherNaturePosition + steps;

        if (targetIndex >= islands.size()) {
            targetIndex = targetIndex - islands.size();
        }

        islands.get(targetIndex).placeMotherNature();
        motherNaturePosition = targetIndex;
    }

    /**
     *
     * @return the set of clouds on the map
     */
    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    /**
     *
     * @return the islands on the map, the given array represents the islands as a circle.
     */
    public ArrayList<Island> getIslands() {
        return new ArrayList<Island>(islands);
    }

    /**
     *
     * @return the current position of mother nature, as the index of the island on which is placed
     */
    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public Island getMotherNatureIsland() {
        return islands.get(motherNaturePosition);
    }

    /**
     * @param islandIndex the index of the island to reach
     * @return the number of steps that mother nature should perform to reach the given island
     */
    public int getIslandNecessarySteps(int islandIndex) {
        int steps = 0;
        if (islandIndex > motherNaturePosition) {
            steps = islandIndex - motherNaturePosition;
        }
        else if (islandIndex < motherNaturePosition) {
            steps = islandIndex + (islands.size() - motherNaturePosition);
        }
        return steps;
    }
}
