package it.polimi.ingsw.model;

import java.util.Random;

/**
 * An enum representing the 5 factions in the game.
 */
public enum Faction {
    YellowGnomes,
    BlueUnicorns,
    RedDragons,
    PinkFairies,
    GreenFrogs;

    /**
     *
     * @return a random Faction from the enum
     */
    public static Faction getRandom() {
        int i = new Random().nextInt(Faction.values().length);
        int j = 0;
        for (Faction faction : Faction.values()) {
            if (i == j) {
                return faction;
            }
            j++;
        }
        throw(new RuntimeException());
    }
}
