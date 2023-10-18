package it.polimi.ingsw.model.Messages;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

public class AvailableGamesTest extends TestCase {

    public void testAddGame() {
        AvailableGames availableGames = new AvailableGames();

        AvailableGame availableGame = new AvailableGame(12, 2, 2, GameType.Interrupted);

        availableGames.addGame(availableGame);

        assertSame(availableGame, availableGames.get(12));

        availableGame = new AvailableGame(3, 1, 3, GameType.Playing);

        availableGames.addGame(availableGame);

        assertSame(availableGame, availableGames.get(3));

        availableGames.addGame(new AvailableGame(10, 2, 3, GameType.Playing));

        Collection<Integer> ids = availableGames.getIDs();

        assertEquals(3, ids.size());

        assertTrue(ids.contains(12));
        assertTrue(ids.contains(3));
        assertTrue(ids.contains(10));

        ArrayList<AvailableGame> interrupted = availableGames.getType(GameType.Interrupted);
        ArrayList<AvailableGame> playing = availableGames.getType(GameType.Playing);
        ArrayList<AvailableGame> lobbies = availableGames.getType(GameType.Lobby);

        assertTrue(lobbies.isEmpty());
        assertEquals(1, interrupted.size());
        assertEquals(2, playing.size());
    }
}