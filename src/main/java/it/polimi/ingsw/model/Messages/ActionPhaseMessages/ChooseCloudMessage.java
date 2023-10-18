package it.polimi.ingsw.model.Messages.ActionPhaseMessages;

public class ChooseCloudMessage extends ActionPhaseMessage {
    private Integer cloudIndex;

    public ChooseCloudMessage(String message) throws BadActionPhaseMessage {
        String[] actions = message.split(" ");

        if (actions[0].equals("character")) {
            this.characterMessage = new CharacterMessage(message);
        }
        else {
            try {
                cloudIndex = Integer.parseInt(actions[0]);
            } catch(NumberFormatException e) {
                throw(new BadActionPhaseMessage());
            }
        }
    }

    public int getCloudIndex() {
        return cloudIndex;
    }

    public boolean isUseCharacter() {
        return characterMessage != null;
    }

    /**
     * Stringifies the object, making the actual message.
     *
     * The message can be in different forms, depending on the action type.
     *
     * if the action is moving Mother Nature of a certain number of steps:
     * [MotherNatureSteps]
     *
     * if the action is using the effect of a character:
     * refer to CharacterMessage documentation.
     *
     * @return the message.
     */
    public String toString() {
        if (isUseCharacter()) {
            return characterMessage.toString();
        }
        return cloudIndex.toString();
    }
}
