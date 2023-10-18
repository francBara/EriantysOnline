package it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import it.polimi.ingsw.model.Player.exceptions.EmptyNicknameException;

import java.util.ArrayList;
import java.util.Set;

public class Player implements Comparable<Player> {
    private int coinsOwned = 1;
    private final ArrayList<Assistant> assistantsOwned = new ArrayList<Assistant>();
    private Assistant playedAssistant;
    private SchoolBoard schoolBoard;

    private int bonusInfluence = 0;
    private int bonusMotherNatureValue = 0;

    private boolean hasProfessorsBonus = false;
    private int assistantQueue = 0;

    public final String nickname;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sets the school board for the player
     * @param schoolBoard
     */
    public void setSchoolBoard(SchoolBoard schoolBoard) {
        this.schoolBoard = schoolBoard;
    }

    /**
     * Adds a set of assistants to the owned assistants of the Player, it doesn't clear the previously added assistants, so if called multiple times it will result in multiple adds.
     * @param assistants to add to the Player
     */
    public void chooseAssistantDeck(Set<Assistant> assistants) {
        assistantsOwned.addAll(assistants);
    }

    /**
     * Plays an assistant owned by the player, then removes it from the deck.
     * @param assistantIndex the index of the Assistant in the deck of the player
     */
    public void playAssistant(int assistantIndex) {
        playedAssistant = assistantsOwned.get(assistantIndex);
        assistantsOwned.remove(assistantIndex);
    }

    /**
     * Adds one coin to the coins owned by the player.
     */
    public void addCoins(int coins) {
        coinsOwned += coins;
    }

    /**
     * Removes a certain number of coins from the player.
     * @param coinsToRemove
     */
    public void removeCoins(int coinsToRemove) {
        if (coinsToRemove > coinsOwned) {
            coinsOwned = 0;
        }
        else {
            coinsOwned -= coinsToRemove;
        }
    }

    public int getCoins() {
        return coinsOwned;
    }

    /**
     *
     * @return a copy of owned assistants.
     */
    public ArrayList<Assistant> getAssistantsOwned() {
        return new ArrayList<Assistant>(assistantsOwned);
    }

    public int getBonusInfluence() {
        return bonusInfluence;
    }

    public void setBonusInfluence(int bonusInfluence) {
        this.bonusInfluence = bonusInfluence;
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public boolean getProfessorsBonus() {
        return hasProfessorsBonus;
    }

    public void setProfessorsBonus(boolean value) {
        hasProfessorsBonus = value;
    }

    public void addBonusMotherNatureSteps() {
        bonusMotherNatureValue += 2;
    }

    public void removeBonusMotherNatureSteps() {
        bonusMotherNatureValue = 0;
    }

    public void setAssistantQueue(int assistantQueue) {
        this.assistantQueue = assistantQueue;
    }

    public int getAssistantQueue() {
        return assistantQueue;
    }

    public Assistant getPlayedAssistant() {
        return playedAssistant;
    }

    public int getMotherNatureValue() {
        if (playedAssistant == null) {
            return bonusMotherNatureValue;
        }
        return playedAssistant.motherNatureMovement + bonusMotherNatureValue;
    }

    /**
     * Compares the turn value of this player to the turn value of another player, deciding who should play first.
     * @param player the other Player to compare to
     * @return -1 if this Player should play first, 1 if the other player should play first
     */
    @Override
    public int compareTo(Player player) {
        if (player.getPlayedAssistant() == null) {
            return -1;
        }
        else if (playedAssistant == null) {
            return 1;
        }
        else if (playedAssistant.turnValue < player.getPlayedAssistant().turnValue) {
            return -1;
        }
        else if (playedAssistant.turnValue == player.getPlayedAssistant().turnValue) {
            if (getAssistantQueue() < player.getAssistantQueue()) {
                return -1;
            }
            return 1;
        }
        return 1;
    }
}
