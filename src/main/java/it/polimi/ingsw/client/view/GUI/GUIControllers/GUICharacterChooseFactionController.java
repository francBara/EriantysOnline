package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.model.Faction;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;


public class GUICharacterChooseFactionController extends GUIController {
    Consumer<Faction> onFactionChosen;
    Runnable onDismiss;

    @FXML
    private ImageView redDragons;

    @FXML
    private ImageView pinkFairies;

    @FXML
    private ImageView blueUnicorns;

    @FXML
    private ImageView greenFrogs;

    @FXML
    private ImageView yellowGnomes;

    public void setOnFactionChosen(Consumer<Faction> onFactionChosen) {
        this.onFactionChosen = onFactionChosen;
    }

    public void setOnDismiss(Runnable onDismiss) {
        this.onDismiss = onDismiss;
    }

    @FXML
    private void onCancel(MouseEvent mouseEvent) {
        onDismiss.run();
    }

    @FXML
    private void onRedDragonsClicked(MouseEvent mouseEvent) {
        onFactionChosen.accept(Faction.RedDragons);
    }

    @FXML
    private void onGreenFrogsClicked(MouseEvent mouseEvent) {
        onFactionChosen.accept(Faction.GreenFrogs);
    }

    @FXML
    private void onYellowGnomesClicked(MouseEvent mouseEvent) {
        onFactionChosen.accept(Faction.YellowGnomes);
    }

    @FXML
    private void onPinkFairiesClicked(MouseEvent mouseEvent) {
        onFactionChosen.accept(Faction.PinkFairies);
    }

    @FXML
    private void onBlueUnicornsClicked(MouseEvent mouseEvent) {
        onFactionChosen.accept(Faction.BlueUnicorns);
    }
}
