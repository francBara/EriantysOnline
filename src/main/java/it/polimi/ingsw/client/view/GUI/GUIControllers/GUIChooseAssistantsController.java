package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GUIChooseAssistantsController extends GUIController {
    @FXML
    private FlowPane assistantsPane;

    private Runnable onAssistantChosen;

    public void setOnAssistantChosen(Runnable onAssistantChosen) {
        this.onAssistantChosen = onAssistantChosen;
    }

    public void showAssistants(ArrayList<Assistant> assistants) {
        final Set<Assistant> usedAssistants = controller.getGame().getUsedAssistants();
        final boolean allUsed = controller.getGame().canUseAnyAssistant(assistants);

        Platform.runLater(() -> {
            assistantsPane.getChildren().clear();
            for (int i = 0; i < controller.getPlayer().getAssistantsOwned().size(); i++) {
                int finalI = i;
                assistantsPane.getChildren().add(getAssistantCard(assistants.get(i), i, allUsed || (usedAssistants.stream().noneMatch((assistant -> assistant.turnValue == assistants.get(finalI).turnValue)))));
            }
        });
    }

    private Pane getAssistantCard(Assistant assistant, int index, boolean isAvailable) {
        final ImageView card = new ImageView(String.valueOf(getClass().getClassLoader().getResource("Graphics/Assistants/" + assistant.name + ".png")));
        card.setFitHeight(260);
        card.setFitWidth(176.8);

        final Pane pane = new Pane();

        if (isAvailable) {
            pane.setOnMouseClicked((MouseEvent e) -> {
                controller.actionPerformed(String.valueOf(index));
                //onAssistantChosen.run();
            });

            pane.setOnMousePressed((MouseEvent e) -> {
                card.setOpacity(0.8);
            });
            pane.setOnMouseReleased((MouseEvent e) -> {
                card.setOpacity(1);
            });
            pane.setOnMouseEntered((MouseEvent e) -> {
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), card);
                translateTransition.setFromY(0);
                translateTransition.setToY(-16);
                translateTransition.play();
            });
            pane.setOnMouseExited((MouseEvent e) -> {
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), card);
                translateTransition.setFromY(-16);
                translateTransition.setToY(0);
                translateTransition.play();
            });
        }
        else {
            pane.setOpacity(0.6);
        }

        pane.getChildren().add(card);
        return pane;
    }
}
