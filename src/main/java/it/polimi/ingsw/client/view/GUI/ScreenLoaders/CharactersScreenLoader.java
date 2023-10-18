package it.polimi.ingsw.client.view.GUI.ScreenLoaders;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIChooseCharactersController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIEnemySchoolBoardController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIGameController;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Player.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CharactersScreenLoader {
    private Pane playerPane;
    private FXMLLoader playerLoader;

    public Pane getCharactersScreen(Controller controller, GUIGameController gameController) {
        try {
            if (playerLoader == null || playerPane == null) {
                playerLoader = new FXMLLoader();
                playerLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/ChooseCharacters.fxml")));
                playerPane = playerLoader.load();
            }

            GUIChooseCharactersController guiController = playerLoader.getController();
            guiController.setGameController(gameController);
            guiController.setController(controller);

            return playerPane;
        } catch(IOException e) {

        }

        return null;
    }

    public GUIChooseCharactersController getController() {
        return playerLoader.getController();
    }
}
