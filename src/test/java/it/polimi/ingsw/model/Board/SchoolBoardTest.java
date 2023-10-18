package it.polimi.ingsw.model.Board;

import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Students;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

public class SchoolBoardTest extends TestCase {
    /**
     * The SchoolBoard getProfessors() method must correctly return the factions of its professors.
     *
     * The professors are manually updated by the single dining tables, then the SchoolBoard needs to return the correct values.
     */
    public void testGetProfessors() {
        SchoolBoard schoolBoard = new SchoolBoard(6, 6, TowerColor.Black);

        assertEquals(0, schoolBoard.getProfessors().size());

        schoolBoard.getDiningTable(Faction.PinkFairies).updateProfessor(true);
        schoolBoard.getDiningTable(Faction.RedDragons).updateProfessor(true);

        assertEquals(2, schoolBoard.getProfessors().size());
        assertTrue(schoolBoard.getProfessors().contains(Faction.PinkFairies));
        assertTrue(schoolBoard.getProfessors().contains(Faction.RedDragons));
    }
}