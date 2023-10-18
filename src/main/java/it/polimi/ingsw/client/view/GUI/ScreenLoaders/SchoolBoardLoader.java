package it.polimi.ingsw.client.view.GUI.ScreenLoaders;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIGameController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUISchoolBoardController;
import it.polimi.ingsw.model.Player.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class SchoolBoardLoader {
    private final Controller controller;
    private final GUIGameController gameController;

    private Pane playerPane;
    private FXMLLoader playerLoader;

    public SchoolBoardLoader(Controller controller, GUIGameController gameController) {
        this.controller = controller;
        this.gameController = gameController;
    }

    public Pane getSchoolBoardPane(Player player) {
        try {
            if (playerLoader == null || playerPane == null) {
                playerLoader = new FXMLLoader();
                playerLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/SchoolBoard.fxml")));
                playerPane = playerLoader.load();
            }

            GUISchoolBoardController guiController = playerLoader.getController();
            guiController.setController(controller);
            guiController.setGameController(gameController);

            guiController.refresh(player.getSchoolBoard(), true);
            return playerPane;
        } catch(IOException e) {

        }

        return null;
    }

    public GUISchoolBoardController getController() {
        return playerLoader.getController();
    }
}
