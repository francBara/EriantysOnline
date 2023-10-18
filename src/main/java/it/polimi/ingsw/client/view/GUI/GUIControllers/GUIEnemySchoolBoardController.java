package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Player.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GUIEnemySchoolBoardController extends GUIController {
    @FXML
    private HBox factionsRow;

    @FXML
    private Text playerName;

    @FXML
    private Text towersNumber;

    @FXML
    private ImageView towerImage;

    @FXML
    private ImageView assistantImage;

    @FXML
    private ImageView coinImage;

    @FXML
    private Text coinsNumber;

    @FXML
    private HBox entrance;

    @FXML
    private Text turnValue;

    @FXML
    private Text motherNatureValue;

    public void refresh(Player player) {
        Platform.runLater(() -> {
            if (player.nickname.length() > 18) {
                playerName.setText(player.nickname.substring(0, 12) + "...");
            }
            else {
                playerName.setText(player.nickname);
            }
            towersNumber.setText("x" + player.getSchoolBoard().getTowers());
            if (player.getPlayedAssistant() != null) {
                assistantImage.setImage(loadImage("Graphics/Assistants/" + player.getPlayedAssistant().name + ".png"));
                turnValue.setText(String.valueOf(player.getPlayedAssistant().turnValue));
                motherNatureValue.setText(String.valueOf(player.getPlayedAssistant().motherNatureMovement));
            }

            towerImage.setImage(loadImage("Graphics/Pawns/Tower" + player.getSchoolBoard().getTowersColor() + ".png"));

            factionsRow.getChildren().clear();
            factionsRow.getChildren().add(getPadding());
            for (Faction faction : Faction.values()) {
                factionsRow.getChildren().add(getFactionCircle(faction, player.getSchoolBoard().getDiningTable(faction).getStudents(), player.getSchoolBoard().getDiningTable(faction).hasProfessor()));
                factionsRow.getChildren().add(getPadding());
            }

            entrance.getChildren().clear();
            entrance.getChildren().add(getPadding());
            for (Faction faction : Faction.values()) {
                for (int i = 0; i < player.getSchoolBoard().getEntrance().getStudents().get(faction); i++) {
                    ImageView pawn = new ImageView(loadImage("Graphics/Pawns/" + faction + "Stud2D.png"));
                    pawn.setFitHeight(25);
                    pawn.setFitWidth(25);
                    entrance.getChildren().add(pawn);
                    entrance.getChildren().add(getPadding());
                }
            }

            if (controller.getGame().isExpert) {
                coinImage.setVisible(true);
                coinsNumber.setText("x" + player.getCoins());
            }
        });
    }

    private Region getPadding() {
        final Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    private StackPane getFactionCircle(Faction faction, int studentsNumber, boolean hasProfessor) {
        StackPane stackPane = new StackPane();
        Circle circle = new Circle(15);

        if (faction == Faction.RedDragons) {
            circle.setFill(Color.RED);
        }
        else if (faction == Faction.BlueUnicorns) {
            circle.setFill(Color.BLUE);
        }
        else if (faction == Faction.GreenFrogs) {
            circle.setFill(Color.GREEN);
        }
        else if (faction == Faction.PinkFairies) {
            circle.setFill(Color.PINK);
        }
        else if (faction == Faction.YellowGnomes) {
            circle.setFill(Color.YELLOW);
        }

        if (hasProfessor) {
            circle.setOpacity(0.8);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(4);
        }
        else {
            circle.setOpacity(0.5);
        }


        Text studentsNumberText = new Text();

        studentsNumberText.setText(String.valueOf(studentsNumber));
        studentsNumberText.setTextAlignment(TextAlignment.CENTER);

        stackPane.getChildren().add(circle);
        stackPane.getChildren().add(studentsNumberText);
        return stackPane;
    }
}
