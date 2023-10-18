package it.polimi.ingsw.model.Cards.Characters.CharacterTypes;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Factory to build all characters in the game, following Eriantys specifics.
 * @author Francesco Barabino
 */
public class CharactersFactory {
    private ArrayList<CharacterCard> characters;

    public CharactersFactory(Game game) {
        characters = new ArrayList<CharacterCard>();

        Students students = new Students();

        for (int i = 0; i < 4; i++) {
            students.add(Faction.getRandom());
        }

        /*
        First in rules.
        Choose a student from this card and place it on an island, then take one student from the bag and place it on this card.
         */
        characters.add(new FactionIslandCharacter(
            0,
            "Monk",
            "In setup, draw 4 Students and place them on this card.\nEFFECT: Take 1 Student from this card and place it on an Island of your choice.\nThen, draw a new Student from the Bag and place it on this card.",
            1,
            students,
            (Faction chosenStudent, Island island) -> {
                island.placeStudent(chosenStudent);
            },
            (Faction chosenStudent, Students cardStudents) -> {
                cardStudents.remove(chosenStudent);
                cardStudents.add(game.bag.takeStudent());
            }
        ));

        /*
        Second in rules.
        Gain professors bonus.
         */
        characters.add(new SimpleCharacter(
            1,
            "Farmer",
            "During this turn, you take control of any number of Professors even if you have the same number of Students as the player who currently controls them.",
            2,
            (Player player) -> {
                player.setProfessorsBonus(true);
                game.setProfessors();
            }
        ));

        /*
        Third in rules.
        Resolve an island as if Mother Nature has ended her movement there.
         */
        characters.add(new IslandCharacter(
            2,
            "Herald",
            "Choose an Island and resolve the Island as if Mother Nature has ended her movement there.\nMother Nature will still move and the Island where she ends her movement will also be resolved.",
            3,
            (Island island) -> {
                int islandIndex = 0;
                int currentIslandIndex = game.gameMap.getMotherNaturePosition();
                for (int i = 0; i < game.gameMap.getIslands().size(); i++) {
                    if (island == game.gameMap.getIslands().get(i)) {
                        islandIndex = i;
                        break;
                    }
                }
                game.gameMap.moveMotherNature(game.gameMap.getIslandNecessarySteps(islandIndex));
                game.placeTower();
                game.gameMap.mergeIslands();
                game.gameMap.moveMotherNature(game.gameMap.getIslandNecessarySteps(currentIslandIndex));
            }
        ));

        /*
        Forth in rules.
        Bonus in mother nature movement.
         */
        characters.add(new SimpleCharacter(
            3,
            "Magic Postman",
            "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.",
            1,
            Player::addBonusMotherNatureSteps
        ));

        /*
        Sixth in rules.
        Towers are ignored in influence calculation.
         */
        characters.add(new SimpleCharacter(
            4,
            "Centaurus",
            "When resolving a Conquering on an Island, Towers do not count towards influence.",
            3,
            (Player player) -> {
                for (Island island : game.gameMap.getIslands()) {
                    island.setIgnoreTowers(true);
                }
            }
        ));

        /*
        Eight in rules.
        Bonus influence for one turn.
         */
        characters.add(new SimpleCharacter(
            5,
            "Knight",
            "During the influence calculation this turn, you count as having 2 more influence.",
            2,
            (Player player) -> {
                player.setBonusInfluence(2);
            }
        ));

        /*
        Ninth in rules.
        A chosen faction is ignored for influence calculation.
         */
        characters.add(new FactionCharacter(
            6,
            "Mushroom Man",
            "Choose a color of Student: during the influence calculation this turn, that color adds no influence.",
            3,
            (Faction ignoredFaction) -> {
                for (Island island : game.gameMap.getIslands()) {
                    island.setIgnoredFaction(ignoredFaction);
                }
            }
        ));

        /*
        Twelfth in rules.
        Every player gives back 3 students from the hall of a certain faction.
         */
        characters.add(new FactionCharacter(
            7,
            "Thief",
            "Choose a type of Student: every player (including yourself) must return 3 Students of that type from their Dining Room to the Bag.\n If any player has fewer than 2 Students of that type, return as many Students as they have.",
            3,
            (Faction faction) -> {
                for (Player matchPlayer : game.getPlayers()) {
                    int removedStudents = matchPlayer.getSchoolBoard().getDiningTable(faction).removeStudents(3);
                    game.bag.addBackStudents(faction, removedStudents);
                }
            }
        ));
    }

    /**
     *
     * @return 3 distinct random characters from all the available characters.
     */
    public ArrayList<CharacterCard> getRandom() {
        ArrayList<CharacterCard> randomCharacters = new ArrayList<>();

        ArrayList<Integer> randomNumbers = new ArrayList<Integer>();

        for (int i = 0; i < characters.size(); i++) {
            randomNumbers.add(i);
        }

        Collections.shuffle(randomNumbers);

        for (int i = 0; i < 3; i++) {
            randomCharacters.add(characters.get(randomNumbers.get(i)));
        }

        return randomCharacters;
    }

    /**
     * Returns a copy of the array containing all characters in the game.
     * @return all characters in the game
     */
    public ArrayList<CharacterCard> getAllCharacters() {
        return new ArrayList<CharacterCard>(characters);
    }
}
