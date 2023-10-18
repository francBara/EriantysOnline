package it.polimi.ingsw.client.view.GUI.GUIControllers;

import it.polimi.ingsw.client.view.GUI.ScreenLoaders.ChooseFactionLoader;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.CharacterMessage;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.function.Consumer;

public class GUICharacterCardController extends GUIController {
    @FXML
    private ImageView characterImage;

    @FXML
    private Text characterName;

    @FXML
    private Text characterDescription;

    @FXML
    private FlowPane studentsFlowPane;

    @FXML
    private ImageView plusCoin;

    private GUIGameController gameController;
    private GUIChooseCharactersController charactersController;

    private CharacterCard character;
    private int characterIndex;

    private boolean canTakeStudent = false;

    public void update(CharacterCard character, int characterIndex) {
        this.character = character;
        this.characterIndex = characterIndex;
        characterImage.setImage(new Image(String.valueOf(getClass().getClassLoader().getResource("Graphics/Characters/" + character.id + ".jpg"))));
        characterName.setText(character.getName());
        characterDescription.setText(character.getDescription());
        studentsFlowPane.getChildren().clear();

        if (character.isUsed()) {
            plusCoin.setVisible(true);
        }
        if (character.getStudents() != null) {
            for (Faction faction : Faction.values()) {
                for (int i = 0; i < character.getStudents().get(faction); i++) {
                    studentsFlowPane.getChildren().add(getStudentPawn(faction));
                }
            }
        }
    }

    private ImageView getStudentPawn(Faction faction) {
        final ImageView pawn = new ImageView(String.valueOf(getClass().getClassLoader().getResource("Graphics/Pawns/" + faction + "Stud2D.png")));
        pawn.setFitHeight(40);
        pawn.setFitWidth(40);
        pawn.setOnMouseClicked((MouseEvent e) -> {
            if (canTakeStudent) {
                canTakeStudent = false;
                character.getFactionRequirement().setFaction(faction);
                if (character.requiresIsland()) {
                    gameController.islandRequirementCharacterIndex = characterIndex;
                    gameController.displayMessage("Choose an island for the character");
                }
                else {
                    controller.actionPerformed(new CharacterMessage(characterIndex, faction, null).toString());
                }
                gameController.dismissCharacters();
            }
        });
        pawn.setOnMouseEntered((MouseEvent e) -> {
            if (canTakeStudent) {
                pawn.setOpacity(0.7);
            }
        });
        pawn.setOnMouseExited((MouseEvent e) -> {
            pawn.setOpacity(1);
        });
        return pawn;
    }

    private boolean checkCharacter() {
        if (controller.getPlayer().getCoins() < character.getCost()) {
            charactersController.displayMessage("You don't have enough coins to play this character");
            return false;
        }
        else return controller.getLastMessage().isActionPhase();
    }

    @FXML
    private void onClicked(MouseEvent mouseEvent) {
        if (!checkCharacter() || canTakeStudent) {
            return;
        }
        if (character.getStudents() != null) {
            charactersController.displayMessage("Choose a student from the card");
            canTakeStudent = true;
            return;
        }

        if (character.requiresFaction() && character.getFactionRequirement().getFaction() == null) {
            gameController.dismissCharacters(character, characterIndex);
        }
        else {
            if (character.requiresIsland()) {
                gameController.islandRequirementCharacterIndex = characterIndex;
                gameController.displayMessage("Choose an island for the character");
            }
            else {
                controller.actionPerformed(new CharacterMessage(characterIndex, null, null).toString());
            }
            gameController.dismissCharacters();
        }
    }

    public void disableCanTakeStudent() {
        canTakeStudent = false;
    }

    public void setGUIControllers(GUIGameController gameController, GUIChooseCharactersController charactersController) {
        this.gameController = gameController;
        this.charactersController = charactersController;
    }
}
