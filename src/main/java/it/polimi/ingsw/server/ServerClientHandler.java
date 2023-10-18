package it.polimi.ingsw.server;
import com.google.gson.Gson;
import it.polimi.ingsw.model.Board.Exceptions.FullEntranceException;
import it.polimi.ingsw.model.Messages.ActionPhaseMessages.*;
import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Game.GameSerializer;
import it.polimi.ingsw.model.Map.Cloud;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.server.Exceptions.ClientNotFound;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

/**
 * The server class which handles game interactions with a specific client.
 */
public class ServerClientHandler {
    private Player player;
    final private ServerGame serverGame;
    private boolean isSynchronized = false;
    public final String nickname;

    protected ClientIO io;

    /**
     *
     * @param io The ClientIO to communicate with the client
     * @param nickname The nickname of the client
     * @param serverGame The ServerGame in which the client is playing
     */
    public ServerClientHandler(ClientIO io, String nickname, ServerGame serverGame) {
        this.serverGame = serverGame;
        this.nickname = nickname;
        this.io = io;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Resets the ClientIO for the client, useful when a client rejoins a left game.
     * @param io
     */
    public void setSocket(ClientIO io) {
        this.io = io;
        if (serverGame.gameStarted()) {
            serverGame.synchronizePlayers();
        }
        else {
            awaitOtherPlayers();
        }
    }

    /**
     * Requests the client to choose an Assistant to play, if set to automatic mode it chooses the first one available.
     * @param usedAssistants The assistants that were already played in this round.
     * @return the chosen Assistant.
     * @throws ClientNotFound if the client is offline.
     */
    public Assistant requestAssistant(Set<Assistant> usedAssistants) throws ClientNotFound {
        if (!io.isConnected()) {
            for (int i = 0; i < player.getAssistantsOwned().size(); i++) {
                int finalI = i;
                if (usedAssistants.stream().noneMatch(usedAssistant -> usedAssistant.turnValue == player.getAssistantsOwned().get(finalI).turnValue)) {
                    final Assistant playedAssistant = player.getAssistantsOwned().get(i);
                    player.playAssistant(i);
                    return playedAssistant;
                }
            }
            final Assistant playedAssistant = player.getAssistantsOwned().get(0);
            player.setAssistantQueue((int) usedAssistants.stream().filter(usedAssistant -> usedAssistant.turnValue == player.getAssistantsOwned().get(0).turnValue).count());
            player.playAssistant(0);
            return playedAssistant;
        }

        io.write(Message.RequestUseAssistant);

        int assistantId;

        while (true) {;
            try {
                assistantId = Integer.parseInt(io.readLine());
            } catch(NumberFormatException e) {
                io.write(Message.KO);
                continue;
            }

            if (assistantId < 0 || assistantId >= player.getAssistantsOwned().size()) {
                io.write(Message.KO);
                continue;
            }

            final Assistant chosenAssistant = player.getAssistantsOwned().get(assistantId);

            int numberOfTimesUsed = (int) usedAssistants.stream().filter(usedAssistant -> usedAssistant.turnValue == chosenAssistant.turnValue).count();
            if (numberOfTimesUsed > 0) {
                //If the player only has assistants who were already played, he's allowed to use one but will play after the first player who used it.
                if (player.getAssistantsOwned().stream().allMatch(assistant -> usedAssistants.stream().anyMatch(usedAssistant -> assistant.turnValue == usedAssistant.turnValue))) {
                    player.setAssistantQueue(numberOfTimesUsed);
                }
                else {
                    io.write(Message.KO);
                    continue;
                }
            }


            player.playAssistant(assistantId);
            io.write(Message.OK);

            return chosenAssistant;
        }
    }

    /**
     * Requests the player to initiate the action phase: the player can choose to
     *
     * Move a student from the entrance to the dining room,
     * Move a student from the entrance to an island,
     * Play a character.
     *
     * If the client is automatic, the students are randomly moved to islands and entrance.
     *
     * @throws ClientNotFound
     */
    public void moveStudent() throws ClientNotFound {
        if (!isClientConnected()) {
            Random random = new Random();
            //A student is moved to a random island
            if (random.nextBoolean()) {
                Faction chosenFaction = null;
                for (Faction faction : Faction.values()) {
                    if (player.getSchoolBoard().getEntrance().canTakeStudent(faction)) {
                        chosenFaction = faction;
                        break;
                    }
                }
                player.getSchoolBoard().getEntrance().remove(chosenFaction);
                serverGame.getGame().gameMap.getIslands().get(random.nextInt(serverGame.getGame().gameMap.getIslands().size())).placeStudent(chosenFaction);
            }
            //A student is moved to the hall
            else {
                Faction chosenFaction = null;
                for (Faction faction : Faction.values()) {
                    if (player.getSchoolBoard().getEntrance().canTakeStudent(faction) && !player.getSchoolBoard().getDiningTable(faction).isFull()) {
                        chosenFaction = faction;
                        break;
                    }
                }
                if (chosenFaction == null) {
                    for (Faction faction : Faction.values()) {
                        if (player.getSchoolBoard().getEntrance().canTakeStudent(faction)) {
                            chosenFaction = faction;
                            break;
                        }
                    }
                    player.getSchoolBoard().getEntrance().remove(chosenFaction);
                    serverGame.getGame().gameMap.getIslands().get(random.nextInt(serverGame.getGame().gameMap.getIslands().size())).placeStudent(chosenFaction);
                    return;
                }
                player.getSchoolBoard().getEntrance().remove(chosenFaction);
                player.getSchoolBoard().getDiningTable(chosenFaction).addStudent();
                serverGame.getGame().setProfessors();
                int takenCoins = player.getSchoolBoard().getDiningTable(chosenFaction).takeAllCoins();
                if (serverGame.getGame().getAvailableCoins() > takenCoins) {
                    serverGame.getGame().decrementAvailableCoins(takenCoins);
                    player.addCoins(takenCoins);
                }
            }
            return;
        }

        io.write(Message.MoveStudent);

        while (true) {
            StudentsMovementMessage actionPhaseMessage;
            try {
                actionPhaseMessage = new StudentsMovementMessage(io.readLine());
            } catch(BadActionPhaseMessage e) {
                io.write(Message.KO);
                continue;
            }

            if (actionPhaseMessage.isMoveStudentsToHall()) {
                if (!player.getSchoolBoard().getEntrance().canTakeStudent(actionPhaseMessage.getFaction()) || player.getSchoolBoard().getDiningTable(actionPhaseMessage.getFaction()).isFull()) {
                    io.write(Message.KO);
                    continue;
                }
                player.getSchoolBoard().getEntrance().remove(actionPhaseMessage.getFaction());
                player.getSchoolBoard().getDiningTable(actionPhaseMessage.getFaction()).addStudent();
                int takenCoins = player.getSchoolBoard().getDiningTable(actionPhaseMessage.getFaction()).takeAllCoins();
                if (serverGame.getGame().getAvailableCoins() > takenCoins) {
                    serverGame.getGame().decrementAvailableCoins(takenCoins);
                    player.addCoins(takenCoins);
                }
                serverGame.getGame().setProfessors();
                io.write(Message.OK);
                return;
            }
            else if (actionPhaseMessage.isMoveStudentsToIsland()) {
                if (!player.getSchoolBoard().getEntrance().canTakeStudent(actionPhaseMessage.getFaction()) || actionPhaseMessage.getIslandIndex() < 0 || actionPhaseMessage.getIslandIndex() >= serverGame.getGame().gameMap.getIslands().size()) {
                    io.write(Message.KO);
                    continue;
                }

                player.getSchoolBoard().getEntrance().remove(actionPhaseMessage.getFaction());
                serverGame.getGame().gameMap.getIslands().get(actionPhaseMessage.getIslandIndex()).placeStudent(actionPhaseMessage.getFaction());
                io.write(Message.OK);
                return;
            }
            else if (actionPhaseMessage.isUseCharacter()) {
                if (serverGame.wasCharacterUsed() || serverGame.getGame().getCharacters().get(actionPhaseMessage.getCharacterMessage().characterIndex).getCost() > player.getCoins()) {
                    io.write(Message.KO);
                    continue;
                }
                if (useCharacter(actionPhaseMessage.getCharacterMessage())) {
                    io.write(Message.OK);
                }
                else {
                    io.write(Message.KO);
                    continue;
                }
                serverGame.setWasCharacterUsed(true);
                return;
            }
            else {
                io.write(Message.KO);
            }
        }
    }

    /**
     * Requests the player to finish the action phase, the player can choose to:
     *
     * Move mother nature,
     * Play a character.
     * @throws ClientNotFound
     * @return true if the player moved mother nature, false if he chose to play a character.
     */
    public void moveMotherNature() throws ClientNotFound {
        if (!isClientConnected()) {
            serverGame.getGame().gameMap.moveMotherNature(new Random().nextInt(player.getMotherNatureValue()) + 1);
            serverGame.getGame().placeTower();
            serverGame.getGame().gameMap.mergeIslands();
            return;
        }

        io.write(Message.MoveMotherNature);

        while (true) {
            MotherNatureMovementMessage actionPhaseMessage;
            try {
                actionPhaseMessage = new MotherNatureMovementMessage(io.readLine());
            } catch(BadActionPhaseMessage e) {
                io.write(Message.KO);
                continue;
            }

            if (actionPhaseMessage.isUseCharacter()) {
                if (serverGame.wasCharacterUsed() || serverGame.getGame().getCharacters().get(actionPhaseMessage.getCharacterMessage().characterIndex).getCost() > player.getCoins()) {
                    io.write(Message.KO);
                    continue;
                }
                if (useCharacter(actionPhaseMessage.getCharacterMessage())) {
                    serverGame.setWasCharacterUsed(true);
                    io.write(Message.OK);
                    return;
                }
                else {
                    io.write(Message.KO);
                }
            }
            else {
                if (actionPhaseMessage.getMotherNatureSteps() < 1 || actionPhaseMessage.getMotherNatureSteps() > player.getMotherNatureValue()) {
                    io.write(Message.KO);
                    continue;
                }

                serverGame.getGame().gameMap.moveMotherNature(actionPhaseMessage.getMotherNatureSteps());
                serverGame.getGame().placeTower();
                serverGame.getGame().gameMap.mergeIslands();

                io.write(Message.OK);
                return;
            }
        }
    }

    /**
     * Requests the player to choose a cloud to take students from.
     * @throws ClientNotFound
     */
    public void chooseCloud() throws ClientNotFound {
        if (!isClientConnected()) {
            for (Cloud cloud : serverGame.getGame().gameMap.getClouds()) {
                if (!cloud.peekStudents().isEmpty()) {
                    player.getSchoolBoard().getEntrance().addStudents(cloud.takeStudents());
                    return;
                }
            }
        }

        io.write(Message.ChooseCloud);

        while (true) {
            ChooseCloudMessage message;
            try {
                message = new ChooseCloudMessage(io.readLine());
            } catch(BadActionPhaseMessage e) {
                io.write(Message.KO);
                continue;
            }

            if (message.isUseCharacter()) {
                if (serverGame.wasCharacterUsed() || serverGame.getGame().getCharacters().get(message.getCharacterMessage().characterIndex).getCost() > player.getCoins()) {
                    io.write(Message.KO);
                    continue;
                }
                if (useCharacter(message.getCharacterMessage())) {
                    io.write(Message.OK);
                }
                else {
                    io.write(Message.KO);
                    continue;
                }
                serverGame.setWasCharacterUsed(true);
                break;
            }
            else {
                if (message.getCloudIndex() < 0 || message.getCloudIndex() >= serverGame.getGame().gameMap.getClouds().size()) {
                    io.write(Message.KO);
                }
                else if (serverGame.getGame().gameMap.getClouds().get(message.getCloudIndex()).peekStudents().isEmpty()) {
                    io.write(Message.KO);
                }
                else {
                    try {
                        player.getSchoolBoard().getEntrance().addStudents(serverGame.getGame().gameMap.getClouds().get(message.getCloudIndex()).takeStudents());
                    } catch(FullEntranceException ignored) {}
                    io.write(Message.OK);
                    break;
                }
            }
        }
    }

    public void sendWaitingActionPhase() {
        new Thread(() -> {
            if (!io.isConnected()) {
                return;
            }
            try {
                io.write(Message.WaitingActionPhase);
            } catch(ClientNotFound ignored) {}
        }).start();
    }

    public void sendWaitingPlanningPhase() {
        new Thread(() -> {
            if (!io.isConnected()) {
                return;
            }
            try {
                io.write(Message.WaitingPlanningPhase);
            } catch(ClientNotFound ignored) {}
        }).start();
    }

    /**
     * Parses a CharacterMessage and uses the effect of the character chosen by the player.
     * @param characterMessage
     * @return true if the character was correctly used, false if an error occurred.
     */
    private boolean useCharacter(CharacterMessage characterMessage) {
        CharacterCard chosenCharacter = serverGame.getGame().getCharacters().get(characterMessage.characterIndex);
        if (chosenCharacter.requiresFaction()) {
            if (chosenCharacter.getFactionRequirement() == null) {
                return false;
            }
            chosenCharacter.getFactionRequirement().setFaction(characterMessage.getFaction());
        }
        if (chosenCharacter.requiresIsland()) {
            if (chosenCharacter.getIslandRequirement() == null) {
                return false;
            }
            chosenCharacter.getIslandRequirement().setIsland(serverGame.getGame().gameMap.getIslands().get(characterMessage.getIslandIndex()));
        }

        final int characterCost = chosenCharacter.getCost();
        final boolean wasUsed = chosenCharacter.isUsed();
        chosenCharacter.useEffect(player);
        player.removeCoins(characterCost);
        if (wasUsed) {
            serverGame.getGame().addAvailableCoins(characterCost);
        }
        else {
            serverGame.getGame().addAvailableCoins(characterCost - 1);
        }
        return true;
    }

    public void win() {
        new Thread(() -> {
            try {
                io.write(Message.Won);
            } catch(ClientNotFound ignored) {}
        }).start();
    }

    public void tie() {
        new Thread(() -> {
            try {
                io.write(Message.Tie);
            } catch(ClientNotFound ignored) {}
        }).start();
    }

    public void lose() {
        new Thread(() -> {
            try {
                io.write(Message.Lost);
            } catch(ClientNotFound ignored) {}
        }).start();
    }

    public void awaitOtherPlayers() {
        new Thread(() -> {
            try {
                io.write(Message.InterruptedGame);
            } catch(ClientNotFound ignored) {}
        }).start();
    }

    public void synchronize(boolean notifyGame) {
        isSynchronized = false;
        new Thread(() -> {
            try {
                Gson gson = new Gson();

                io.write(Message.Synchronize, new GameSerializer().serialize(serverGame.getSafeGame()), gson.toJson(player));

               io.readLine();

                isSynchronized = true;

                if (notifyGame) {
                    synchronized (serverGame) {
                        serverGame.notifyAll();
                    }
                }

            } catch(ClientNotFound e) {
                isSynchronized = true;
            }
        }).start();
    }

    /**
     *
     * @return true if the client is connected, false if not.
     */
    public synchronized boolean isClientConnected() {
        return io.isConnected();
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }
}
