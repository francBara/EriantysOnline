package it.polimi.ingsw.client.view.GUI.GUIControllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class GUIWaitingPlayersController extends GUIController {
    @FXML
    private void onDismiss(final MouseEvent e) {
        controller.actionPerformed("quit");
    }
}
