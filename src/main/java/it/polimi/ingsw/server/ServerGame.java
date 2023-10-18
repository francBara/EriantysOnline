package it.polimi.ingsw.server;
import com.google.gson.Gson;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Game.GameStatus;
import it.polimi.ingsw.model.Game.GameSerializer;
import it.polimi.ingsw.model.Game.VictoryType;
import it.polimi.ingsw.model.Map.Cloud;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;
import it.polimi.ingsw.server.Exceptions.ClientNotFound;
import it.polimi.ingsw.server.Exceptions.GameOver;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Class that manages the execution of a single game, handling communication with multiple clients, process of game phases and turns.
 */
public class ServerGame {
    private transient ServerHostHandler host;
    private transient HashMap<String, ServerClientHandler> clients = new HashMap<String, ServerClientHandler>();
    private final HashSet<String> nicknames = new HashSet<String>();
    private Game game;
    private int id;

    private transient Runnable onGameSet;

    //Utilities to resume an interrupted game
    //The current phase of the game
    private GameStatus gameStatus;
    //The nickname of the client who is currently playing
    private String currentlyPlaying;
    //The index of the client who plays first in the current turn
    private int firstPlayerIndex;

    private boolean wasCharacterUsed = false;

    private boolean gameSet = false;
    private boolean gameStarted = false;
    private boolean gameOver = false;

