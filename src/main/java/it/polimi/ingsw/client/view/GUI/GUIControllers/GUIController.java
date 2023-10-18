package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.client.ClientConnectionHandler;
import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.Screens;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Random;

/**
 * Class inherited by every JavaFX controller in the game, it ensures that every GUIController has access to the client Controller, the Screens class and a cache of loaded images.
 * @author Francesco Barabino
 */
public abstract class GUIController {
    protected Controller controller;
    protected Screens screens;

    private static final HashMap<String, Image> images = new HashMap<String, Image>();

    /**
     *
     * @param controller the client Controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setScreens(Screens screens) {
        this.screens = screens;
    }

    /**
     * Returns a JavaFX image loaded from the given path, if the image wasn't loaded before, it gets cached for faster future retrieve.
     * @param imagePath the disk path of the image to load
     * @return a JavaFX image instance
     */
    protected Image loadImage(String imagePath) {
        synchronized (images) {
            if (!images.containsKey(imagePath)) {
                images.put(imagePath, new Image(String.valueOf(getClass().getClassLoader().getResource(imagePath))));
            }
        }
        return images.get(imagePath);
    }

    /**
     * Updates a cached image with a different value.
     * @param imagePath the path of the new image to load
     * @param key the key of the previously saved image
     */
    protected void updateImage(String imagePath, String key) {
        synchronized (images) {
            images.put(key, new Image(String.valueOf(getClass().getClassLoader().getResource(imagePath))));
        }
    }
}
