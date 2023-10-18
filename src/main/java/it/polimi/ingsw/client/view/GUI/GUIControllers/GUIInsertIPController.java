package it.polimi.ingsw.client.view.GUI.GUIControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GUIInsertIPController extends GUIController {
    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private void initialize() {
        ipTextField.setText("127.0.0.1");
        portTextField.setText("1000");
    }

    @FXML
    private void onConfirm(final ActionEvent event) {
        int port;
        try {
            port = Integer.parseInt(portTextField.getText());
        } catch(NumberFormatException e) {
            return;
        }

        controller.connect(ipTextField.getText(), port);
    }

    @FXML
    private void onKeyPressed(final KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            int port;
            try {
                port = Integer.parseInt(portTextField.getText());
            } catch(NumberFormatException exception) {
                return;
            }

            controller.connect(ipTextField.getText(), port);
        }
    }
}
