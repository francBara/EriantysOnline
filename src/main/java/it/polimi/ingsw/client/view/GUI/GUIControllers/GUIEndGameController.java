package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.model.Messages.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class GUIEndGameController extends GUIController {
    @FXML
    private Text endGameText;

    public void display(Message message) {
        if (message == Message.Won) {
            endGameText.setText("You won!");
        }
        else if (message == Message.Tie) {
            endGameText.setText("It's a tie!");
        }
        else if (message == Message.Lost) {
            endGameText.setText("Oh oh, you lost...");
        }
    }

    @FXML
    private void onExit(final MouseEvent e) {
        controller.actionPerformed("quit");
    }
}
