package it.polimi.ingsw.client.view.GUI.GUIControllers;
import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.ScreenLoaders.*;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Map.Cloud;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.CharacterMessage;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;


public class GUIGameController extends GUIController {
    @FXML
    private GridPane gridPane;

    @FXML
    private Pane schoolBoardPane;

    @FXML
    private Rectangle character1;
    @FXML
    private Rectangle character2;
    @FXML
    private Rectangle character3;

    @FXML
    private Pane enemy1;
    @FXML
    private Pane enemy2;

    @FXML
    private Rectangle playedAssistant;

    @FXML
    private Pane playedAssistantPane;

    @FXML
    private VBox playerCoins;
    @FXML
    private Text coinsNumber;

    @FXML
    private Pane charactersScreen;

    @FXML
    private Pane assistantsScreen;

    @FXML
    private Pane chooseFactionScreen;

    @FXML
    private StackPane gameMessage;
    @FXML
    private Text messageText;

    @FXML
    private FlowPane cloud1Students;
    @FXML
    private FlowPane cloud2Students;
    @FXML
    private FlowPane cloud3Students;

    @FXML
    private StackPane cloud3;

    @FXML
    private AnchorPane menu;

    @FXML
    private ImageView background;

    @FXML
    private ImageView bottomOpening;
    @FXML
    private ImageView topLeftOpening;
    @FXML
    private ImageView topRightOpening;

    private final AssistantsScreenLoader assistantsScreenLoader = new AssistantsScreenLoader();
    
    private SchoolBoardLoader schoolBoardLoader;

    private final EnemySchoolBoardLoader enemySchoolBoardLoader1 = new EnemySchoolBoardLoader();
    private final EnemySchoolBoardLoader enemySchoolBoardLoader2 = new EnemySchoolBoardLoader();

    private final CharactersScreenLoader charactersScreenLoader = new CharactersScreenLoader();

    public Integer islandRequirementCharacterIndex;

    private final HashMap<Integer, IslandLoader> islands = new HashMap<>();

