package it.polimi.ingsw.client.view;
import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Map.GameMap;
import it.polimi.ingsw.model.Messages.AvailableGames;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The view interface, implemented by CLIView and GUIView.
 * @author Francesco Barabino
 */
public interface View {
    /**
     * Displays the start screen for Eriantys.
     */
    void startScreen();

    void setIP();

    /**
     * Displays the game screen.
     * @param game the model Game instance.
     * @param player the model Player instance for this client.
     */
    void startGame(Game game, Player player);

    /**
     * Updates the Game screen basing on new model instances.
     * @param game the model Game instance.
     * @param player the model Player instance for this client.
     */
    void updateGame(Game game, Player player);

    /**
     * Displays a view for the client to set the game, by inserting the number of players and choosing the game mode.
     */
    void setGame();

    /**
     * Displays a view for the client to set the nickname.
     */
    void setNickname();

    /**
     * Displays a view for the client to choose which games to join, or to make a new one.
     * @param availableGames The IDs and the types of all available games for the client.
     */
    void chooseGame(AvailableGames availableGames);

    /**
     * Shows the user a loading view, while waiting players for the game.
     */
    void waitingPlayers();

    /**
     * Prompts the user to choose a cloud of the map.
     */
    void chooseCloud(Game game);


    /**
     * Prompts the user to choose an assistant.
     */
    void requestAssistant(Game game, Player player);

    /**
     * Removes the assistants choice screen from the view
     */
    void dismissAssistants();

    /**
     * Prompts the user to move a student. He can move from the entrance to the hall, or from the entrance to an island.
     * The client can choose to play a character instead of moving a student.
     */
    void moveStudent(Game game, SchoolBoard schoolBoard);

    /**
     * Prompts the user to move mother nature.
     * The client can choose to play a character instead of moving mother nature.
     */
    void moveMotherNature(Game game, Player player);

    /**
     * Notifies the client of game interruption.
     */
    void interruptedGame();

    /**
     * Shows the user an internal server error screen and blocks every server related process.
     */
    void displayServerError();

    /**
     * Displays an arbitrary message to the user.
     * @param message the message to display
     */
    void displayMessage(String message);

    /**
     * Displays instructions about the game.
     */
    void displayHelp();

    /**
     * Displays the victory screen.
     */
    void displayWin();

    /**
     * Displays the tie screen.
     */
    void displayTie();

    /**
     * Displays the loss screen.
     */
    void displayLost();
}
