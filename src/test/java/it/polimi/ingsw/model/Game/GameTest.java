package it.polimi.ingsw.model.Game;

import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Player.Player;
import junit.framework.TestCase;

import java.util.ArrayList;

public class GameTest extends TestCase {
    /**
     * - Players need to be correctly added to the game.
     * - A just built game cannot contain players.
     */
    public void testAddPlayer() {
        Game game = new Game(6, false);

        assertTrue(game.getPlayers().isEmpty());

        Player player = new Player("");

        game.addPlayer(player);

        assertTrue(game.getPlayers().size() == 1 && game.getPlayers().get(0) == player);
    }

    /**
     * - There must be only one professor per faction for each player.
     * - A player obtains a professor if he has more students of that factions than every other student.
     * - Professor bonuses given by characters must be considered.
     * - If a player reaches the amount of students of another player that already has that professor, nothing changes.
     */
    public void testSetProfessors() {
        Game game = new Game(3, false);

        ArrayList<Player> players = new ArrayList<Player>();

        for (int i = 0; i < 3; i++) {
            Player player = new Player("");
            player.setSchoolBoard(new SchoolBoard(6,6, TowerColor.Black));
            players.add(player);
            game.addPlayer(player);
        }

        players.get(0).getSchoolBoard().getDiningTable(Faction.YellowGnomes).addStudent();
        players.get(1).getSchoolBoard().getDiningTable(Faction.PinkFairies).addStudent();
        players.get(2).getSchoolBoard().getDiningTable(Faction.RedDragons).addStudent();

        for (Player player : players) {
            assertEquals(0, player.getSchoolBoard().getProfessors().size());
        }

        game.setProfessors();

        for (Player player : players) {
            assertEquals(1, player.getSchoolBoard().getProfessors().size());
        }

        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.YellowGnomes));
        assertTrue(players.get(1).getSchoolBoard().getProfessors().contains(Faction.PinkFairies));
        assertTrue(players.get(2).getSchoolBoard().getProfessors().contains(Faction.RedDragons));

        for (int i = 0; i < 5; i++) {
            players.get(0).getSchoolBoard().getDiningTable(Faction.BlueUnicorns).addStudent();
        }

        game.setProfessors();

        assertEquals(2, players.get(0).getSchoolBoard().getProfessors().size());
        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.YellowGnomes));
        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.BlueUnicorns));

        for (int i = 0; i < 5; i++) {
            players.get(2).getSchoolBoard().getDiningTable(Faction.BlueUnicorns).addStudent();
        }

        game.setProfessors();

        assertEquals(2, players.get(0).getSchoolBoard().getProfessors().size());
        assertEquals(1, players.get(1).getSchoolBoard().getProfessors().size());
        assertEquals(1, players.get(2).getSchoolBoard().getProfessors().size());

        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.YellowGnomes));
        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.BlueUnicorns));
        assertTrue(players.get(1).getSchoolBoard().getProfessors().contains(Faction.PinkFairies));
        assertTrue(players.get(2).getSchoolBoard().getProfessors().contains(Faction.RedDragons));

        players.get(0).getSchoolBoard().getDiningTable(Faction.RedDragons).addStudent();

        game.setProfessors();

        assertEquals(2, players.get(0).getSchoolBoard().getProfessors().size());
        assertEquals(1, players.get(1).getSchoolBoard().getProfessors().size());
        assertEquals(1, players.get(2).getSchoolBoard().getProfessors().size());

        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.YellowGnomes));
        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.BlueUnicorns));
        assertTrue(players.get(1).getSchoolBoard().getProfessors().contains(Faction.PinkFairies));
        assertTrue(players.get(2).getSchoolBoard().getProfessors().contains(Faction.RedDragons));

        players.get(0).getSchoolBoard().getDiningTable(Faction.RedDragons).addStudent();

        game.setProfessors();

        assertEquals(3, players.get(0).getSchoolBoard().getProfessors().size());
        assertEquals(1, players.get(1).getSchoolBoard().getProfessors().size());
        assertEquals(0, players.get(2).getSchoolBoard().getProfessors().size());

        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.YellowGnomes));
        assertTrue(players.get(1).getSchoolBoard().getProfessors().contains(Faction.PinkFairies));

        players.get(0).setProfessorsBonus(true);

        game.setProfessors();

        assertEquals(3, players.get(0).getSchoolBoard().getProfessors().size());
        assertEquals(1, players.get(1).getSchoolBoard().getProfessors().size());
        assertEquals(0, players.get(2).getSchoolBoard().getProfessors().size());

        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.YellowGnomes));
        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.RedDragons));
        assertTrue(players.get(1).getSchoolBoard().getProfessors().contains(Faction.PinkFairies));

        for (int i = 0; i < 5; i++) {
            players.get(0).getSchoolBoard().getDiningTable(Faction.PinkFairies).addStudent();
        }

        game.setProfessors();

        assertEquals(4, players.get(0).getSchoolBoard().getProfessors().size());
        assertEquals(0, players.get(1).getSchoolBoard().getProfessors().size());
        assertEquals(0, players.get(2).getSchoolBoard().getProfessors().size());

        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.YellowGnomes));
        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.RedDragons));
        assertTrue(players.get(0).getSchoolBoard().getProfessors().contains(Faction.PinkFairies));
    }

    /**
     * - Towers need to be correctly placed on islands and removed from school boards
     * - The student who has the highest influence on an island should conquer that island
     */
    public void testPlaceTower() {
        Game game = new Game(3,false);

        for (TowerColor towerColor : TowerColor.values()) {
            Player player = new Player("");
            player.setSchoolBoard(new SchoolBoard(6, 6, towerColor));
            if (towerColor == TowerColor.Black) {
                player.getSchoolBoard().getDiningTable(Faction.RedDragons).updateProfessor(true);
            }
            else if (towerColor == TowerColor.White) {
                player.getSchoolBoard().getDiningTable(Faction.PinkFairies).updateProfessor(true);
            }
            game.addPlayer(player);
        }

        game.gameMap.getMotherNatureIsland().placeStudent(Faction.RedDragons);

        game.placeTower();

        assertEquals(TowerColor.Black, game.gameMap.getMotherNatureIsland().getTowersColor());

        for (Player player : game.getPlayers()) {
            if (player.getSchoolBoard().getTowersColor() == TowerColor.Black) {
                assertEquals(5, player.getSchoolBoard().getTowers());
            }
            else {
                assertEquals(6, player.getSchoolBoard().getTowers());
            }
        }

        game.placeTower();

        assertEquals(TowerColor.Black, game.gameMap.getMotherNatureIsland().getTowersColor());

        for (Player player : game.getPlayers()) {
            if (player.getSchoolBoard().getTowersColor() == TowerColor.Black) {
                assertEquals(5, player.getSchoolBoard().getTowers());
            }
            else {
                assertEquals(6, player.getSchoolBoard().getTowers());
            }
        }

        game.gameMap.getMotherNatureIsland().placeStudent(Faction.PinkFairies);
        game.gameMap.getMotherNatureIsland().placeStudent(Faction.PinkFairies);

        game.placeTower();

        assertEquals(TowerColor.Black, game.gameMap.getMotherNatureIsland().getTowersColor());

        for (Player player : game.getPlayers()) {
            if (player.getSchoolBoard().getTowersColor() == TowerColor.Black) {
                assertEquals(5, player.getSchoolBoard().getTowers());
            }
            else {
                assertEquals(6, player.getSchoolBoard().getTowers());
            }
        }

        game.gameMap.getMotherNatureIsland().placeStudent(Faction.PinkFairies);

        game.placeTower();

        assertEquals(TowerColor.White, game.gameMap.getMotherNatureIsland().getTowersColor());

        for (Player player : game.getPlayers()) {
            if (player.getSchoolBoard().getTowersColor() == TowerColor.White) {
                assertEquals(5, player.getSchoolBoard().getTowers());
            }
            else {
                assertEquals(6, player.getSchoolBoard().getTowers());
            }
        }
    }

    /**
     * Assistants needs to be correctly added and retrieved from the game.
     */
    public void testUsedAssistants() {
        Game game = new Game(3,false);

        Assistant assistant1 = new Assistant(1, 1, "Gianluca");
        Assistant assistant2 = new Assistant(2, 4, "Elisa");

        assertEquals(0, game.getUsedAssistants().size());

        game.addUsedAssistant(assistant1);
        game.addUsedAssistant(assistant2);

        assertTrue(game.getUsedAssistants().contains(assistant1));
        assertTrue(game.getUsedAssistants().contains(assistant2));
        assertEquals(2, game.getUsedAssistants().size());
    }

    /**
     * - School boards must be assigned to every player
     * - Towers must be assigned basing on the number of players
     * - Assistants must be assigned to every player
     * - 3 random Characters, if game mode is set to expert, are placed in the game
     * - The students are placed in the bag
     */
    public void testInitGame() {
        Game game = new Game( 3, false);

        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player(""));
        }

        //Moves mother nature to the island at index zero
        game.gameMap.moveMotherNature(12 - game.gameMap.getMotherNaturePosition());
        assertEquals(0, game.gameMap.getMotherNaturePosition());

        game.initGame();

        for (int i = 0; i < 12; i++) {
            if (i == 6 || i == 0) {
                assertEquals(0, game.gameMap.getIslands().get(i).getStudents().getTotalStudents());
            }
            else {
                assertEquals(1, game.gameMap.getIslands().get(i).getStudents().getTotalStudents());
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {
                assertFalse(game.getPlayers().get(i).getSchoolBoard().getTowersColor() == game.getPlayers().get(j).getSchoolBoard().getTowersColor());
            }
            assertEquals(6, game.getPlayers().get(i).getSchoolBoard().getTowers());
        }

        for (Player player : game.getPlayers()) {
            assertEquals(10, player.getAssistantsOwned().size());
            assertEquals(9, player.getSchoolBoard().getEntrance().getStudents().getTotalStudents());
        }

        game = new Game(2, false);

        for (int i = 0; i < 2; i++) {
            game.addPlayer(new Player(""));
        }

        //Moves mother nature to the island at index 10
        game.gameMap.moveMotherNature(22 - game.gameMap.getMotherNaturePosition());
        assertEquals(10, game.gameMap.getMotherNaturePosition());

        game.initGame();

        for (int i = 0; i < 12; i++) {
            if (i == 4 || i == 10) {
                assertEquals(0, game.gameMap.getIslands().get(i).getStudents().getTotalStudents());
            }
            else {
                assertEquals(1, game.gameMap.getIslands().get(i).getStudents().getTotalStudents());
            }
        }

        assertTrue(game.getPlayers().get(0).getSchoolBoard().getTowersColor() != game.getPlayers().get(1).getSchoolBoard().getTowersColor());
        assertEquals(8, game.getPlayers().get(0).getSchoolBoard().getTowers());
        assertEquals(8, game.getPlayers().get(1).getSchoolBoard().getTowers());

        for (Player player : game.getPlayers()) {
            assertEquals(10, player.getAssistantsOwned().size());
        }
    }

    /**
     * Testing for game serialization, with focus on character persistence between serialization and deserialization.
     */
    public void testBuildCharacters() {
        Game game = new Game(1, true);
        game.addPlayer(new Player(""));
        game.initGame();

        GameSerializer serializer = new GameSerializer();

        Game newGame = serializer.deserialize(serializer.serialize(game));

        assertEquals(3, newGame.getCharacters().size());

        assertEquals(3, game.getCharacters().size());

        for (int i = 0; i < 3; i++) {
            assertSame(game.getCharacters().get(i).getClass(), newGame.getCharacters().get(i).getClass());
            assertEquals(game.getCharacters().get(i).getCost(), newGame.getCharacters().get(i).getCost());
        }
    }

    /**
     * isGameOver() method must return the correct VictoryType based on end game specifics.
     */
    public void testIsGameOver() {
        Game game = new Game(2, true);

        for (int i = 0; i < 2; i++) {
            game.addPlayer(new Player(""));
        }

        game.initGame();

        assertEquals(VictoryType.None, game.isGameOver());

        for (int i = 0; i < 8; i++) {
            game.getPlayers().get(0).getSchoolBoard().removeTower();
        }

        assertEquals(VictoryType.EmptyTowers, game.isGameOver());

        game = new Game(3, true);

        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player(""));
        }

        game.initGame();

        assertEquals(VictoryType.None, game.isGameOver());

        for (Island island : game.gameMap.getIslands()) {
            island.placeTower(TowerColor.White);
        }

        for (int i = 0; i < 5; i++) {
            game.gameMap.mergeIslands();
        }

        assertEquals(VictoryType.IslandsGroups, game.isGameOver());

        game = new Game(2, false);

        for (int i = 0; i < 2; i++) {
            game.addPlayer(new Player(""));
        }

        game.initGame();

        assertEquals(VictoryType.None, game.isGameOver());

        for (int i = 0; i < 10; i++) {
            game.getPlayers().get(0).playAssistant(0);
        }

        assertEquals(VictoryType.EmptyAssistants, game.isGameOver());
    }

    public void testGetWinner() {
        Game game = new Game(3, false);

        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player(""));
        }

        game.initGame();

        game.getPlayers().get(1).getSchoolBoard().getDiningTable(Faction.RedDragons).updateProfessor(true);

        assertSame(game.getPlayers().get(1), game.getWinners().get(0));
        assertEquals(1, game.getWinners().size());

        game.getPlayers().get(0).getSchoolBoard().getDiningTable(Faction.PinkFairies).updateProfessor(true);
        game.getPlayers().get(0).getSchoolBoard().getDiningTable(Faction.BlueUnicorns).updateProfessor(true);

        assertSame(game.getPlayers().get(0), game.getWinners().get(0));
        assertEquals(1, game.getWinners().size());

        game = new Game(3, false);

        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player(""));
        }

        game.initGame();

        game.getPlayers().get(2).getSchoolBoard().removeTower();

        assertSame(game.getPlayers().get(2), game.getWinners().get(0));
        assertEquals(1, game.getWinners().size());

        game.getPlayers().get(1).getSchoolBoard().removeTower();
        game.getPlayers().get(1).getSchoolBoard().removeTower();

        assertSame(game.getPlayers().get(1), game.getWinners().get(0));
        assertEquals(1, game.getWinners().size());

        game.getPlayers().get(0).getSchoolBoard().removeTower();
        game.getPlayers().get(0).getSchoolBoard().removeTower();
        game.getPlayers().get(0).getSchoolBoard().removeTower();

        assertSame(game.getPlayers().get(0), game.getWinners().get(0));
        assertEquals(1, game.getWinners().size());

        game.getPlayers().get(1).getSchoolBoard().removeTower();

        assertTrue(game.getWinners().contains(game.getPlayers().get(0)) && game.getWinners().contains(game.getPlayers().get(1)));
        assertEquals(2, game.getWinners().size());

        game.getPlayers().get(1).getSchoolBoard().getDiningTable(Faction.YellowGnomes).updateProfessor(true);

        assertSame(game.getPlayers().get(1), game.getWinners().get(0));
        assertEquals(1, game.getWinners().size());
    }

    public void testStudentsToAddToClouds() {
        Game game = new Game(2, false);

        assertEquals(3, game.studentsToAddToClouds());

        game = new Game(3, false);

        assertEquals(4, game.studentsToAddToClouds());
    }

    public void testStudentsMovements() {
        Game game = new Game(2, false);

        assertEquals(3, game.studentsMovements());

        game = new Game(3, false);

        assertEquals(4, game.studentsMovements());
    }

    public void testGetCharacters() {
        Game game = new Game(3, true);
        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player(""));
        }
        game.initGame();

        ArrayList<CharacterCard> characters1 = game.getCharacters();
        ArrayList<CharacterCard> characters2 = game.getCharacters();

        assertNotSame(characters1, characters2);

        for (int i = 0; i < 3; i++) {
            assertSame(characters1.get(i), characters2.get(i));
        }
    }

    /**
     * The reset() method restores the game for a new turn, cleaning assistants and resetting characters effects.
     */
    public void testReset() {
        Game game = new Game(3, true);

        for (int i = 0; i < 3; i++) {
            game.addPlayer(new Player(""));
        }

        game.initGame();

        game.addUsedAssistant(new Assistant(3, 3, "Gianluca"));
        game.addUsedAssistant(new Assistant(5, 3, "Gianluca"));

        assertEquals(2, game.getUsedAssistants().size());

        for (Player player : game.getPlayers()) {
            player.setProfessorsBonus(true);
            player.setBonusInfluence(2);
            player.addBonusMotherNatureSteps();
        }
        for (Island island : game.gameMap.getIslands()) {
            island.setIgnoreTowers(true);
            island.setIgnoredFaction(Faction.RedDragons);
        }

        game.reset();

        for (Player player : game.getPlayers()) {
            assertFalse(player.getProfessorsBonus());
            assertEquals(0, player.getBonusInfluence());
        }
        for (Island island : game.gameMap.getIslands()) {
            assertFalse(island.getIgnoreTowers());
            assertNull(island.getIgnoredFaction());
        }

        assertTrue(game.getUsedAssistants().isEmpty());
    }
}