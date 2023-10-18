package it.polimi.ingsw.client.view.GUI.ScreenLoaders;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIEnemySchoolBoardController;
import it.polimi.ingsw.model.Player.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.Objects;

public class EnemySchoolBoardLoader {
    private Pane playerPane;
    private FXMLLoader playerLoader;

    public Pane getSchoolBoardPane(Player player, Controller controller) {
        try {
            if (playerLoader == null || playerPane == null) {
                playerLoader = new FXMLLoader();
                playerLoader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI/EnemySchoolBoard.fxml")));
                playerPane = playerLoader.load();
            }

            GUIEnemySchoolBoardController guiController = playerLoader.getController();
            guiController.setController(controller);

            guiController.refresh(player);

            return playerPane;
        } catch(IOException e) {

        }

        return null;
    }
}
