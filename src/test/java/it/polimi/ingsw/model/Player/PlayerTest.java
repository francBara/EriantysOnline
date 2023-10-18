package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Player.exceptions.EmptyNicknameException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class PlayerTest extends TestCase {
    /**
     * - The Player must be initialized without any assistant
     * - The Assistants must be added and correctly get by getter methods
     */
    public void testChooseAssistantDeck() {
        Player player = new Player("");

        HashSet<Assistant> assistants = new HashSet<Assistant>();

        for (int i = 0; i < 5; i++) {
            assistants.add(new Assistant(5, 5, "a"));
        }

        assertEquals(0, player.getAssistantsOwned().size());

        player.chooseAssistantDeck(assistants);

        assertEquals(5, player.getAssistantsOwned().size());
    }

    public void testProfessorBonus() {
        Player player = new Player("");

        assertFalse(player.getProfessorsBonus());

        player.setProfessorsBonus(true);

        assertTrue(player.getProfessorsBonus());

        player.setProfessorsBonus(false);
        assertFalse(player.getProfessorsBonus());
    }

    public void testBonusInfluence() {
        Player player = new Player("");

        assertEquals(0, player.getBonusInfluence());

        player.setBonusInfluence(5);

        assertEquals(5, player.getBonusInfluence());
    }

    public void testMotherNatureValue() {
        Player player = new Player("");
        HashSet<Assistant> assistants = new HashSet<Assistant>();
        assistants.add(new Assistant(1, 7, "a"));
        player.chooseAssistantDeck(assistants);

        assertEquals(0, player.getMotherNatureValue());

        player.playAssistant(0);

        assertEquals(7, player.getMotherNatureValue());
    }

    public void testAssistantValue() {
        Player player = new Player("");
        HashSet<Assistant> assistants = new HashSet<Assistant>();
        assistants.add(new Assistant(3, 0, "a"));
        player.chooseAssistantDeck(assistants);

        assertNull(player.getPlayedAssistant());

        player.playAssistant(0);

        assertEquals(3, player.getPlayedAssistant().turnValue);
    }

    public void testCoins() {
        Player player = new Player("");

        assertEquals(1, player.getCoins());

        player.addCoins(4);

        assertEquals(5, player.getCoins());
    }

    /**
     * Player turn ordering must account:
     * - The turn values of the played assistants for every player
     * - If two players have the same turn value, comparison will be performed basing on the queue they were set on
     */
    public void testComparable() {
        ArrayList<Player> players = new ArrayList<Player>();

        for (int i = 0; i < 6; i++) {
            players.add(new Player(""));
            HashSet<Assistant> assistantsSet = new HashSet<Assistant>();
            assistantsSet.add(new Assistant(5 - i, 0, "Gianluca"));
            players.get(i).chooseAssistantDeck(assistantsSet);
            players.get(i).playAssistant(0);
        }

        ArrayList<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort(Comparator.naturalOrder());

        for (int i = 0; i < 6; i++) {
            assertSame(sortedPlayers.get(i), players.get(5 - i));
        }
        
        players = new ArrayList<Player>();
        
        HashSet<Assistant> assistants = new HashSet<Assistant>();
        assistants.add(new Assistant(4, 1, "Gianluca"));

        for (int i = 0; i < 3; i++) {
            players.add(new Player(""));
            players.get(i).chooseAssistantDeck(assistants);
            players.get(i).playAssistant(0);
            players.get(i).setAssistantQueue(2 - i);
        }

        assistants.clear();
        assistants.add(new Assistant(2, 1, "Gianluca"));

        for (int i = 3; i < 5; i++) {
            players.add(new Player(""));
            players.get(i).chooseAssistantDeck(assistants);
            players.get(i).playAssistant(0);
            players.get(i).setAssistantQueue(i);
        }

        sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort(Comparator.naturalOrder());

        assertEquals(players.size(), sortedPlayers.size());

        assertSame(players.get(0), sortedPlayers.get(4));
        assertSame(players.get(1), sortedPlayers.get(3));
        assertSame(players.get(2), sortedPlayers.get(2));
        assertSame(players.get(3), sortedPlayers.get(0));
        assertSame(players.get(4), sortedPlayers.get(1));
    }

    public void testSetSchoolBoard() {
        Player player = new Player("");

        assertNull(player.getSchoolBoard());

        SchoolBoard schoolBoard = new SchoolBoard(6, 6, TowerColor.Black);

        player.setSchoolBoard(schoolBoard);

        assertSame(schoolBoard, player.getSchoolBoard());
    }

    public void testRemoveCoins() {
        Player player = new Player("");

        player.addCoins(4);

        assertEquals(5, player.getCoins());

        player.removeCoins(2);

        assertEquals(3, player.getCoins());

        player.removeCoins(23);

        assertEquals(0, player.getCoins());
    }
}