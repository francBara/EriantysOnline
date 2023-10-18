package it.polimi.ingsw.client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.view.PromptMessages;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Game.GameStatus;
import it.polimi.ingsw.model.Messages.AvailableGames;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.model.Player.Player;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

/**
 * The controller for the client, it handles communication between ConnectionHandler, model and view.
 * @author Francesco Barabino
 */
public class Controller {
    private ClientConnectionHandler connectionHandler;
    private View view;

    private AvailableGames availableGames;

    private Message lastMessage;

    private Player player;
    private Game game;


    public void setView(View view) {
        this.view = view;
    }

    public void setConnectionHandler(ClientConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    /**
     * Starts the connection via the ConnectionHandler, the ConnectionHandler needs to be set before calling the Connect method.
     */
    public void connect(String host, int port) {
        assert(connectionHandler != null);
        connectionHandler.startConnection(host, port);
    }

    /**
     * Takes proper action for each message sent by the server, by notifying the view.
     * @param message the message to parse.
     */
    public void parseMessage(Message message) {
        if (message == Message.RequestNickname || (message == Message.KO && lastMessage == Message.RequestNickname)) {
            view.setNickname();
        }
        else if (message == Message.SetGame || (message == Message.KO && lastMessage == Message.SetGame)) {
            view.setGame();
        }
        else if (message == Message.RequestUseAssistant || (message == Message.KO && lastMessage == Message.RequestUseAssistant)) {
            view.requestAssistant(game, player);
        }
        else if (message == Message.WaitingPlanningPhase) {
            view.displayMessage(PromptMessages.waitingPlanningPhase());
        }
        else if (message == Message.WaitingActionPhase) {
            view.displayMessage(PromptMessages.waitingActionPhase());
        }
        else if (message == Message.MoveStudent || (message == Message.KO && lastMessage == Message.MoveStudent)) {
            view.moveStudent(game, player.getSchoolBoard());
        }
        else if (message == Message.MoveMotherNature || (message == Message.KO && lastMessage == Message.MoveMotherNature)) {
            view.moveMotherNature(game, player);
        }
        else if (message == Message.ChooseCloud || (message == Message.KO && lastMessage == Message.ChooseCloud)) {
            view.chooseCloud(game);
        }
        else if (message == Message.InterruptedGame) {
            view.interruptedGame();
        }
        else if (message == Message.Won) {
            view.displayWin();
        }
        else if (message == Message.Tie) {
            view.displayTie();
        }
        else if (message == Message.Lost) {
            view.displayLost();
        }
        else if (message == Message.OK) {
            if (lastMessage == Message.SetGame) {
                view.waitingPlayers();
            }
            else if (lastMessage == Message.RequestUseAssistant) {
                view.dismissAssistants();
            }
        }
        if (message != Message.OK && message != Message.KO) {
            lastMessage = message;
        }
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    /**
     * Properly handles the available games sent by the server.
     * @param availableGames
     */
    public void parseAvailableGamesMessage(AvailableGames availableGames) {
        if (availableGames != null) {
            this.availableGames = availableGames;
        }
        view.chooseGame(this.availableGames);
        lastMessage =  Message.ChooseGame;
    }

    /**
     * Properly handles the updated model sent by the server.
     * @param game the new Game instance
     * @param player the new Player instance
     */
    public void parseGameMessage(Game game, Player player) {
        this.game = game;
        this.player = player;

        if (lastMessage == Message.SetGame || lastMessage == Message.ChooseGame) {
            view.startGame(game, player);
        }
        else {
            view.updateGame(game, player);
        }
    }

    /**
     * Notifies the view to display a server error
     */
    public void displayServerError() {
        view.displayServerError();
    }

    /**
     * Processes a client command by passing it to the ConnectionHandler.
     * @param command
     */
    public void actionPerformed(String command) {
        connectionHandler.handle(command);
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public AvailableGames getAvailableGames() {
        return availableGames;
    }
}
