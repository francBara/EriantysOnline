package it.polimi.ingsw.model.Cards.Characters.CharacterTypes;

import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Player.Player;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Map;

public class CharactersFactoryTest extends TestCase {
    public void testGetRandom() {
        CharactersFactory charactersFactory = new CharactersFactory(new Game(6, true));

        ArrayList<CharacterCard> characters = charactersFactory.getRandom();

        assertEquals(3, characters.size());
    }

    public void testGetAllCharacters() {
        CharactersFactory charactersFactory = new CharactersFactory(new Game(6, true));

        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        assertEquals(8, characters.size());

        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {
                assertNotSame(characters.get(i), characters.get(j));
            }
        }

        assertTrue(characters.get(0) instanceof FactionIslandCharacter);
        assertTrue(characters.get(1) instanceof SimpleCharacter);
        assertTrue(characters.get(2) instanceof IslandCharacter);
        assertTrue(characters.get(3) instanceof SimpleCharacter);
        assertTrue(characters.get(4) instanceof SimpleCharacter);
        assertTrue(characters.get(5) instanceof SimpleCharacter);
        assertTrue(characters.get(6) instanceof FactionCharacter);
        assertTrue(characters.get(7) instanceof FactionCharacter);

        assertEquals(1, characters.get(0).getCost());
        assertEquals(2, characters.get(1).getCost());
        assertEquals(3, characters.get(2).getCost());
        assertEquals(1, characters.get(3).getCost());
        assertEquals(3, characters.get(4).getCost());
        assertEquals(2, characters.get(5).getCost());
        assertEquals(3, characters.get(6).getCost());
        assertEquals(3, characters.get(7).getCost());

    }

    public void testFirstCharacter() {
        Game game = new Game(6, true);
        game.bag.addBackStudents(Faction.GreenFrogs, 10);

        final int bagStudents = game.bag.getStudents().getTotalStudents();

        CharactersFactory charactersFactory = new CharactersFactory(game);
        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        CharacterCard character = characters.get(0);
        int characterCost = character.getCost();



        assertTrue(character.factionRequirement.getFromFactions().containsAll(character.students.getFactions()));
        assertEquals(character.factionRequirement.getFromFactions().size(), character.students.getFactions().size());

        Faction chosenFaction = null;

        for (Faction faction : character.factionRequirement.getFromFactions()) {
            chosenFaction = faction;
            break;
        }

        character.factionRequirement.setFaction(chosenFaction);

        Island island = new Island(0);

        character.islandRequirement.setIsland(island);

        character.useEffect(new Player(""));
        assertEquals(characterCost + 1, character.getCost());

        assertEquals(1, island.getStudents().get(chosenFaction));

        assertEquals(4, character.students.getTotalStudents());

        assertEquals(bagStudents - 1, game.bag.getStudents().getTotalStudents());
    }

    public void testSecondCharacter() {
        Game game = new Game(2, true);

        CharactersFactory charactersFactory = new CharactersFactory(game);
        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        CharacterCard character = characters.get(1);
        int characterCost = character.getCost();

        Player player1 = new Player("");
        Player player2 = new Player("");
        game.addPlayer(player1);
        game.addPlayer(player2);

        game.initGame();

        player2.getSchoolBoard().getDiningTable(Faction.RedDragons).addStudent();

        game.setProfessors();

        assertTrue(player2.getSchoolBoard().getDiningTable(Faction.RedDragons).hasProfessor());

        player1.getSchoolBoard().getDiningTable(Faction.RedDragons).addStudent();

        game.setProfessors();

        assertTrue(player2.getSchoolBoard().getDiningTable(Faction.RedDragons).hasProfessor());
        assertFalse(player1.getSchoolBoard().getDiningTable(Faction.RedDragons).hasProfessor());

        assertFalse(player1.getProfessorsBonus());

        character.useEffect(player1);

        assertTrue(player1.getProfessorsBonus());
        assertFalse(player2.getProfessorsBonus());

        game.setProfessors();

        assertTrue(player1.getSchoolBoard().getDiningTable(Faction.RedDragons).hasProfessor());
        assertFalse(player2.getSchoolBoard().getDiningTable(Faction.RedDragons).hasProfessor());

        assertEquals(characterCost + 1, character.getCost());
    }

    public void testThirdCharacter() {
        Game game = new Game(3, true);

        CharactersFactory charactersFactory = new CharactersFactory(game);
        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        CharacterCard character = characters.get(2);
        int characterCost = character.getCost();

        if (game.gameMap.getMotherNaturePosition() == 5) {
            game.gameMap.moveMotherNature(3);
        }

        final int motherNaturePosition = game.gameMap.getMotherNaturePosition();

        character.getIslandRequirement().setIsland(game.gameMap.getIslands().get(5));

        character.useEffect(new Player(""));

        assertEquals(characterCost + 1, character.getCost());
        assertEquals(motherNaturePosition, game.gameMap.getMotherNaturePosition());
    }

    public void testForthCharacter() {
        Game game = new Game(6, true);

        CharactersFactory charactersFactory = new CharactersFactory(game);
        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        CharacterCard character = characters.get(3);
        int characterCost = character.getCost();

        Player player = new Player("");

        assertEquals(0, player.getMotherNatureValue());

        for (int i = 0; i < 10; i++) {
            character.useEffect(player);
        }

        assertEquals(20, player.getMotherNatureValue());

        assertEquals(characterCost + 1, character.getCost());
    }

    public void testFifthCharacter() {
        Game game = new Game(6, true);

        CharactersFactory charactersFactory = new CharactersFactory(game);
        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        CharacterCard character = characters.get(4);
        int characterCost = character.getCost();

        for (Island island : game.gameMap.getIslands()) {
            assertFalse(island.getIgnoreTowers());
        }

        character.useEffect(new Player(""));

        for (Island island : game.gameMap.getIslands()) {
            assertTrue(island.getIgnoreTowers());
        }

        assertEquals(characterCost + 1, character.getCost());
    }

    public void testSixthCharacter() {
        Game game = new Game(6, true);

        CharactersFactory charactersFactory = new CharactersFactory(game);
        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        CharacterCard character = characters.get(5);
        int characterCost = character.getCost();

        Player player = new Player("");

        assertEquals(0, player.getBonusInfluence());

        character.useEffect(player);

        assertEquals(2, player.getBonusInfluence());

        assertEquals(characterCost + 1, character.getCost());
    }

    public void testSeventhCharacter() {
        Game game = new Game(6, true);

        CharactersFactory charactersFactory = new CharactersFactory(game);
        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        CharacterCard character = characters.get(6);
        int characterCost = character.getCost();

        character.getFactionRequirement().setFaction(Faction.RedDragons);

        for (Island island : game.gameMap.getIslands()) {
            assertNull(island.getIgnoredFaction());
        }

        character.useEffect(new Player(""));

        for (Island island : game.gameMap.getIslands()) {
            assertEquals(Faction.RedDragons, island.getIgnoredFaction());
        }

        assertEquals(characterCost + 1, character.getCost());
    }

    public void testEightCharacter() {
        Game game = new Game(6, true);

        final int bagStudents = game.bag.getStudents().getTotalStudents();

        CharactersFactory charactersFactory = new CharactersFactory(game);
        ArrayList<CharacterCard> characters = charactersFactory.getAllCharacters();

        CharacterCard character = characters.get(7);
        int characterCost = character.getCost();

        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player(""));
            game.getPlayers().get(i).setSchoolBoard(new SchoolBoard(6, 6, TowerColor.Black));
            for (int j = 0; j < 7; j++) {
                game.getPlayers().get(i).getSchoolBoard().getDiningTable(Faction.GreenFrogs).addStudent();
            }
        }

        character.getFactionRequirement().setFaction(Faction.GreenFrogs);
        character.useEffect(new Player(""));

        for (Player player : game.getPlayers()) {
            assertEquals(4, player.getSchoolBoard().getDiningTable(Faction.GreenFrogs).getStudents());
        }

        assertEquals(bagStudents + 9, game.bag.getStudents().getTotalStudents());
        assertEquals(characterCost + 1, character.getCost());
    }
}