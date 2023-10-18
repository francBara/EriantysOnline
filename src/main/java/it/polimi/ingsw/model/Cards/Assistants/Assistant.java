package it.polimi.ingsw.model.Cards.Assistants;

/**
 * The assistant card, which determines the movement of mother nature and the turns order.
 * @author Francesco Barabino
 */
public class Assistant {
    final public int turnValue;
    final public int motherNatureMovement;
    final public String name;

    /**
     * Constructs the Assistant with the value and the movement of mother nature that it can provide.
     * @param turnValue
     * @param motherNatureMovement
     * @throws RuntimeException if turnValue is less than zero or if motherNatureMovement is less than zero
     */
    public Assistant(int turnValue, int motherNatureMovement, String name) throws RuntimeException {
        if (turnValue < 0 || motherNatureMovement < 0 || name == null || name.isEmpty()) {
            throw(new RuntimeException());
        }
        this.turnValue = turnValue;
        this.motherNatureMovement = motherNatureMovement;
        this.name = name;
    }
}