    public boolean canPlay = false;

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        schoolBoardLoader = new SchoolBoardLoader(controller, this);
    }

    synchronized public void init(Game game, Player player) {
        Platform.runLater(() -> {
            islands.clear();
            gridPane.getChildren().clear();

            if (game.isExpert) {
                playerCoins.setVisible(true);
                character1.setVisible(true);
                character2.setVisible(true);
                character3.setVisible(true);
            }
            if (game.numberOfPlayers == 3) {
                enemy2.setVisible(true);
                cloud3.setVisible(true);
            }

            for (Island island : game.gameMap.getIslands()) {
                islands.put(island.id, new IslandLoader(controller, this, schoolBoardLoader));
            }

            int j = 0;
            for (int i = 0; i < 12 && j < game.gameMap.getIslands().size(); ) {
                final StackPane islandStackPane = islands.get(game.gameMap.getIslands().get(j).id).getIsland(game.gameMap.getIslands().get(j), j);
                if (i >= 0 && i < 4) {
                    gridPane.add(islandStackPane, i + 1, 0);
                }
                else if (i == 4) {
                    gridPane.add(islandStackPane, 5, 1);
                }
                else if (i == 5) {
                    gridPane.add(islandStackPane, 5, 2);
                }
                else if (i >= 6 && i < 10) {
                    gridPane.add(islandStackPane, 10 - i, 3);
                }
                else if (i == 10) {
                    gridPane.add(islandStackPane, 0, 2);
                }
                else if (i == 11) {
                    gridPane.add(islandStackPane, 0, 1);
                }
                if (game.gameMap.getIslands().get(j).getTowersNumber() > 0) {
                    i += game.gameMap.getIslands().get(j).getTowersNumber();
                }
                else {
                    i += 1;
                }
                j++;
            }

            gridPane.getScene().addEventHandler(KeyEvent.KEY_PRESSED, t -> {
                if(t.getCode() == KeyCode.ESCAPE) {
                    if (menu.getOpacity() == 0) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), menu);
                        menu.setVisible(true);
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        ft.play();
                    }
                    else if (menu.getOpacity() == 1) {
                        FadeTransition ft = new FadeTransition(Duration.millis(200), menu);
                        ft.setFromValue(1);
                        ft.setToValue(0);
                        ft.setOnFinished((e) -> menu.setVisible(false));
                        ft.play();
                    }
                }
            });

            TranslateTransition t1 = new TranslateTransition(Duration.millis(500), topLeftOpening);
            FadeTransition f1 = new FadeTransition(Duration.millis(500), topLeftOpening);
            TranslateTransition t2 = new TranslateTransition(Duration.millis(500), topRightOpening);
            FadeTransition f2 = new FadeTransition(Duration.millis(500), topRightOpening);
            TranslateTransition t3 = new TranslateTransition(Duration.millis(500), bottomOpening);
            FadeTransition f3 = new FadeTransition(Duration.millis(500), bottomOpening);;

            final int offset = 300;

            t1.setToY(topLeftOpening.getTranslateY() - offset);
            t1.setToX(topLeftOpening.getTranslateX() - offset);
            t2.setToY(topRightOpening.getTranslateY() - offset);
            t2.setToX(topRightOpening.getTranslateX() + offset);
            t3.setToY(bottomOpening.getTranslateY() + offset);
            f1.setToValue(0);
            f1.setOnFinished((ActionEvent e) -> {
                topLeftOpening.setManaged(false);
                topLeftOpening.setVisible(false);
            });
            f2.setToValue(0);
            f2.setOnFinished((ActionEvent e) -> {
                topRightOpening.setManaged(false);
                topRightOpening.setVisible(false);
            });
            f3.setToValue(0);
            f3.setOnFinished((ActionEvent e) -> {
                bottomOpening.setManaged(false);
                bottomOpening.setVisible(false);
            });

            (new SequentialTransition(new PauseTransition(Duration.millis(1500)), new ParallelTransition(t1, t2, t3, f1, f2, f3))).play();
        });
    }

    synchronized public void refresh(Game game, Player player) {
        islandRequirementCharacterIndex = null;
        Platform.runLater(() -> {
            final ArrayList<Island> gameIslands = game.gameMap.getIslands();

            final HashSet<Integer> islandsToRemove = new HashSet<Integer>();

            for (int islandID : islands.keySet()) {
                if (gameIslands.stream().noneMatch((island) -> island.id == islandID)) {
                    islandsToRemove.add(islandID);
                }
            }

            for (Integer islandID : islandsToRemove) {
                islands.get(islandID).delete();
                islands.remove(islandID);
            }

            for (int i = 0; i < gameIslands.size(); i++) {
                final Island island = gameIslands.get(i);
                islands.get(island.id).getController().refresh(island, i);
                if (island.getIsMotherNaturePlaced()) {
                    islands.get(island.id).getController().showMotherNature();
                }
                else {
                    islands.get(island.id).getController().removeMotherNature();
                }
            }

            for (int i = 0; i < game.gameMap.getClouds().size(); i++) {
                updateCloud(i);
            }

            if (player.getPlayedAssistant() != null) {
                playedAssistant.setFill(new ImagePattern(new Image(String.valueOf(getClass().getClassLoader().getResource("Graphics/Assistants/" + player.getPlayedAssistant().name + ".png")))));

            }
            if (game.isExpert) {
                coinsNumber.setText("x" + player.getCoins());
            }

            if (game.isExpert) {
                character1.setFill(getCharacterImage(game.getCharacters().get(0).id));
                character2.setFill(getCharacterImage(game.getCharacters().get(1).id));
                character3.setFill(getCharacterImage(game.getCharacters().get(2).id));
            }

            schoolBoardPane.getChildren().clear();
            schoolBoardPane.getChildren().add(schoolBoardLoader.getSchoolBoardPane(player));

            List<Player> enemies = game.getPlayers().stream().filter((p) -> !p.nickname.equals(player.nickname)).collect(Collectors.toList());
            Pane pane = enemySchoolBoardLoader1.getSchoolBoardPane(enemies.get(0), controller);
            enemy1.getChildren().clear();
            enemy1.getChildren().add(pane);
            if (enemies.size() > 1) {
                pane = enemySchoolBoardLoader2.getSchoolBoardPane(enemies.get(1), controller);
                enemy2.getChildren().clear();
                enemy2.getChildren().add(pane);
            }
        });
    }

    private void updateCloud(int cloudIndex) {
        FlowPane flowPane;
        if (cloudIndex == 0) {
            flowPane = cloud1Students;
        }
        else if (cloudIndex == 1) {
            flowPane = cloud2Students;
        }
        else {
            flowPane = cloud3Students;
        }
        flowPane.getChildren().clear();
        for (Faction faction : Faction.values()) {
            for (int i = 0; i < controller.getGame().gameMap.getClouds().get(cloudIndex).peekStudents().get(faction); i++) {
                flowPane.getChildren().add(getPawn(faction, 20));
            }
        }
    }

    private ImagePattern getCharacterImage(int characterID) {
        return new ImagePattern(new Image(String.valueOf(getClass().getClassLoader().getResource("Graphics/Characters/" + characterID + ".jpg"))));
    }

    private ImageView getPawn(Faction faction, int size) {
        final ImageView studentImage = new ImageView(String.valueOf(getClass().getClassLoader().getResource("Graphics/Pawns/" + faction.name() + "Stud3D.png")));
        studentImage.setFitHeight(size);
        studentImage.setFitWidth(size);
        return studentImage;
    }

    @FXML
    private void onCloudClicked(final MouseEvent e) {
        if (!canPlay) {
            return;
        }
        final StackPane cloudStackPane = (StackPane) e.getSource();
        final int cloudIndex = cloudStackPane.getId().equals("cloud1") ? 0 : cloudStackPane.getId().equals("cloud2") ? 1 : 2;
        if (controller.getLastMessage() == Message.ChooseCloud) {
            final Students cloudStudents = controller.getGame().gameMap.getClouds().get(cloudIndex).takeStudents();
            GUISchoolBoardController schoolBoardController = schoolBoardLoader.getController();
            for (Faction faction : Faction.values()) {
                for (int i = 0; i < cloudStudents.get(faction); i++) {
                    if (schoolBoardController.getEntrance3().getChildren().size() < 3) {
                        schoolBoardController.getEntrance3().getChildren().add(getPawn(faction, 30));
                    }
                    else {
                        schoolBoardController.getEntrance5().getChildren().add(getPawn(faction, 30));
                    }
                }
            }
            ((FlowPane) cloudStackPane.getChildren().get(1)).getChildren().clear();
            controller.actionPerformed(String.valueOf(cloudIndex));
            canPlay = false;
        }
    }

    @FXML
    private void onCloudHover(final MouseEvent e) {
        if (controller.getLastMessage() == Message.ChooseCloud) {
            ((StackPane) e.getSource()).setOpacity(0.7);
        }
    }

    @FXML
    private void onCloudLeave(final MouseEvent e) {
        ((StackPane) e.getSource()).setOpacity(1);
    }

    synchronized public void moveMotherNature(int islandIndex) {
        islands.get(controller.getGame().gameMap.getMotherNatureIsland().id).getController().removeMotherNature();
        islands.get(controller.getGame().gameMap.getIslands().get(islandIndex).id).getController().showMotherNature();
    }

    public void dismissCharacters() {
        final FadeTransition ft = new FadeTransition(Duration.millis(300), charactersScreen);
        ft.setOnFinished(e1 -> charactersScreen.setVisible(false));
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();
    }

    public void dismissCharacters(CharacterCard character, int characterIndex) {
        final FadeTransition ft = new FadeTransition(Duration.millis(300), charactersScreen);
        ft.setOnFinished(e1 -> {
            charactersScreen.setVisible(false);
            displayChooseFaction(character, characterIndex);
        });
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();
    }

    public void displayChooseFaction(CharacterCard character, int characterIndex) {
        if (chooseFactionScreen.getChildren().size() == 0) {
            chooseFactionScreen.getChildren().add(new ChooseFactionLoader().getFactionsScreen(
                () -> {
                    final FadeTransition ft1 = new FadeTransition(Duration.millis(300), chooseFactionScreen);
                    ft1.setOnFinished(e1 -> chooseFactionScreen.setVisible(false));
                    ft1.setFromValue(1);
                    ft1.setToValue(0);
                    ft1.play();
                },
                (Faction chosenFaction) -> {
                    character.getFactionRequirement().setFaction(chosenFaction);
                    if (character.requiresIsland()) {
                        islandRequirementCharacterIndex = characterIndex;
                        displayMessage("Choose an island for the character");
                    }
                    else {
                        controller.actionPerformed(new CharacterMessage(characterIndex, chosenFaction, null).toString());
                    }
                    final FadeTransition ft1 = new FadeTransition(Duration.millis(300), chooseFactionScreen);
                    ft1.setOnFinished(e1 -> chooseFactionScreen.setVisible(false));
                    ft1.setFromValue(1);
                    ft1.setToValue(0);
                    ft1.play();
                }
            ));
        }
        final FadeTransition ft1 = new FadeTransition(Duration.millis(300), chooseFactionScreen);
        chooseFactionScreen.setVisible(true);
        ft1.setFromValue(0);
        ft1.setToValue(1);
        ft1.play();
    }

    @FXML
    private void displayCharacters(ActionEvent actionEvent) {
        if (!controller.getGame().isExpert) {
            return;
        }
        if (charactersScreen.getChildren().size() == 0) {
            charactersScreen.getChildren().add(charactersScreenLoader.getCharactersScreen(controller, this));
        }
        charactersScreenLoader.getController().refresh(controller.getGame().getCharacters());
        FadeTransition ft = new FadeTransition(Duration.millis(300), charactersScreen);
        charactersScreen.setVisible(true);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
    
    @FXML
    private void onCharactersPressed(MouseEvent actionEvent) {
        character1.setOpacity(0.9);
        character2.setOpacity(0.9);
        character3.setOpacity(0.9);
    }

    @FXML
    private void onCharactersReleased(MouseEvent actionEvent) {
        character1.setOpacity(1);
        character2.setOpacity(1);
        character3.setOpacity(1);
    }

    public void displayAssistants() {
        Platform.runLater(() -> {
            if (assistantsScreen.getChildren().size() == 0) {
                assistantsScreen.getChildren().add(assistantsScreenLoader.getAssistantsScreen(controller.getPlayer().getAssistantsOwned(), controller, () -> {
                    FadeTransition ft = new FadeTransition(Duration.millis(300), assistantsScreen);
                    ft.setOnFinished(e1 -> assistantsScreen.setVisible(false));
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.play();
                }));
            }
            else {
                assistantsScreenLoader.getController().showAssistants(controller.getPlayer().getAssistantsOwned());
            }

            if (charactersScreen.isVisible()) {
                FadeTransition ft = new FadeTransition(Duration.millis(300), charactersScreen);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.setOnFinished((ActionEvent e) -> {
                    charactersScreen.setVisible(false);
                    FadeTransition ft1 = new FadeTransition(Duration.millis(300), assistantsScreen);
                    assistantsScreen.setVisible(true);
                    ft1.setFromValue(0);
                    ft1.setToValue(1);
                    ft1.play();
                });
                ft.play();
            }
            else {
                FadeTransition ft = new FadeTransition(Duration.millis(300), assistantsScreen);
                assistantsScreen.setVisible(true);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }
        });
    }

    public void dismissAssistants() {
        Platform.runLater(() -> {
            FadeTransition ft = new FadeTransition(Duration.millis(300), assistantsScreen);
            ft.setOnFinished(e1 -> assistantsScreen.setVisible(false));
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
        });
    }

    public void displayMessage(String message) {
        Platform.runLater(() -> {
            if (message.equals(messageText.getText())) {
                return;
            }

            if (!messageText.getText().isEmpty()) {
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), gameMessage);
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), gameMessage);

                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);

                translateTransition.setFromX(290);
                translateTransition.setToX(0);

                translateTransition.setOnFinished((ActionEvent e) -> {
                    this.messageText.setText(message);

                    TranslateTransition translateTransition1 = new TranslateTransition(Duration.millis(200), gameMessage);
                    FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(200), gameMessage);

                    fadeTransition1.setFromValue(0);
                    fadeTransition1.setToValue(1);

                    translateTransition1.setFromX(0);
                    translateTransition1.setToX(290);

                    fadeTransition1.play();
                    translateTransition1.play();
                });

                translateTransition.play();
                return;
            }

            this.messageText.setText(message);

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(150), gameMessage);
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), gameMessage);

            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);

            translateTransition.setFromX(0);
            translateTransition.setToX(290);

            fadeTransition.play();
            translateTransition.play();
        });
    }

    @FXML
    private void peekCharacters() {
        final Duration duration = Duration.millis(80);
        final int xOffset = -20;
        final int yOffset = -20;

        TranslateTransition t1 = new TranslateTransition(duration, character1);
        TranslateTransition t2 = new TranslateTransition(duration, character2);
        TranslateTransition t3 = new TranslateTransition(duration, character3);

        t1.setToX(xOffset);
        t1.setToY(yOffset);
        
        t2.setToX(xOffset);
        t2.setToY(yOffset);
        
        t3.setToX(xOffset);
        t3.setToY(yOffset);
        
        t1.play();
        t2.play();
        t3.play();
    }

    @FXML
    private void hideCharacters() {
        final Duration duration = Duration.millis(80);

        TranslateTransition t1 = new TranslateTransition(duration, character1);
        TranslateTransition t2 = new TranslateTransition(duration, character2);
        TranslateTransition t3 = new TranslateTransition(duration, character3);

        t1.setToX(0);
        t1.setToY(0);

        t2.setToX(0);
        t2.setToY(0);

        t3.setToX(0);
        t3.setToY(0);

        t1.play();
        t2.play();
        t3.play();
    }

    @FXML
    private void onMenuQuit(MouseEvent e) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), menu);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished((event) -> menu.setVisible(false));
        ft.play();
    }

    @FXML
    private void onQuit(MouseEvent e) {
        controller.actionPerformed("quit");
    }

    @FXML
    private void sardify(MouseEvent e) {
        for (IslandLoader islandLoader : islands.values()) {
            islandLoader.getController().sardify();
        }
        for (TowerColor towerColor : TowerColor.values()) {
            updateImage("Graphics/Pawns/Nuraghe" + towerColor + ".png", "Graphics/Pawns/Tower" + towerColor + ".png");
        }
        for (Faction faction : Faction.values()) {
            updateImage("Graphics/Pawns/" + faction + "StudSard.png", "Graphics/Pawns/" + faction + "Stud3D.png");
        }
        final Image bulletImage = new Image(String.valueOf(getClass().getClassLoader().getResource("Graphics/Others/signbullet.png")));
        ImageView bullet;
        Random random = new Random();
        playedAssistantPane.getChildren().clear();
        for (int i = 0; i < 7; i++) {
            bullet = new ImageView(bulletImage);
            bullet.setLayoutX(random.nextInt(150));
            bullet.setLayoutY(random.nextInt(240));
            bullet.setFitHeight(40 - random.nextInt(2));
            bullet.setFitWidth(40 - random.nextInt(2));
            playedAssistantPane.getChildren().add(bullet);
        }
        FadeTransition ft = new FadeTransition(Duration.millis(200), menu);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished((event) -> menu.setVisible(false));
        ft.play();
     }
}
