package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import it.polimi.ingsw.model.Cards.Assistants.AssistantsFactory;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Objects;

public class AssistantsFactoryTest extends TestCase {
    public void testAssistantsFactory() {
        AssistantsFactory assistantsFactory = new AssistantsFactory();

        ArrayList<Assistant> assistants = assistantsFactory.getAssistants();

        assertEquals(10, assistants.size());

        assertTrue(assistants.stream().allMatch((Objects::nonNull)));

        for (int i = 0; i < assistants.size(); i++) {
            for (int j = i + 1; j < assistants.size(); j++) {
                assertNotSame(assistants.get(i), assistants.get(j));
                assertFalse(assistants.get(i).turnValue == assistants.get(j).turnValue);
            }
        }
    }
}