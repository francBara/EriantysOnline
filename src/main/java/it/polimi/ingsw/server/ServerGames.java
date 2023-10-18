package it.polimi.ingsw.server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * ADT that contains all ServerGame instances.
 */
public class ServerGames {
    private final HashMap<Integer, ServerGame> serverGames = new HashMap<Integer, ServerGame>();
    private transient HashMap<String, ClientLogin> preGameClients = new HashMap<String, ClientLogin>();

    /**
     * Builds the ServerGames attempting to load data from disk
     */
    public ServerGames() {
        Gson gson = new Gson();
        try {
            ArrayList<Integer> gameIDs = gson.fromJson(new String(Files.readAllBytes(Paths.get("." + File.separator + "state" + File.separator + "game_ids.json"))), new TypeToken<ArrayList<Integer>>(){}.getType());
            for (int id : gameIDs) {
                try {
                    ServerGame loadedGame = gson.fromJson(new String(Files.readAllBytes(Paths.get("." + File.separator + "state" + File.separator + id + "_state.json"))), ServerGame.class);
                    loadedGame.getGame().buildCharacters();
                    if (loadedGame.gameStarted()) {
                        loadedGame.resumeGame();
                    }
                    serverGames.put(id, loadedGame);
                } catch(IOException ignored) {}
            }
        } catch(IOException ignored) {}
        saveGameIDs();
        removeFinishedGames();
    }

    /**
     * @return all the active ServerGame instances
     */
    public Collection<ServerGame> getGames() {
        return serverGames.values();
    }

    /**
     *
     * @param gameId
     * @return the ServerGame with gameId as id
     */
    public ServerGame get(int gameId) {
        return serverGames.get(gameId);
    }

    /**
     * Adds a ServerGame to the ADT.
     * @param serverGame
     */
    synchronized public void add(ServerGame serverGame) {
        int maxId = 0;

        for (Integer id : serverGames.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }

        serverGames.put(maxId + 1, serverGame);
        serverGame.setId(maxId + 1);
        saveGameIDs();
    }

    /**
     * Cleans the ServerGames and the disk from finished and unset games.
     */
    private void removeFinishedGames() {
        ArrayList<Integer> gamesToDelete = new ArrayList<Integer>();

        for (Integer gameID : serverGames.keySet()) {
            if (serverGames.get(gameID).isGameOver()) {
                gamesToDelete.add(gameID);
            }
            else if (!serverGames.get(gameID).isGameSet() && serverGames.get(gameID).getClients().stream().noneMatch((ServerClientHandler::isClientConnected))) {
                gamesToDelete.add(gameID);
            }
        }

        for (Integer id : gamesToDelete) {
            serverGames.remove(id);
            try {
                File file = new File("." + File.separator + "state" + File.separator + id + "_state.json");
                file.delete();
            } catch (Exception ignored) {}
        }
    }

    /**
     *
     * @param nickname
     * @return true if the nickname is already used in the current session, both from a client who is playing a game or from a client who still has to enter one
     */
    synchronized public boolean isNicknameUsed(String nickname) {
        if (preGameClients.containsKey(nickname)) {
            return true;
        }
        for (ServerGame game : serverGames.values()) {
            if (game.getClients().stream().anyMatch(client -> client.nickname.equals(nickname) && client.isClientConnected())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resends the AvailableGames to all clients who are waiting to enter one game.
     */
    public void refreshLobbies() {
        for (ClientLogin login : preGameClients.values()) {
            login.reloadAvailableGames();
        }
    }

    /**
     * Adds a nickname to the collection of users who are waiting to enter a game.
     * @param nickname
     */
    synchronized public void addPreGameNickname(String nickname, ClientLogin client) {
        preGameClients.put(nickname, client);
    }

    /**
     * Removes a nickname from the collection of users who are waiting to enter a game.
     * @param nickname
     */
    synchronized public void removePreGameNickname(String nickname) {
        preGameClients.remove(nickname);
    }

    /**
     * Saves the ids of the ongoing games on disk.
     */
    synchronized private void saveGameIDs() {
        try {
            File stateDirectory = new File("." + File.separator + "state");

            if (!(stateDirectory).exists()) {
                stateDirectory.mkdir();
            }
            FileWriter fileWriter = new FileWriter("." + File.separator + "state" + File.separator + "game_ids.json");
            Gson gson = new Gson();
            fileWriter.write(gson.toJson(serverGames.keySet()));
            fileWriter.flush();
            fileWriter.close();
        } catch(IOException ignored) {}
    }
}
