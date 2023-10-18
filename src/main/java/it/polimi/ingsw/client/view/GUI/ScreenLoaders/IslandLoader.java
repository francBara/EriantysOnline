package it.polimi.ingsw.client.view.GUI.ScreenLoaders;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIGameController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIIslandController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUISchoolBoardController;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Player.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;

public class IslandLoader {
    private final Controller controller;

    private StackPane pane;
    private FXMLLoader loader;

    private final GUIGameController gameController;
    private final SchoolBoardLoader schoolBoardLoader;

    public IslandLoader(Controller controller, GUIGameController gameController, SchoolBoardLoader schoolBoardLoader) {
        this.controller = controller;
        this.gameController = gameController;
        this.schoolBoardLoader = schoolBoardLoader;
    }

    public StackPane getIsland(Island island, int islandIndex) {
        try {
            if (loader == null || pane == null) {
                loader = new FXMLLoader();
                loader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/Island.fxml")));
                pane = loader.load();

                GUIIslandController guiController = loader.getController();
                guiController.setController(controller);
                guiController.setGUIControllers(gameController, schoolBoardLoader);

                guiController.init(islandIndex);
            }

            return pane;
        } catch(IOException e) {

        }

        return null;
    }

    public void delete() {
        pane.setManaged(false);
        pane.setVisible(false);
    }

    public GUIIslandController getController() {
        return loader.getController();
    }
}
