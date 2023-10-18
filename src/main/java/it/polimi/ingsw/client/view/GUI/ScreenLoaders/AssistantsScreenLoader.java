package it.polimi.ingsw.client.view.GUI.ScreenLoaders;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIChooseAssistantsController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIChooseCharactersController;
import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AssistantsScreenLoader {
    private Pane playerPane;
    private FXMLLoader playerLoader;

    public Pane getAssistantsScreen(ArrayList<Assistant> assistants, Controller controller, Runnable onAssistantChosen) {
        try {
            if (playerLoader == null || playerPane == null) {
                playerLoader = new FXMLLoader();
                playerLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/ChooseAssistants.fxml")));
                playerPane = playerLoader.load();
            }

            GUIChooseAssistantsController guiController = playerLoader.getController();
            guiController.setController(controller);
            guiController.setOnAssistantChosen(onAssistantChosen);

            guiController.showAssistants(assistants);

            return playerPane;
        } catch(IOException e) {

        }

        return null;
    }

    public GUIChooseAssistantsController getController() {
        return playerLoader.getController();
    }
}
