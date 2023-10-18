package it.polimi.ingsw.client.view.GUI.GUIControllers;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Messages.Message;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GUIChooseCharactersController extends GUIController {
    @FXML
    private StackPane errorPrompt;

    @FXML
    private Text errorText;

    @FXML
    private HBox charactersRow;

    private GUIGameController gameController;

    private final ArrayList<FXMLLoader> loaders = new ArrayList<FXMLLoader>();

    private Region getPadding() {
        final Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public void refresh(ArrayList<CharacterCard> characters) {
        Platform.runLater(() -> {
            if (loaders.isEmpty()) {
                charactersRow.getChildren().clear();
                for (int i = 0; i < characters.size(); i++) {
                    charactersRow.getChildren().add(getPadding());

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/CharacterCard.fxml")));
                    loaders.add(loader);

                    try {
                        charactersRow.getChildren().add(loader.load());
                    } catch (IOException ignored) {}

                    GUICharacterCardController controller = loader.getController();
                    controller.setController(this.controller);
                    controller.setGUIControllers(gameController, this);

                    controller.update(characters.get(i), i);
                }
                charactersRow.getChildren().add(getPadding());
            }
            else {
                for (int i = 0; i < characters.size(); i++) {
                    GUICharacterCardController controller = loaders.get(i).getController();
                    controller.update(characters.get(i), i);
                }
            }
        });
    }

    public void displayMessage(String message) {
        new Thread(() -> {
            errorText.setText(message);
            TranslateTransition tt = new TranslateTransition(Duration.millis(100), errorPrompt);
            tt.setFromY(-66.4);
            tt.setToY(80);

            TranslateTransition tt1 = new TranslateTransition(Duration.millis(100), errorPrompt);
            tt1.setFromY(80);
            tt1.setToY(-66.4);

            new SequentialTransition(tt, new PauseTransition(Duration.millis(3000)), tt1).play();
        }).start();
    }

    public void setGameController(GUIGameController gameController) {
        this.gameController = gameController;
    }

    @FXML
    private void onDismiss(final MouseEvent event) {
        for (FXMLLoader loader : loaders) {
            ((GUICharacterCardController) loader.getController()).disableCanTakeStudent();
        }
        gameController.dismissCharacters();
    }
}
