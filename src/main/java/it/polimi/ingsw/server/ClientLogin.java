package it.polimi.ingsw.server;
import com.google.gson.Gson;
import it.polimi.ingsw.model.Messages.AvailableGame;
import it.polimi.ingsw.model.Messages.AvailableGames;
import it.polimi.ingsw.model.Messages.GameType;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.server.Exceptions.ClientNotFound;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Logs the client into available games, asking him the nickname as an identifier.
 *
 * If the client was playing a game and had logged out, he can get back into that game.
 * If there's a game which is waiting for players to begin, the client will be able to join that game.
 * The player can set up and start a new game.
 *
 * @author Francesco Barabino
 */
public class ClientLogin implements Runnable {
    private final Socket clientSocket;
    private final ServerGames serverGames;
    private String nickname;
    private ClientIO io;

    ClientLogin(Socket clientSocket, ServerGames serverGames) {
        this.clientSocket = clientSocket;
        this.serverGames = serverGames;
    }

    /**
     *
     * @return the games that this client can join.
     */
    private AvailableGames getAvailableGames() {
        final AvailableGames availableGames = new AvailableGames();

        for (ServerGame serverGame : serverGames.getGames()) {
            boolean gameAdded = false;

            if (!serverGame.isGameSet() || serverGame.isGameOver()) {
                continue;
            }

            int activePlayers = (int) serverGame.getClients().stream().filter(ServerClientHandler::isClientConnected).count();

            //The client rejoins a game he was playing
            for (ServerClientHandler client : serverGame.getClients()) {
                if (!client.isClientConnected() && nickname.equals(client.nickname)) {
                    availableGames.addGame(new AvailableGame(serverGame.getId(), activePlayers, serverGame.getGame().numberOfPlayers, GameType.Playing));
                    gameAdded = true;
                }
            }

            if (gameAdded) {
                continue;
            }

            //The client rejoins an interrupted game
            for (String gameNickname : serverGame.getNicknames()) {
                if (nickname.equals(gameNickname)) {
                    availableGames.addGame(new AvailableGame(serverGame.getId(), activePlayers, serverGame.getGame().numberOfPlayers, GameType.Interrupted));
                }
            }
            //The client is added to a lobby
            if (!serverGame.gameStarted()) {
                availableGames.addGame(new AvailableGame(serverGame.getId(), activePlayers, serverGame.getGame().numberOfPlayers, GameType.Lobby));
            }
        }

        return availableGames;
    }

    /**
     * Refreshes the AvailableGames for the client.
     */
    public void reloadAvailableGames() {
        if (nickname == null) {
            return;
        }
        try {
            io.write(Message.ChooseGame, new Gson().toJson(getAvailableGames()));
        } catch(ClientNotFound e) {
            serverGames.removePreGameNickname(nickname);
        }
    }

    /**
     * Asks the user to insert a nickname, then check if it is valid, if so it will send an OK message to the user, if not it will ask the nickname again.
     * @throws ClientNotFound If the client couldn't be reached
     */
    private void setNickname() throws ClientNotFound {
        io.write(Message.RequestNickname);

        while (true) {
            nickname = io.readLine();
            synchronized (serverGames) {
                if (serverGames.isNicknameUsed(nickname)) {
                    io.write(Message.KO);
                }
                else if (nickname.isEmpty()) {
                    io.write(Message.KO);
                }
                else if (nickname.length() > 30) {
                    io.write(Message.KO);
                }
                else {
                    io.write(Message.OK);
                    serverGames.addPreGameNickname(nickname, this);
                    break;
                }
            }
        }
    }

    /**
     * Sends the AvailableGames to the client, then accepting the client choice about the games to join.
     * The client can choose to:
     * make a new game,
     * join a game that was saved on disk,
     * join a game from which he was disconnected,
     * join a game that was made by another client and that is waiting for players.
     */
    public void sendAvailableGames() {
        try {
            io.write(Message.ChooseGame, new Gson().toJson(getAvailableGames()));

            while (true) {
                int gameID;
                try {
                    gameID = Integer.parseInt(io.readLine());
                } catch(NumberFormatException e) {
                    io.write(Message.KO, new Gson().toJson(getAvailableGames()));
                    continue;
                }

                if (gameID == -1) {
                    //The client starts a new game
                    serverGames.removePreGameNickname(nickname);
                    ServerGame newGame = new ServerGame(io, nickname, serverGames::refreshLobbies);
                    newGame.prepareGame();
                    serverGames.add(newGame);
                    return;
                }
                else {
                    final AvailableGames availableGames = getAvailableGames();

                    if (availableGames.get(gameID) == null) {
                        io.write(Message.KO, new Gson().toJson(availableGames));
                        continue;
                    }

                    if (availableGames.get(gameID).gameType == GameType.Playing) {
                        for (ServerClientHandler client : serverGames.get(gameID).getClients()) {
                            if (client.nickname.equals(nickname)) {
                                //The client joins a game from which he was disconnected
                                serverGames.removePreGameNickname(nickname);
                                client.setSocket(io);
                                return;
                            }
                        }
                    }
                    else if (availableGames.get(gameID).gameType == GameType.Interrupted) {
                        //The client joins a game that was interrupted and saved on disk
                        serverGames.removePreGameNickname(nickname);
                        serverGames.get(gameID).addClient(io, nickname);
                        return;
                    }
                    else if (availableGames.get(gameID).gameType == GameType.Lobby) {
                        //The client joins a lobby which is waiting for players
                        serverGames.removePreGameNickname(nickname);
                        serverGames.get(gameID).addClient(io, nickname);
                        return;
                    }
                }
            }
        } catch(ClientNotFound ignored) {}
    }

    /**
     * Rebuilds the ClientIO for the client.
     */
    public void refreshIO() {
        try {
            io = new ClientIO(clientSocket, this);
        } catch(IOException ignored) {}
    }

    /**
     * Thread to run as soon as the client connects with the server, a ClientIO is built for the client and the AvailableGames are sent to him.
     */
    @Override
    public void run() {
        try {
            io = new ClientIO(clientSocket, this);
            //Asks the nickname to the client
            setNickname();
        } catch(IOException | ClientNotFound e) {
            if (nickname != null) {
                serverGames.removePreGameNickname(nickname);
            }
            return;
        }

        sendAvailableGames();

        if (nickname != null) {
            serverGames.removePreGameNickname(nickname);
        }
    }
}
