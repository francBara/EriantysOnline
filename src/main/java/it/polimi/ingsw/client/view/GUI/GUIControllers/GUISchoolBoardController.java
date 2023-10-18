package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Messages.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class GUISchoolBoardController extends GUIController {
    @FXML
    private VBox entrance3;

    @FXML
    private VBox entrance5;

    @FXML
    private HBox redDiningTable;
    @FXML
    private HBox blueDiningTable;
    @FXML
    private HBox pinkDiningTable;
    @FXML
    private HBox greenDiningTable;
    @FXML
    private HBox yellowDiningTable;

    @FXML
    private GridPane professorsTable;

    @FXML
    private GridPane towersGrid;

    private Faction chosenStudent;
    private ImageView chosenStudentImage;

    private GUIGameController gameController;

    public void refresh(SchoolBoard schoolBoard, boolean isClickable) {
        Platform.runLater(() -> {
            ArrayList<Faction> entranceStudents = schoolBoard.getEntrance().getStudents().toArray();

            entrance3.getChildren().clear();
            entrance5.getChildren().clear();

            for (int i = 0; i < 4 && i < entranceStudents.size(); i++) {
                entrance3.getChildren().add(getPawn(entranceStudents.get(i), isClickable));
            }
            for (int i = 4; i < 9 && i < entranceStudents.size(); i++) {
                entrance5.getChildren().add(getPawn(entranceStudents.get(i), isClickable));
            }

            for (Faction faction : Faction.values()) {
                if (faction == Faction.RedDragons) {
                    redDiningTable.getChildren().clear();
                    for (int i = 0; i < schoolBoard.getDiningTable(faction).getStudents(); i++) {
                        redDiningTable.getChildren().add(getPawn(faction, false));
                    }
                }
                else if (faction == Faction.BlueUnicorns) {
                    blueDiningTable.getChildren().clear();
                    for (int i = 0; i < schoolBoard.getDiningTable(faction).getStudents(); i++) {
                        blueDiningTable.getChildren().add(getPawn(faction, false));
                    }
                }
                else if (faction == Faction.PinkFairies) {
                    pinkDiningTable.getChildren().clear();
                    for (int i = 0; i < schoolBoard.getDiningTable(faction).getStudents(); i++) {
                        pinkDiningTable.getChildren().add(getPawn(faction, false));
                    }
                }
                else if (faction == Faction.GreenFrogs) {
                    greenDiningTable.getChildren().clear();
                    for (int i = 0; i < schoolBoard.getDiningTable(faction).getStudents(); i++) {
                        greenDiningTable.getChildren().add(getPawn(faction, false));
                    }
                }
                else if (faction == Faction.YellowGnomes) {
                    yellowDiningTable.getChildren().clear();
                    for (int i = 0; i < schoolBoard.getDiningTable(faction).getStudents(); i++) {
                        yellowDiningTable.getChildren().add(getPawn(faction, false));
                    }
                }
            }

            professorsTable.getChildren().clear();

            for (Faction professor : schoolBoard.getProfessors()) {
                if (professor == Faction.RedDragons) {
                    professorsTable.add(getProfessor(professor), 0, 1);
                }
                else if (professor == Faction.BlueUnicorns) {
                    professorsTable.add(getProfessor(professor), 0, 4);
                }
                else if (professor == Faction.YellowGnomes) {
                    professorsTable.add(getProfessor(professor), 0, 2);
                }
                else if (professor == Faction.GreenFrogs) {
                    professorsTable.add(getProfessor(professor), 0, 0);
                }
                else if (professor == Faction.PinkFairies) {
                    professorsTable.add(getProfessor(professor), 0, 3);
                }
            }
            towersGrid.getChildren().clear();
            for (int i = 0; i < schoolBoard.getTowers(); i++) {
                towersGrid.add(getTower(schoolBoard.getTowersColor()), i % 2 == 0 ? 0 : 1, i / 2);
            }
        });
    }

    private ImageView getPawn(Faction faction, boolean isClickable) {
        final ImageView studentImage = new ImageView(loadImage("Graphics/Pawns/" + faction.name() + "Stud3D.png"));
        studentImage.setFitHeight(25);
        studentImage.setFitWidth(25);
        if (isClickable) {
            studentImage.setOnMouseClicked((MouseEvent e) -> {
                synchronized (studentImage) {
                    if (controller.getLastMessage() == Message.MoveStudent) {
                        if (chosenStudentImage != null) {
                            chosenStudentImage.setOpacity(1);
                            if (chosenStudentImage == studentImage) {
                                chosenStudent = null;
                                chosenStudentImage = null;
                                return;
                            }
                        }
                        chosenStudent = faction;
                        chosenStudentImage = studentImage;
                        chosenStudentImage.setOpacity(0.5);
                    }
                }
            });
            studentImage.setOnMouseEntered((MouseEvent e) -> {
                synchronized (studentImage) {
                    if (controller.getLastMessage() == Message.MoveStudent && chosenStudentImage != studentImage) {
                        studentImage.setOpacity(0.7);
                    }
                }
            });
            studentImage.setOnMouseExited((MouseEvent e) -> {
                synchronized (studentImage) {
                    if (controller.getLastMessage() == Message.MoveStudent && chosenStudentImage != studentImage) {
                        studentImage.setOpacity(1);
                    }
                }
            });
        }
        return studentImage;
    }

    @FXML
    private void onDiningRoomClicked(final ActionEvent event) {
        if (chosenStudent == null || chosenStudentImage == null || controller.getPlayer().getSchoolBoard().getDiningTable(chosenStudent).isFull() || !gameController.canPlay) {
            return;
        }

        chosenStudentImage.setManaged(false);
        chosenStudentImage.setVisible(false);

        if (chosenStudent == Faction.RedDragons) {
            redDiningTable.getChildren().add(getPawn(chosenStudent, false));
        }
        else if (chosenStudent == Faction.BlueUnicorns) {
            blueDiningTable.getChildren().add(getPawn(chosenStudent, false));
        }
        else if (chosenStudent == Faction.YellowGnomes) {
            yellowDiningTable.getChildren().add(getPawn(chosenStudent, false));
        }
        else if (chosenStudent == Faction.GreenFrogs) {
            greenDiningTable.getChildren().add(getPawn(chosenStudent, false));
        }
        else if (chosenStudent == Faction.PinkFairies) {
            pinkDiningTable.getChildren().add(getPawn(chosenStudent, false));
        }
        controller.actionPerformed(chosenStudent.name());
        chosenStudent = null;
        chosenStudentImage = null;
        gameController.canPlay = false;
    }

    private ImageView getProfessor(Faction faction) {
        final ImageView professorImage = new ImageView(loadImage("Graphics/Pawns/" + faction.name() + "Prof3D.png"));
        professorImage.setFitHeight(30);
        professorImage.setFitWidth(30);
        professorImage.setTranslateX(7);
        return professorImage;
    }

    private ImageView getTower(TowerColor towerColor) {
        final ImageView towerImage = new ImageView(loadImage("Graphics/Pawns/Tower" + towerColor + ".png"));
        towerImage.setFitHeight(40);
        towerImage.setFitWidth(40);
        towerImage.setPreserveRatio(true);
        towerImage.setTranslateX(11);
        towerImage.setTranslateY(-4);
        return towerImage;
    }

    public VBox getEntrance3() {
        return entrance3;
    }

    public VBox getEntrance5() {
        return entrance5;
    }

    public Faction getChosenStudent() {
        return chosenStudent;
    }

    public ImageView getChosenStudentImage() {
        return chosenStudentImage;
    }

    public void deleteChosenStudent() {
        chosenStudent = null;
        chosenStudentImage = null;
    }

    public void setGameController(GUIGameController gameController) {
        this.gameController = gameController;
    }
}
