package it.polimi.ingsw.model.Messages.ActionPhaseMessages;

public abstract class ActionPhaseMessage {
    protected CharacterMessage characterMessage;

    public CharacterMessage getCharacterMessage() {
        return characterMessage;
    }

    public abstract boolean isUseCharacter();
}
