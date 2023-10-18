package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.model.Messages.AvailableGame;
import it.polimi.ingsw.model.Messages.AvailableGames;
import it.polimi.ingsw.model.Messages.GameType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class GUIGamesController extends GUIController {
    @FXML
    private VBox gamesColumn;

    public void setGames(AvailableGames availableGames) {
        Platform.runLater(() -> {
            ArrayList<AvailableGame> games;

            gamesColumn.getChildren().clear();

            games = availableGames.getType(GameType.Playing);
            if (!games.isEmpty()) {
                gamesColumn.getChildren().add(getGamesLabel(GameType.Playing));
                for (AvailableGame availableGame : games) {
                    gamesColumn.getChildren().add(getGameButton(availableGame));
                }
            }

            games = availableGames.getType(GameType.Interrupted);
            if (!games.isEmpty()) {
                gamesColumn.getChildren().add(getGamesLabel(GameType.Interrupted));
                for (AvailableGame availableGame : games) {
                    gamesColumn.getChildren().add(getGameButton(availableGame));
                }
            }

            games = availableGames.getType(GameType.Lobby);
            if (!games.isEmpty()) {
                gamesColumn.getChildren().add(getGamesLabel(GameType.Lobby));
                for (AvailableGame availableGame : games) {
                    gamesColumn.getChildren().add(getGameButton(availableGame));
                }
            }

            if (gamesColumn.getChildren().isEmpty()) {
                Text text = new Text("No games available for now, make a new game!");
                text.setFont(new Font("Vivaldi Italic", 40));
                VBox.setMargin(text, new Insets(16, 0, 0, 16));
                gamesColumn.getChildren().add(text);
            }
        });
    }

    private StackPane getGameButton(AvailableGame availableGame) {
        StackPane stackPane = new StackPane();
        Rectangle rectangle = new Rectangle(1000, 60);
        rectangle.setArcHeight(20);
        rectangle.setArcWidth(20);

        Text text1 = new Text("Game id: " + availableGame.id);
        text1.setFont(new Font("Vivaldi Italic", 24));

        StackPane.setMargin(text1, new Insets(0, 0, 0, 56));
        StackPane.setAlignment(text1, Pos.CENTER_LEFT);

        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);

        stackPane.getChildren().add(rectangle);
        stackPane.getChildren().add(text1);

        if (availableGame.gameType != GameType.Playing) {
            Text text2 = new Text("Online players: " + availableGame.playersNumber + "/" + availableGame.maxPlayersNumber);
            text2.setFont(new Font("Vivaldi Italic",24));
            StackPane.setMargin(text2, new Insets(0, 56, 0, 0));
            StackPane.setAlignment(text2, Pos.CENTER_RIGHT);
            stackPane.getChildren().add(text2);
        }

        stackPane.setOnMouseClicked((MouseEvent e) -> {
            controller.actionPerformed(String.valueOf(availableGame.id));
        });
        stackPane.setOnMousePressed((MouseEvent e) -> {
            stackPane.setOpacity(0.4);
        });
        stackPane.setOnMouseReleased((MouseEvent e) -> {
            stackPane.setOpacity(1);
        });

        return stackPane;
    }

    private Text getGamesLabel(GameType gameType) {
        Text text = new Text();

        if (gameType == GameType.Lobby) {
            text.setText("Lobbies");
        }
        else if (gameType == GameType.Playing) {
            text.setText("Abandoned games");
        }
        else if (gameType == GameType.Interrupted) {
            text.setText("Interrupted games");
        }

        text.setFont(new Font("Vivaldi Italic", 36));

        VBox.setMargin(text, new Insets(0, 0, 0, 32));

        return text;
    }

    @FXML
    private void makeGame(final ActionEvent event) {
        controller.actionPerformed("-1");
    }
}
