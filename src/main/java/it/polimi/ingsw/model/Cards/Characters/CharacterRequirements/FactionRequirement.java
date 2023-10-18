package it.polimi.ingsw.model.Cards.Characters.CharacterRequirements;

import it.polimi.ingsw.model.Faction;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Useful when a character card requires a faction for its effect to work.
 * @author Francesco Barabino
 */
public class FactionRequirement extends CharacterRequirement {
    private Faction faction;
    private final HashSet<Faction> fromFactions;

    /**
     * A faction requirement where the user can choose between any faction in the game.
     */
    public FactionRequirement() {
        fromFactions = new HashSet<Faction>();
        fromFactions.addAll(Arrays.asList(Faction.values()));
    }

    /**
     * A faction requirement where the user can choose between a given set of factions.
     * @param fromFactions the factions from which the user can choose
     */
    public FactionRequirement(HashSet<Faction> fromFactions) {
        this.fromFactions = fromFactions;
    }

    /**
     * @param faction the faction that will be used by the character effect
     */
    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    /**
     *
     * @return the factions from which the user can choose
     */
    public HashSet<Faction> getFromFactions() {
        return fromFactions;
    }

    /**
     *
     * @return the chosen faction
     */
    public Faction getFaction() {
        return faction;
    }
}
