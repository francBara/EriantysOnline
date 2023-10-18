package it.polimi.ingsw.model.Messages.ActionPhaseMessages;

import it.polimi.ingsw.model.Faction;

/**
 * It describes an object to make client-server communication regarding characters easier.
 * It contains the index of the chosen character, the faction if required by the Character and the island index if an island is required by the Character.
 * It has built in serializer and deserializer.
 */
public class CharacterMessage {
    public final int characterIndex;
    private Faction faction;
    private Integer islandIndex;

    /**
     * Builds the message for a character, faction and IslandIndex can be null.
     * @param characterIndex
     * @param islandIndex
     * @param faction
     */
    public CharacterMessage(int characterIndex, Faction faction, Integer islandIndex) {
        this.characterIndex = characterIndex;
        this.islandIndex = islandIndex;
        this.faction = faction;
    }

    /**
     * Builds the object from a string message.
     * @param message
     */
    public CharacterMessage(String message) throws BadActionPhaseMessage {
        try {
            String[] messages = message.split(" ");
            this.characterIndex = Integer.parseInt(messages[1]);
            if (messages.length == 3) {
                try {
                    islandIndex = Integer.parseInt(messages[2]);
                } catch(NumberFormatException e) {
                    faction = Faction.valueOf(messages[2]);
                }
            }
            else if (messages.length == 4) {
                faction = Faction.valueOf(messages[2]);
                islandIndex = Integer.parseInt(messages[3]);
            }
        } catch(Exception e) {
            throw(new BadActionPhaseMessage());
        }
    }

    /**
     * Stringifies the object, making the actual message.
     *
     * The message can be in different forms, depending on the requirements of the character.
     *
     * if the character doesn't require anything:
     * character [CharacterIndex]
     *
     * if the character requires a faction:
     * character [CharacterIndex] [Faction]
     *
     * if the character requires an island:
     * character [CharacterIndex] [IslandIndex]
     *
     * if the character requires both a faction and an island:
     * character [CharacterIndex] [Faction] [IslandIndex]
     *
     * @return the message.
     */
    @Override
    public String toString() {
        String message = "character " + characterIndex;
        if (faction != null) {
            message += " " + faction;
        }
        if (islandIndex != null) {
            message += " " + islandIndex;
        }
        return message;
    }

    public Integer getIslandIndex() {
        return islandIndex;
    }

    public Faction getFaction() {
        return faction;
    }
}
