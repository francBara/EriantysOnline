package it.polimi.ingsw.model.Cards.Characters;

import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Game.GameSerializer;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;
import junit.framework.TestCase;

public class CharactersTest extends TestCase {

    public void testBuild() {
        final Game game = new Game(2, true);
        for (int i = 0; i < 2; i++) {
            game.addPlayer(new Player(""));
        }
        game.initGame();

        final int cost = game.getCharacters().get(0).getCost();

        if (game.getCharacters().get(0).requiresIsland()) {
            game.getCharacters().get(0).getIslandRequirement().setIsland(game.gameMap.getIslands().get(0));
        }
        if (game.getCharacters().get(0).requiresFaction()) {
            game.getCharacters().get(0).getFactionRequirement().setFaction(Faction.RedDragons);
        }

        Students students = new Students();
        students.add(Faction.RedDragons, 4);
        game.getCharacters().get(0).setStudents(students);

        game.getCharacters().get(0).useEffect(game.getPlayers().get(0));

        assertEquals(cost + 1, game.getCharacters().get(0).getCost());

        final GameSerializer gameSerializer = new GameSerializer();

        final Game newGame = gameSerializer.deserialize(gameSerializer.serialize(game));

        assertEquals(cost + 1, newGame.getCharacters().get(0).getCost());
    }
}