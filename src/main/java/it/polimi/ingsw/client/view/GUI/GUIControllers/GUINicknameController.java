package it.polimi.ingsw.client.view.GUI.GUIControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GUINicknameController extends GUIController {
    @FXML
    private TextField nicknameTextField;

    @FXML
    private Button nicknameButton;

    @FXML
    private void onConfirm(final ActionEvent event) {
        controller.actionPerformed(nicknameTextField.getText());
    }

    @FXML
    private void onKeyPressed(final KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            controller.actionPerformed(nicknameTextField.getText());
        }
    }
}
