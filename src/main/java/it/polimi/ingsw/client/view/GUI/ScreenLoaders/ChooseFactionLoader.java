package it.polimi.ingsw.client.view.GUI.ScreenLoaders;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUICharacterChooseFactionController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIChooseCharactersController;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Faction;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class ChooseFactionLoader {
    private Pane playerPane;
    private FXMLLoader playerLoader;

    public Pane getFactionsScreen(Runnable onDismiss, Consumer<Faction> onFactionChosen) {
        try {
            if (playerLoader == null || playerPane == null) {
                playerLoader = new FXMLLoader();
                playerLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/CharacterChooseFaction.fxml")));
                playerPane = playerLoader.load();
            }

            GUICharacterChooseFactionController guiController = playerLoader.getController();
            guiController.setOnDismiss(onDismiss);
            guiController.setOnFactionChosen(onFactionChosen);

            return playerPane;
        } catch(IOException e) {

        }

        return null;
    }
}
