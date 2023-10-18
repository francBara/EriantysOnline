package it.polimi.ingsw.model.Messages.ActionPhaseMessages;

import it.polimi.ingsw.model.Faction;

public class StudentsMovementMessage extends ActionPhaseMessage {
    Faction faction;
    Integer islandIndex;

    public StudentsMovementMessage(String message) throws BadActionPhaseMessage {
        String[] actionSplit = message.split(" ");
        if (actionSplit[0].equals("character")) {
            characterMessage = new CharacterMessage(message);
        }
        else {
            try {
                faction = Faction.valueOf(actionSplit[0]);
                if (actionSplit.length == 2) {
                    islandIndex = Integer.parseInt(actionSplit[1]);
                }
            } catch(IllegalArgumentException e) {
                throw(new BadActionPhaseMessage());
            }
        }
    }

    /**
     * Stringifies the object, making the actual message.
     *
     * The message can be in different forms, depending on the action type.
     *
     * if the action is moving a student from the entrance to the hall:
     * [Faction]
     *
     * if the action is moving a student from the entrance to an island:
     * [Faction] [Island]
     *
     * if the action is using the effect of a character:
     * refer to CharacterMessage documentation.
     *
     * @return the message.
     */
    @Override
    public String toString() {
        if (isMoveStudentsToHall()) {
            return faction.toString();
        }
        else if (isMoveStudentsToIsland()) {
            return faction + " " + islandIndex;
        }
        else if (isUseCharacter()) {
            return characterMessage.toString();
        }
        throw(new RuntimeException());
    }

    public Faction getFaction() {
        return faction;
    }

    public int getIslandIndex() {
        return islandIndex;
    }

    public boolean isMoveStudentsToHall() {
        return faction != null && islandIndex == null && characterMessage == null;
    }
    public boolean isMoveStudentsToIsland() {
        return faction != null && islandIndex != null && characterMessage == null;
    }
    public boolean isUseCharacter() {
        return faction == null && islandIndex == null && characterMessage != null;
    }
}
