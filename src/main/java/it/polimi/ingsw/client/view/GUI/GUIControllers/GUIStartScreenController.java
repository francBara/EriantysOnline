package it.polimi.ingsw.client.view.GUI.GUIControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GUIStartScreenController extends GUIController {
    @FXML
    private Button startButton;

    @FXML
    private void onStart(final ActionEvent event) {
        screens.switchTo("InsertIP");
    }
}
