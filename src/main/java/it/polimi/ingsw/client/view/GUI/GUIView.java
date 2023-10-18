package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIChooseAssistantsController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIEndGameController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIGameController;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIGamesController;
import it.polimi.ingsw.client.view.PromptMessages;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Messages.AvailableGames;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.model.Player.Player;

import java.util.HashMap;

/**
 * The graphical user interface view, implemented with JavaFX.
 * @author Francesco Barabino
 */
public class GUIView implements View {
    Screens screens;

    public void setScreens(Screens screens) {
        this.screens = screens;
    }

    @Override
    public void startScreen() {

    }

    @Override
    public void setIP() {

    }

    @Override
    synchronized public void startGame(Game game, Player player) {
        screens.switchTo("GameMap");
        GUIGameController controller = (GUIGameController) screens.getGUIController("GameMap");
        controller.init(game, player);
        controller.refresh(game, player);
    }

    @Override
    synchronized public void updateGame(Game game, Player player) {
        if (!screens.getCurrentScreen().equals("GameMap")) {
            startGame(game, player);
            return;
        }
        GUIGameController controller = (GUIGameController) screens.getGUIController("GameMap");
        controller.refresh(game, player);
    }

    @Override
    public void chooseGame(AvailableGames availableGames) {
        screens.switchTo("ChooseGame");
        GUIGamesController controller = (GUIGamesController) screens.getGUIController("ChooseGame");
        controller.setGames(availableGames);
    }

    @Override
    public void setGame() {
        screens.switchTo("NewGame");
    }

    @Override
    public void setNickname() {
        screens.switchTo("SetNickname");
    }

    @Override
    public void waitingPlayers() {
        screens.switchTo("WaitingPlayers");
    }

    @Override
    synchronized public void requestAssistant(Game game, Player player) {
        GUIGameController controller = (GUIGameController) screens.getGUIController("GameMap");
        controller.displayAssistants();
    }

    @Override
    public void dismissAssistants() {
        GUIGameController controller = (GUIGameController) screens.getGUIController("GameMap");
        controller.dismissAssistants();
    }

    @Override
    public void moveStudent(Game game, SchoolBoard schoolBoard) {
        ((GUIGameController) screens.getGUIController("GameMap")).canPlay = true;
        displayMessage(PromptMessages.moveStudent());
    }

    @Override
    public void moveMotherNature(Game game, Player player) {
        ((GUIGameController) screens.getGUIController("GameMap")).canPlay = true;
        displayMessage(PromptMessages.moveMotherNature());
    }

    @Override
    public void chooseCloud(Game game) {
        try {
            ((GUIGameController) screens.getGUIController("GameMap")).canPlay = true;
        } catch(NullPointerException ignored) {}

        displayMessage(PromptMessages.chooseCloud());
    }

    @Override
    public void interruptedGame() {
        screens.switchTo("WaitingPlayers");
    }

    @Override
    public void displayServerError() {
        screens.switchTo("ServerError");
    }

    @Override
    public void displayMessage(String message) {
        try {
            GUIGameController controller = (GUIGameController) screens.getGUIController("GameMap");
            controller.displayMessage(message);
        } catch(NullPointerException ignored) {}
    }

    @Override
    public void displayHelp() {

    }

    @Override
    public void displayWin() {
        screens.switchTo("EndGame");
        ((GUIEndGameController) screens.getGUIController("EndGame")).display(Message.Won);
    }

    @Override
    public void displayTie() {
        screens.switchTo("EndGame");
        ((GUIEndGameController) screens.getGUIController("EndGame")).display(Message.Tie);
    }

    @Override
    public void displayLost() {
        screens.switchTo("EndGame");
        ((GUIEndGameController) screens.getGUIController("EndGame")).display(Message.Lost);
    }
}
