package it.polimi.ingsw.client.view.GUI.GUIControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GUIServerErrorController extends GUIController {
    @FXML
    private void onExit(final ActionEvent e) {
        screens.switchTo("StartScreen");
    }
}
