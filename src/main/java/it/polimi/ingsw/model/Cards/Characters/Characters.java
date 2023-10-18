package it.polimi.ingsw.model.Cards.Characters;

import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharactersFactory;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the characters in a game, making serialization, deserialization and caching easier.
 * @author Francesco Barabino
 */
public class Characters {
    private final ArrayList<CharacterCache> characterIds;
    private final HashMap<Integer, Students> charactersStudents;
    private transient ArrayList<CharacterCard> characters;

    public Characters(Game game) {
        characters = new CharactersFactory(game).getRandom();
        characterIds = new ArrayList<CharacterCache>();
        charactersStudents = new HashMap<Integer, Students>();
        //Ensures that every characterId has the same index of the corresponding character.
        for (CharacterCard character : characters) {
            characterIds.add(new CharacterCache(character.id));
            if (character.getStudents() != null) {
                charactersStudents.put(character.id, character.getStudents());
            }
        }
    }

    /**
     *
     * @return The characters in this class
     */
    public ArrayList<CharacterCard> get() {
        return new ArrayList<CharacterCard>(characters);
    }

    /**
     * Builds the CharacterCard classes for the characters in the game, starting from the cached ids.
     * @param game
     */
    public void build(Game game) {
        ArrayList<CharacterCard> allCharacters = new CharactersFactory(game).getAllCharacters();
        characters = new ArrayList<CharacterCard>();
        for (CharacterCache characterCache : characterIds) {
            characters.add(allCharacters.get(characterCache.id));
            if (characterCache.isUsed()) {
                characters.get(characters.size() - 1).setUsed();
            }

            if (charactersStudents.containsKey(characterCache.id)) {
                characters.get(characters.size() - 1).setStudents(charactersStudents.get(characterCache.id));
            }
        }
    }

    /**
     * Caches current data about the CharacterCard classes.
     */
    public void cache() {
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).isUsed()) {
                characterIds.get(i).setUsed();
            }

            if (characters.get(i).getStudents() != null) {
                charactersStudents.put(characters.get(i).id, characters.get(i).getStudents());
            }
        }
    }

    /**
     * Helper class which contains the id of a character and a value indicating if he was used.
     */
    private static class CharacterCache {
        public final int id;
        private boolean isUsed = false;

        CharacterCache(int id) {
            this.id = id;
        }

        public void setUsed() {
            this.isUsed = true;
        }

        public boolean isUsed() {
            return isUsed;
        }
    }
}