    /**
     *
     * @param io The ClientIO of the client who created the game
     * @param hostNickname The nickname of the client who created the game
     * @param onGameSet Function to execute when all game settings (number of players and expert mode) are set, useful to refresh lobbies for all players who are looking for a game to join
     */
    public ServerGame(ClientIO io, String hostNickname, Runnable onGameSet) {
        this.host = new ServerHostHandler(io, hostNickname, this);
        this.onGameSet = onGameSet;
        clients.put(hostNickname, host);
        nicknames.add(hostNickname);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Adds a client to the game, useful when a game is starting and waiting for all players to join, both for a fresh game and for a disk saved game.
     * @param io the ClientIO of the new player
     * @param nickname the nickname of the new player
     */
    public synchronized void addClient(ClientIO io, String nickname) {
        ServerClientHandler client = new ServerClientHandler(io, nickname, this);
        for (Player player : game.getPlayers()) {
            if (nickname.equals(player.nickname)) {
                client.setPlayer(player);
                break;
            }
        }
        clients.put(nickname, client);
        nicknames.add(nickname);

        if (gameStarted) {
            if (clients.size() == game.numberOfPlayers) {
                client.synchronize(false);
            }
            else {
                client.awaitOtherPlayers();
            }
        }
        else if (clients.size() != game.numberOfPlayers) {
            client.awaitOtherPlayers();
        }
        notifyAll();
    }

    /**
     * Asks the first player to set the game, choosing the number of players and the game mode, then awaits for all the necessary players to join and starts the game.
     */
    public void prepareGame() {
        assert(!gameStarted && !gameSet);
        new Thread(() -> {
            try {
                int playersNumber;
                boolean isExpertGameMode;
                try {
                    String[] gameSettings = host.requestGameSettings();
                    playersNumber = Integer.parseInt(gameSettings[0]);
                    isExpertGameMode = Boolean.parseBoolean(gameSettings[1]);
                } catch(ClientNotFound e) {
                    setGameOver();
                    return;
                }

                game = new Game(playersNumber, isExpertGameMode);

                gameSet = true;

                onGameSet.run();

                //Waits for players
                synchronized(this) {
                    try {
                        while (clients.size() < playersNumber) {
                            wait();
                        }
                    } catch(InterruptedException ignored) {}
                }

                for (ServerClientHandler client : clients.values()) {
                    Player player = new Player(client.nickname);
                    client.setPlayer(player);
                    game.addPlayer(player);
                }

                game.initGame();
                gameStarted = true;
                startGame();
            } catch(IOException ignored) {}
        }).start();
    }

    /**
     * Resumes a game which was previously set, awaiting all necessary players to join before starting.
     */
    public void resumeGame() {
        clients = new HashMap<String, ServerClientHandler>();
        new Thread(() -> {
            synchronized(this) {
                try {
                    while (clients.size() < game.numberOfPlayers) {
                        wait();
                    }
                } catch(InterruptedException ignored) {}
            }
            startGame();
        }).start();
    }

    /**
     * Starts and plays the actual game.
     */
    private void startGame() {
        assert(gameStarted);
        synchronizePlayers();
        saveState();

        //Choose the first player to play randomly
        if (gameStatus == null) {
            gameStatus = GameStatus.PlanningPhase;
            firstPlayerIndex = new Random().nextInt(clients.size());
        }

        while (true) {
            /*
            Planning phase.
            Players choose the assistants to play to decide the turns order in the action phase.
             */
            if (gameStatus == GameStatus.PlanningPhase) {
                int startingIndex = firstPlayerIndex;
                //If the game was interrupted during planning phase, the player who was playing before will continue playing.
                if (currentlyPlaying != null) {
                    for (int i = 0; i < game.getPlayers().size(); i++) {
                        if (game.getPlayers().get(i).nickname.equals(currentlyPlaying)) {
                            startingIndex = i;
                            break;
                        }
                    }
                }

                //Communicates to non-playing clients that one player is choosing the assistant
                for (int i = 0; i < clients.size(); i++) {
                    if (i != startingIndex) {
                        clients.get(game.getPlayers().get(i).nickname).sendWaitingPlanningPhase();
                    }
                }

                if (startingIndex < firstPlayerIndex) {
                    for (int i = startingIndex ; i < firstPlayerIndex; i++) {
                        currentlyPlaying = game.getPlayers().get(i).nickname;
                        saveState();
                        planningPhase(clients.get(game.getPlayers().get(i).nickname));
                        clients.get(game.getPlayers().get(i).nickname).sendWaitingPlanningPhase();
                    }
                }
                else {
                    for (int i = startingIndex; i < clients.size(); i++) {
                        currentlyPlaying = game.getPlayers().get(i).nickname;
                        saveState();
                        planningPhase(clients.get(game.getPlayers().get(i).nickname));
                        clients.get(game.getPlayers().get(i).nickname).sendWaitingPlanningPhase();
                    }
                    for (int i = 0; i < firstPlayerIndex; i++) {
                        currentlyPlaying = game.getPlayers().get(i).nickname;
                        saveState();
                        planningPhase(clients.get(game.getPlayers().get(i).nickname));
                        clients.get(game.getPlayers().get(i).nickname).sendWaitingPlanningPhase();
                    }
                }

                gameStatus = GameStatus.AddToClouds;
                currentlyPlaying = null;
                saveState();
            }

            //Random students are automatically moved from the bag to the clouds
            if (gameStatus == GameStatus.AddToClouds) {
                addToClouds();
                gameStatus = GameStatus.MoveStudent;
                saveState();
            }

            //Arranges the order of action phase playing basing on the played assistants values
            ArrayList<ServerClientHandler> sortedClients = new ArrayList<ServerClientHandler>(clients.values());
            sortedClients.sort(Comparator.comparing(ServerClientHandler::getPlayer));

            //If the game was interrupted during action phase, the player who was playing before will continue playing.
            int startingIndex = 0;
            if (currentlyPlaying != null) {
                for (int i = 0; i < sortedClients.size(); i++) {
                    if (sortedClients.get(i).nickname.equals(currentlyPlaying)) {
                        startingIndex = i;
                        break;
                    }
                }
            }

            //Communicates to non-playing clients that one player is playing action phase
            for (int i = 0; i < sortedClients.size(); i++) {
                if (i != startingIndex) {
                    sortedClients.get(i).sendWaitingActionPhase();
                }
            }

            //If not all clouds are full of students, the cloud choice phase is skipped
            boolean skipClouds = !game.gameMap.getClouds().stream().allMatch((cloud -> cloud.peekStudents().getTotalStudents() == game.studentsToAddToClouds()));

            for (int i = startingIndex; i < sortedClients.size(); i++) {
                currentlyPlaying = sortedClients.get(i).nickname;
                saveState();
                wasCharacterUsed = false;
                if (gameStatus == GameStatus.MoveStudent) {
                    try {
                        moveStudents(sortedClients.get(i));
                    } catch(GameOver e) {
                        //return;
                    }
                    gameStatus = GameStatus.MoveMotherNature;
                    saveState();
                }
                if (gameStatus == GameStatus.MoveMotherNature) {
                    try {
                        moveMotherNature(sortedClients.get(i));
                    } catch(GameOver e) {
                        //The game could finish immediately if all towers of a player are finished or if few island groups are left
                        return;
                    }

                    gameStatus = GameStatus.ChooseCloud;
                    saveState();
                }
                if (gameStatus == GameStatus.ChooseCloud && !skipClouds) {
                    chooseCloud(sortedClients.get(i));
                    gameStatus = GameStatus.MoveStudent;
                    saveState();
                }
                sortedClients.get(i).sendWaitingActionPhase();
            }

            currentlyPlaying = null;
            saveState();

            if (checkWinner(GameStatus.PlanningPhase)) {
                return;
            }

            game.reset();

            synchronizePlayers();

            //Sets the first player to play in the next turn as the first to have played the action phase
            for (int i = 0; i < game.getPlayers().size(); i++) {
                if (clients.get(game.getPlayers().get(i).nickname) == sortedClients.get(0)) {
                    firstPlayerIndex = i;
                    break;
                }
            }

            gameStatus = GameStatus.PlanningPhase;
        }
    }

    /**
     * The client chooses an assistant
     * @param client
     */
    private void planningPhase(ServerClientHandler client) {
        try {
            awaitPlayers();
            game.addUsedAssistant(client.requestAssistant(game.getUsedAssistants()));
            synchronizePlayers();
        } catch(ClientNotFound e) {
            awaitPlayers();
            try {
                game.addUsedAssistant(client.requestAssistant(game.getUsedAssistants()));
            } catch(ClientNotFound ignored) {}
        }
    }

    /**
     * Students are automatically added from the bag to the clouds.
     */
    private void addToClouds() {
        //Students are added to the clouds
        for (Cloud cloud : game.gameMap.getClouds()) {
            Students students = new Students();
            for (int i = 0; i < game.studentsToAddToClouds(); i++) {
                if (game.bag.getStudents().isEmpty()) {
                    break;
                }
                students.add(game.bag.takeStudent());
            }
            cloud.setStudents(students);
        }
        synchronizePlayers();
    }

    /**
     * The player needs to move students from the entrance, and, if not already played, play a character.
     * @param client
     */
    private void moveStudents(ServerClientHandler client) throws GameOver {
        int movedStudents = 0;

        awaitPlayers();
        while (movedStudents < game.studentsMovements()) {
            boolean tmpWasCharacterUsed = this.wasCharacterUsed;
            try {
                client.moveStudent();
            } catch(ClientNotFound e) {
                awaitPlayers();
                continue;
            }
            if (wasCharacterUsed && !tmpWasCharacterUsed) {
                synchronizePlayers();
            }
            else {
                synchronizePlayers();
                movedStudents++;
            }
            if (checkWinner(gameStatus)) {
                throw(new GameOver());
            }
        }
    }

    /**
     * The player needs to move mother nature, and, if not already played, play a character.
     * @param client
     */
    private void moveMotherNature(ServerClientHandler client) throws GameOver {
        awaitPlayers();
        while (true) {
            boolean tmpWasCharacterUsed = this.wasCharacterUsed;
            try {
                client.moveMotherNature();
            } catch(ClientNotFound e) {
                awaitPlayers();
                continue;
            }

            if (!tmpWasCharacterUsed && wasCharacterUsed) {
                synchronizePlayers();
                try {
                    client.moveMotherNature();
                } catch(ClientNotFound e) {
                    awaitPlayers();
                    continue;
                }
            }

            if (checkWinner(gameStatus)) {
                throw(new GameOver());
            }

            break;
        }
        synchronizePlayers();
    }

    /**
     * The player needs to choose a cloud, and, if not already played, play a character.
     * @param client
     */
    private void chooseCloud(ServerClientHandler client) {
        awaitPlayers();
        while (true) {
            boolean tmpWasCharacterUsed = this.wasCharacterUsed;
            try {
                client.chooseCloud();
            } catch(ClientNotFound e) {
                awaitPlayers();
                continue;
            }

            if (!tmpWasCharacterUsed && wasCharacterUsed) {
                synchronizePlayers();
                try {
                    client.chooseCloud();
                } catch(ClientNotFound e) {
                    awaitPlayers();
                    continue;
                }
            }
            break;
        }
        synchronizePlayers();
    }

    public Collection<ServerClientHandler> getClients() {
        return clients.values();
    }

    /**
     * Sends to all clients the current value of Game and of their Player, waiting for all of them to confirm they received it.
     */
    public void synchronizePlayers() {
        clients.values().forEach((client -> client.synchronize(true)));

        synchronized (this) {
            while (!clients.values().stream().allMatch(ServerClientHandler::isSynchronized)) {
                try {
                    wait();
                } catch(InterruptedException ignored) {}
            }
        }
    }

    /**
     * Checks that there's more than 1 player online, if not, the game stops and waits for players to reconnect
     */
    private synchronized void awaitPlayers() {
        int clientsConnected = (int) clients.values().stream().filter(ServerClientHandler::isClientConnected).count();

        if (clientsConnected > 1) {
            return;
        }

        clients.values().forEach(ServerClientHandler::awaitOtherPlayers);

        while (clients.values().stream().filter(ServerClientHandler::isClientConnected).count() == 1) {
            try {
                wait();
            } catch(InterruptedException ignored) {}
        }
    }

    public Game getGame() {
        return game;
    }

    /**
     *
     * @return an instance of the current game that doesn't contain private data about players, so that it can safely be sent to each client.
     */
    public Game getSafeGame() {
        GameSerializer gameSerializer = new GameSerializer();
        Game game = gameSerializer.deserialize(gameSerializer.serialize(this.game));
        for (Player player : game.getPlayers()) {
            player.getAssistantsOwned().clear();
        }
        return game;
    }

    /**
     * Checks if the game is over, if so it communicates who won and who lost.
     * @return true if the game is over, false if not.
     */
    private boolean checkWinner(GameStatus gameStatus) {
        VictoryType victoryType = game.isGameOver();

        if (victoryType == VictoryType.None) {
            return false;
        }
        else if ((victoryType == VictoryType.EmptyBag || victoryType == VictoryType.EmptyAssistants) && gameStatus.isActionPhase()) {
            return false;
        }

        ArrayList<Player> winners = game.getWinners();

        final boolean tie = winners.size() > 1;

        for (ServerClientHandler client : clients.values()) {
            if (winners.stream().anyMatch((player -> Objects.equals(player.nickname, client.nickname)))) {
                if (tie) {
                    client.tie();
                }
                else {
                    client.win();
                }
            }
            else {
                client.lose();
            }
        }
        setGameOver();
        return true;
    }

    /**
     * Saves the current state of the game on disk, with the id assigned by ServerGames
     */
    private void saveState() {
        try {
            FileWriter fileWriter = new FileWriter("." + File.separator + "state" + File.separator + id + "_state.json");
            fileWriter.write(new Gson().toJson(this));
            fileWriter.flush();
            fileWriter.close();
        } catch(IOException ignored) {}
    }

    public boolean gameStarted() {
        return gameStarted;
    }

    public boolean isGameSet() {
        return gameSet;
    }

    private void setGameOver() {
        gameOver = true;
        gameStarted = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Set<String> getNicknames() {
        return new HashSet<String>(nicknames);
    }

    public boolean wasCharacterUsed() {
        return wasCharacterUsed;
    }

    public void setWasCharacterUsed(boolean wasCharacterUsed) {
        this.wasCharacterUsed = wasCharacterUsed;
    }
}
