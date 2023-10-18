package it.polimi.ingsw.client.view.GUI;
import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents the screens in the GUI, which caches the current scene, its controller and the main scene.
 */
public class Screens {
    private Pane screen;
    private FXMLLoader loader;
    private Scene main;
    private final Controller controller;
    private String currentScreen;

    /**
     *
     * @param initialScene the path to the fist scene to display
     * @param controller the client controller
     * @throws IOException if the initialScene couldn't be found.
     */
    public Screens(String initialScene, Controller controller) throws IOException {
        this.controller = controller;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/" + initialScene + ".fxml")));

        loader = fxmlLoader;
        screen = fxmlLoader.load();

        GUIController guiController = fxmlLoader.getController();
        guiController.setController(controller);
        guiController.setScreens(this);

        currentScreen = initialScene;
    }

    /**
     * Sets the main scene for the whole game lifecycle.
     * @param scene
     */
    public void setMain(Scene scene) {
        main = scene;
    }

    /**
     * @return the Pane of the current screen
     */
    public Pane get() {
        return screen;
    }

    /**
     *
     * @param name the name of the screen to get the GUIController of
     * @return the GUIControleller associated to the requested screen
     */
    public GUIController getGUIController(String name) {
        return loader.getController();
    }

    /**
     * Switches the screen to the requested one. It loads the new screen from disk if not already loaded.
     * @param name name of the screen to switch to
     */
    public void switchTo(String name){
        if (!name.equals(currentScreen)) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/" + name + ".fxml")));
            loader = fxmlLoader;
            try {
                screen = fxmlLoader.load();
                GUIController guiController = fxmlLoader.getController();
                if (guiController != null) {
                    guiController.setController(controller);
                    guiController.setScreens(this);
                }
            } catch(IOException e) {
                return;
            }
        }

        Platform.runLater(() -> {
            main.setRoot(screen);
        });
        currentScreen = name;
    }

    /**
     *
     * @return the name of the currently visible screen.
     */
    public String getCurrentScreen() {
        return currentScreen;
    }
}
