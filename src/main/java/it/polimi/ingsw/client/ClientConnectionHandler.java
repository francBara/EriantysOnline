package it.polimi.ingsw.client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Game.GameSerializer;
import it.polimi.ingsw.model.Messages.AvailableGames;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.model.Player.Player;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * It handles the connection to the server by listening to messages and sending messages.
 * It interacts with the Controller.
 * @author Francesco Barabino
 */
public class ClientConnectionHandler {
    private Socket socket;
    private Scanner serverInput;

    private final Controller controller;

    public ClientConnectionHandler(Controller controller) {
        this.controller = controller;
    }

    /**
     * Attempts a connection to the server, if successful it listens to server input in a new thread, handling every new message and notifying the controller.
     * @param host the host to connect to
     * @param port the port to connect to
     */
    public void startConnection(String host, int port) {
        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                serverInput = new Scanner(socket.getInputStream());

                while (true) {
                    Message message;
                    try {
                        message = Message.valueOf(serverInput.nextLine());
                    } catch(IllegalArgumentException e) {
                        continue;
                    }

                    if (message == Message.Ping) {
                        continue;
                    }

                    if (message == Message.Synchronize) {
                        synchronize();
                    }
                    else if (message == Message.ChooseGame || (message == Message.KO && controller.getLastMessage() == Message.ChooseGame)) {
                        final String nextLine = serverInput.nextLine();
                        controller.parseAvailableGamesMessage(new Gson().fromJson(nextLine, AvailableGames.class));
                    }
                    else {
                        controller.parseMessage(message);
                    }
                }
            } catch (NoSuchElementException | IOException e) {
                controller.displayServerError();
            }
        }).start();
    }

    /**
     * Loads the updated game and player sent by the server.
     * @throws IOException
     */
    private void synchronize() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Gson gson = new Gson();

        controller.parseGameMessage(new GameSerializer().deserialize(serverInput.nextLine()), gson.fromJson(serverInput.nextLine(), Player.class));

        out.println(Message.OK);
    }

    /**
     * Sends a string command to the server via output stream.
     * @param command the command to be sent.
     */
    public void handle(String command) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(command);
        } catch(IOException ignored) {}
    }
}
