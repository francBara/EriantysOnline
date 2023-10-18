package it.polimi.ingsw.model.Cards.Assistants;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class AssistantsFactory {
    /**
     *
     * @return 10 assistants, by following Eriantys game rules.
     */
    public ArrayList<Assistant> getAssistants() {
        ArrayList<Assistant> assistants = new ArrayList<Assistant>();

        assistants.add(new Assistant(1, 1, "Cheetah"));
        assistants.add(new Assistant(2, 1, "Ostrich"));
        assistants.add(new Assistant(3, 2, "Cat"));
        assistants.add(new Assistant(4, 2, "Eagle"));
        assistants.add(new Assistant(5, 3, "Fox"));
        assistants.add(new Assistant(6, 3, "Lizard"));
        assistants.add(new Assistant(7, 4, "Octopus"));
        assistants.add(new Assistant(8, 4, "Dog"));
        assistants.add(new Assistant(9, 5, "Elephant"));
        assistants.add(new Assistant(10, 5, "Turtle"));

        return assistants;
    }
}
