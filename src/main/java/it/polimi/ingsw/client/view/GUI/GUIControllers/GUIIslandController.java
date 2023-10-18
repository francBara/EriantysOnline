package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.client.view.GUI.ScreenLoaders.SchoolBoardLoader;
import it.polimi.ingsw.client.view.PromptMessages;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Cards.Characters.CharacterRequirements.FactionRequirement;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.CharacterMessage;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.model.Students;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class GUIIslandController extends GUIController {
    @FXML
    private StackPane stackPane;

    @FXML
    private ImageView islandImage;

    @FXML
    private FlowPane pawns;

    @FXML
    private ImageView motherNature;

    private Island island;
    private Integer islandIndex;

    private GUIGameController gameController;
    private SchoolBoardLoader schoolBoardLoader;


    public void init(int islandIndex) {
        this.islandIndex = islandIndex;
        Platform.runLater(() -> {
            islandImage.setImage(new Image(String.valueOf(getClass().getClassLoader().getResource("Graphics/Islands/Isle" + (new Random().nextInt(3) + 1) + ".png"))));
        });
    }

    public void refresh(Island island, int islandIndex) {
        if (island.equals(this.island)) {
            this.island = island;
            this.islandIndex = islandIndex;
            return;
        }

        this.island = island;
        this.islandIndex = islandIndex;
        Platform.runLater(() -> {
            final Students islandStudents = island.getStudents();
            final int totalStudents = islandStudents.getTotalStudents();

            pawns.getChildren().clear();

            if (totalStudents < 10) {
                for (Faction faction : Faction.values()) {
                    for (int i = 0; i < island.getStudents().get(faction); i++) {
                        pawns.getChildren().add(getPawn(faction));
                    }
                }
            }
            else {
                for (Faction faction : island.getStudents().getFactions()) {
                    pawns.getChildren().add(getStudentsCircle(faction, island.getStudents().get(faction)));
                }
            }


            if (island.getTowersNumber() > 2) {
                pawns.getChildren().add(getTower(island.getTowersNumber()));
            }
            else {
                for (int i = 0; i < island.getTowersNumber(); i++) {
                    pawns.getChildren().add(getTower());
                }
            }
        });
    }

    private ImageView getTower() {
        ImageView tower = new ImageView(loadImage("Graphics/Pawns/Tower" + island.getTowersColor() + ".png"));
        tower.setFitHeight(30);
        tower.setFitWidth(30);
        tower.setPreserveRatio(true);
        return tower;
    }

    private StackPane getTower(int towersNumber) {
        StackPane towerStackPane = new StackPane();
        ImageView tower = new ImageView(loadImage("Graphics/Pawns/Tower" + island.getTowersColor() + ".png"));
        tower.setFitHeight(30);
        tower.setFitWidth(30);
        tower.setPreserveRatio(true);

        Text text = new Text(String.valueOf(towersNumber));
        text.setFont(Font.font(14));
        if (island.getTowersColor() == TowerColor.White) {
            text.setFill(Color.BLACK);
        }
        else {
            text.setFill(Color.WHITE);
        }

        towerStackPane.getChildren().add(tower);
        towerStackPane.getChildren().add(text);
        return towerStackPane;
    }

    private ImageView getPawn(Faction faction) {
        final ImageView studentImage = new ImageView(loadImage("Graphics/Pawns/" + faction.name() + "Stud3D.png"));
        studentImage.setFitHeight(15);
        studentImage.setFitWidth(15);
        return studentImage;
    }

    private StackPane getStudentsCircle(Faction faction, int studentsNumber) {
        StackPane stackPane = new StackPane();
        Circle circle = new Circle(8);

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

        Text studentsNumberText = new Text();

        studentsNumberText.setText(String.valueOf(studentsNumber));
        studentsNumberText.setTextAlignment(TextAlignment.CENTER);

        stackPane.getChildren().add(circle);
        stackPane.getChildren().add(studentsNumberText);
        return stackPane;
    }

    public void showMotherNature() {
        motherNature.setVisible(true);
    }

    public void removeMotherNature() {
        motherNature.setVisible(false);
    }

    public void setGUIControllers(GUIGameController gameController, SchoolBoardLoader schoolBoardLoader) {
        this.gameController = gameController;
        this.schoolBoardLoader = schoolBoardLoader;
    }

    @FXML
    private void onClicked(final MouseEvent e) {
        if (!gameController.canPlay) {
            return;
        }
        if ((controller.getLastMessage().isActionPhase()) && gameController.islandRequirementCharacterIndex != null) {
            FactionRequirement factionRequirement = controller.getGame().getCharacters().get(gameController.islandRequirementCharacterIndex).getFactionRequirement();
            controller.actionPerformed(new CharacterMessage(gameController.islandRequirementCharacterIndex, factionRequirement != null ? factionRequirement.getFaction() : null, islandIndex).toString());
            gameController.islandRequirementCharacterIndex = null;
            if (controller.getLastMessage() == Message.MoveStudent) {
                gameController.displayMessage(PromptMessages.moveStudent());
            }
            else if (controller.getLastMessage() == Message.MoveMotherNature) {
                gameController.displayMessage(PromptMessages.moveMotherNature());
            }
            else if (controller.getLastMessage() == Message.ChooseCloud) {
                gameController.displayMessage(PromptMessages.chooseCloud());
            }
        }
        else if (controller.getLastMessage() == Message.MoveMotherNature) {
            final int steps = controller.getGame().gameMap.getIslandNecessarySteps(islandIndex);
            if (steps <= controller.getPlayer().getMotherNatureValue()) {
                gameController.moveMotherNature(islandIndex);
                controller.getGame().gameMap.moveMotherNature(steps);
                controller.actionPerformed(String.valueOf(steps));
                gameController.canPlay = false;
            }
        }
        else if (controller.getLastMessage() == Message.MoveStudent) {
            final Faction chosenStudent = schoolBoardLoader.getController().getChosenStudent();
            final ImageView chosenStudentImage = schoolBoardLoader.getController().getChosenStudentImage();
            if (chosenStudent == null || chosenStudentImage == null) {
                return;
            }
            island.placeStudent(chosenStudent);
            chosenStudentImage.setManaged(false);
            chosenStudentImage.setVisible(false);
            if (island.getStudents().getTotalStudents() >= 10) {
                Island tmpIsland = island;
                island = null;
                refresh(tmpIsland, islandIndex);
            }
            else {
                pawns.getChildren().add(getPawn(chosenStudent));
            }
            controller.actionPerformed(chosenStudent + " " + islandIndex);
            schoolBoardLoader.getController().deleteChosenStudent();
            gameController.canPlay = false;
        }
        stackPane.setOpacity(1);
    }

    @FXML
    private void onMouseEntered(final MouseEvent e) {
        if (controller.getLastMessage() == Message.MoveMotherNature) {
            final int steps = controller.getGame().gameMap.getIslandNecessarySteps(islandIndex);
            if (steps <= controller.getPlayer().getMotherNatureValue() && steps != 0) {
                stackPane.setOpacity(0.7);
            }
        }
        else if ((controller.getLastMessage() == Message.MoveStudent && schoolBoardLoader.getController().getChosenStudent() != null) || (controller.getLastMessage().isActionPhase() && gameController.islandRequirementCharacterIndex != null)) {
            stackPane.setOpacity(0.7);
        }
    }

    @FXML
    private void onMouseExited(final MouseEvent e) {
        stackPane.setOpacity(1);
    }

    @FXML
    public void sardify() {
        Platform.runLater(() -> {
            islandImage.setImage(new Image(String.valueOf(getClass().getClassLoader().getResource("Graphics/Islands/sardinia.png"))));
            motherNature.setImage(new Image(String.valueOf(getClass().getClassLoader().getResource("Graphics/Pawns/MotherSardinia.png"))));
        });
    }
}
