package it.polimi.ingsw.client.view.GUI.GUIControllers;

import javafx.animation.FillTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GUIMakeGameController extends GUIController {
    @FXML
    private Rectangle twoPlayers;

    @FXML
    private Rectangle threePlayers;

    @FXML
    private Rectangle normal;

    @FXML
    private Rectangle expert;

    @FXML
    private Button gameSettingsButton;

    private int playersNumber = 2;
    private boolean isExpert = true;

    private final Color highlightColor = Color.valueOf("#abffc6");

    @FXML
    private void makeGame(final ActionEvent event) {
        controller.actionPerformed(playersNumber + " " + isExpert);
    }

    @FXML
    private void onTwoPlayers(final MouseEvent e) {
        if (playersNumber == 2) {
            return;
        }

        playersNumber = 2;

        FillTransition fillTransition1 = new FillTransition(Duration.millis(200), twoPlayers);
        fillTransition1.setFromValue(Color.WHITE);
        fillTransition1.setToValue(highlightColor);

        FillTransition fillTransition2 = new FillTransition(Duration.millis(200), threePlayers);
        fillTransition2.setFromValue(highlightColor);
        fillTransition2.setToValue(Color.WHITE);

        fillTransition1.play();
        fillTransition2.play();
    }

    @FXML
    private void onThreePlayers(final MouseEvent e) {
        if (playersNumber == 3) {
            return;
        }

        playersNumber = 3;

        FillTransition fillTransition1 = new FillTransition(Duration.millis(200), threePlayers);
        fillTransition1.setFromValue(Color.WHITE);
        fillTransition1.setToValue(highlightColor);

        FillTransition fillTransition2 = new FillTransition(Duration.millis(200), twoPlayers);
        fillTransition2.setFromValue(highlightColor);
        fillTransition2.setToValue(Color.WHITE);

        fillTransition1.play();
        fillTransition2.play();
    }

    @FXML
    private void onNormal(final MouseEvent e) {
        if (!isExpert) {
            return;
        }

        isExpert = false;

        FillTransition fillTransition1 = new FillTransition(Duration.millis(200), normal);
        fillTransition1.setFromValue(Color.WHITE);
        fillTransition1.setToValue(highlightColor);


        FillTransition fillTransition2 = new FillTransition(Duration.millis(200), expert);
        fillTransition2.setFromValue(highlightColor);
        fillTransition2.setToValue(Color.WHITE);

        fillTransition1.play();
        fillTransition2.play();
    }

    @FXML
    private void onExpert(final MouseEvent e) {
        if (isExpert) {
            return;
        }

        isExpert = true;

        FillTransition fillTransition1 = new FillTransition(Duration.millis(200), expert);
        fillTransition1.setFromValue(Color.WHITE);
        fillTransition1.setToValue(highlightColor);


        FillTransition fillTransition2 = new FillTransition(Duration.millis(200), normal);
        fillTransition2.setFromValue(highlightColor);
        fillTransition2.setToValue(Color.WHITE);

        fillTransition1.play();
        fillTransition2.play();
    }
}
