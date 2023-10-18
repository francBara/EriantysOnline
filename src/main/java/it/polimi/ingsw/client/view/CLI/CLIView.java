package it.polimi.ingsw.client.view.CLI;
import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.PromptMessages;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.model.Board.DiningTable;
import it.polimi.ingsw.model.Board.Entrance;
import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Game.Game;
import it.polimi.ingsw.model.Map.GameMap;
import it.polimi.ingsw.model.Messages.AvailableGame;
import it.polimi.ingsw.model.Messages.AvailableGames;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Students;

import javax.sound.midi.SysexMessage;
import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

/**
 * The command line interface view.
 * @author Gianluca Bertolini
 */
public class CLIView implements View {
    private final Controller controller;
    private final ClientInput scanner;
    private String lastMessage;

    private boolean isChoosingGames = false;

    public CLIView(Controller controller) {
        this.controller = controller;
        controller.setView(this);
        scanner = new ClientInput(this, controller);
    }

    @Override
    public void startScreen() {
        System.out.println(" _______       _                             " + "\n" +
                           "(_______)     (_)             _              " + "\n" +
                           " _____    ____ _ _____ ____ _│ │_ _   _  ___ " + "\n" +
                           "│  ___)  ╱ ___) (____ │  _ (_   _) │ │ │╱___)" + "\n" +
                           "│ │_____│ │   │ ╱ ___ │ │ │ ││ │_│ │_│ │___ │" + "\n" +
                           "│_______)_│   │_╲_____│_│ │_│ ╲__)╲__  (___╱ " + "\n" +
                           "                                 (____╱      ");
        System.out.println("{ Type \033[36m\"help\"\033[0m for help }\n");
        setIP();
        //controller.connect("127.0.0.1", 1000);
    }

    @Override
    public void setIP() {
        String inputIP, inputPort;
        boolean flagIP, flagPort;
        int port;

        System.out.println("{ Insert the IP Address }");
        do {
            inputIP = scanner.nextLine();
            flagIP = checkIPStructure(inputIP);
            if (!flagIP) {
                System.out.println("You did not input a structurally correct \033[36mIP Address\033[0m");
            }
        } while (!flagIP);

        System.out.println("{ Insert port }");
        do {
            inputPort = scanner.nextLine();
            flagPort = checkPortStructure(inputPort);
            if (!flagPort) {
                System.out.println("You did not input a structurally correct \033[36mPort Number\033[0m");
            }
        } while (!flagPort);
        port = Integer.parseInt(inputPort);

        controller.connect(inputIP, port);
    }

    @Override
    public void startGame(Game game, Player player) {
        updateGame(game, player);
    }

    @Override
    public void updateGame(Game game, Player player) {
        clearScreen();

        for (int i = 0; i < game.gameMap.getIslands().size(); i++) {
            String flag, flagSpace;
            String islandSpace;
            String studentsSpaceRed, studentsSpaceGreen, studentsSpaceBlue, studentsSpacePink, studentsSpaceYellow;
            int numberOfTowers = game.gameMap.getIslands().get(i).getTowersNumber();
            String towerColor, towerSpace;

            if (game.gameMap.getIslands().get(i).getIsMotherNaturePlaced()) {
                flag = "Yes";
                flagSpace = " ┃";
            } else {
                flag = "No";
                flagSpace = "  ┃";
            }

            if(i >= 10) {
                islandSpace = "         ┃";
            } else {
                islandSpace = "          ┃";
            }

            int red = game.gameMap.getIslands().get(i).getStudents().get(Faction.RedDragons);
            if (red >= 10) {
                studentsSpaceRed = " ┃";
            } else {
                studentsSpaceRed = "  ┃";
            }
            int green = game.gameMap.getIslands().get(i).getStudents().get(Faction.GreenFrogs);
            if (green >= 10) {
                studentsSpaceGreen = " ┃";
            } else {
                studentsSpaceGreen = "  ┃";
            }
            int blue = game.gameMap.getIslands().get(i).getStudents().get(Faction.BlueUnicorns);
            if (blue >= 10) {
                studentsSpaceBlue = " ┃";
            } else {
                studentsSpaceBlue = "  ┃";
            }
            int pink = game.gameMap.getIslands().get(i).getStudents().get(Faction.PinkFairies);
            if (pink >= 10) {
                studentsSpacePink = " ┃";
            } else {
                studentsSpacePink = "  ┃";
            }
            int yellow = game.gameMap.getIslands().get(i).getStudents().get(Faction.YellowGnomes);
            if (yellow >= 10) {
                studentsSpaceYellow = " ┃";
            } else {
                studentsSpaceYellow = "  ┃";
            }

            if(game.gameMap.getIslands().get(i).getTowersColor() == null) {
                towerColor = "None";
            } else {
                towerColor = String.valueOf(game.gameMap.getIslands().get(i).getTowersColor());
            }
            if (towerColor.equals("Black") || towerColor.equals("White")) {
                towerSpace = "       ┃";
            } else {
                towerSpace = "        ┃";
            }
            System.out.println("╱━━━━━━━━━━━━━━━━━━━━╲" + "\n" +
                    "┃ Island: " + "\033[36m" + i + "\033[0m" + islandSpace + "\n" +
                    "┃ Mother Nature: " + "\033[36m" + flag + "\033[0m" + flagSpace + "\n" +
                    "┃ Students:          ┃" + "\n" +
                    "┃ \033[31mRed Dragons\033[0m:    \033[36m" + red + "\033[0m" + studentsSpaceRed + "\n" +
                    "┃ \033[32mGreen Frogs\033[0m:    \033[36m" + green + "\033[0m" + studentsSpaceGreen + "\n" +
                    "┃ \033[34mBlue Unicorns\033[0m:  \033[36m" + blue + "\033[0m" + studentsSpaceBlue + "\n" +
                    "┃ \033[35mPink Fairies\033[0m:   \033[36m" + pink + "\033[0m" + studentsSpacePink + "\n" +
                    "┃ \033[33mYellow Gnomes\033[0m:  \033[36m" + yellow + "\033[0m" + studentsSpaceYellow + "\n" +
                    "┃ Towers: " + "\033[36m" + numberOfTowers + "\033[0m" + "          ┃" + "\n" +
                    "┃ Color: " + "\033[36m" + towerColor + "\033[0m" + towerSpace + "\n" +
                    "╲━━━━━━━━━━━━━━━━━━━━╱");
            System.out.println();
        }
        System.out.println();


        for (int i = 0; i < game.gameMap.getClouds().size(); i++) {
            String studentsSpaceRed, studentsSpaceGreen, studentsSpaceBlue, studentsSpacePink, studentsSpaceYellow;

            int red = game.gameMap.getClouds().get(i).peekStudents().get(Faction.RedDragons);
            if (red >= 10) {
                studentsSpaceRed = " )";
            } else {
                studentsSpaceRed = "  )";
            }
            int green = game.gameMap.getClouds().get(i).peekStudents().get(Faction.GreenFrogs);
            if (green >= 10) {
                studentsSpaceGreen = " )";
            } else {
                studentsSpaceGreen = "  )";
            }
            int blue = game.gameMap.getClouds().get(i).peekStudents().get(Faction.BlueUnicorns);
            if (blue >= 10) {
                studentsSpaceBlue = " )";
            } else {
                studentsSpaceBlue = "  )";
            }
            int pink = game.gameMap.getClouds().get(i).peekStudents().get(Faction.PinkFairies);
            if (pink >= 10) {
                studentsSpacePink = " )";
            } else {
                studentsSpacePink = "  )";
            }
            int yellow = game.gameMap.getClouds().get(i).peekStudents().get(Faction.YellowGnomes);
            if (yellow >= 10) {
                studentsSpaceYellow = " )";
            } else {
                studentsSpaceYellow = "  )";
            }

            System.out.println(" ~~~~~~~~~~~~~~~~~~~~ " + "\n" +
                    "( Cloud: " + "\033[36m" + i + "\033[0m" + "           )" + "\n" +
                    "( Students:          )" + "\n" +
                    "( \033[31mRed Dragons\033[0m:    \033[36m" + red + "\033[0m" + studentsSpaceRed + "\n" +
                    "( \033[32mGreen Frogs\033[0m:    \033[36m" + green + "\033[0m" + studentsSpaceGreen + "\n" +
                    "( \033[34mBlue Unicorns\033[0m:  \033[36m" + blue + "\033[0m" + studentsSpaceBlue + "\n" +
                    "( \033[35mPink Fairies\033[0m:   \033[36m" + pink + "\033[0m" + studentsSpacePink + "\n" +
                    "( \033[33mYellow Gnomes\033[0m:  \033[36m" + yellow + "\033[0m" + studentsSpaceYellow + "\n" +
                    " ~~~~~~~~~~~~~~~~~~~~ ");
            System.out.println();
        }
        System.out.println();

        if (game.isExpert) {
            int coins = player.getCoins(), supply = game.getAvailableCoins();
            String coinsSpace, supplySpace;
            if(coins >= 10) {
                coinsSpace = "    ┃";
            } else {
                coinsSpace = "     ┃";
            }
            if(supply >= 10) {
                supplySpace = " ┃";
            } else {
                supplySpace = "  ┃";
            }
            System.out.println("┏━━━━━━━━━━━━━━━━━━━━┓\n" +
                               "┃ Coins Owned: \033[36m" + coins + "\033[0m" + coinsSpace + "\n" +
                               "┃ General Supply: \033[36m" + supply + "\033[0m" + supplySpace + "\n" +
                               "┗━━━━━━━━━━━━━━━━━━━━┛");
            for (int i = 0; i < game.getCharacters().size(); i++) {
                int cost = game.getCharacters().get(i).getCost();
                String characterName = game.getCharacters().get(i).getName(), characterDescription = game.getCharacters().get(i).getDescription();
                StringBuilder nameSpace = new StringBuilder();
                String costSpace;
                int j = 0;
                while (j < 16 - characterName.length()) {
                    nameSpace.append(" ");
                    j++;
                }
                nameSpace.append(" ┃");
                if (cost >= 10) {
                    costSpace = "               ┃";
                } else
                    costSpace = "                ┃";
                System.out.println(    "┏━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                       "┃ Character: \033[36m" + i + "\033[0m" + "           ┃" + "\n" +
                                       "┃ Name: \033[36m" + characterName + "\033[0m" + nameSpace + "\n" +
                                       "┃ Cost: \033[36m" + cost + "\033[0m" + costSpace);
                if(game.getCharacters().get(i).requiresFaction()) {
                    System.out.println("┃ Requires \033[36mFaction\033[0m       ┃");
                }
                if(game.getCharacters().get(i).requiresIsland()) {
                    System.out.println("┃ Requires \033[36mIsland Index\033[0m  ┃");
                }
                if(game.getCharacters().get(i).getStudents() != null) {
                    int red = game.getCharacters().get(i).getStudents().get(Faction.RedDragons);
                    int green = game.getCharacters().get(i).getStudents().get(Faction.GreenFrogs);
                    int blue = game.getCharacters().get(i).getStudents().get(Faction.BlueUnicorns);
                    int pink = game.getCharacters().get(i).getStudents().get(Faction.PinkFairies);
                    int yellow = game.getCharacters().get(i).getStudents().get(Faction.YellowGnomes);
                    System.out.println("┃ Students:              ┃" + "\n" +
                                       "┃ \033[31mRed Dragons\033[0m:   \033[36m" + red + "\033[0m" + "       ┃" + "\n" +
                                       "┃ \033[34mBlue Unicorns\033[0m: \033[36m" + blue + "\033[0m" + "       ┃" + "\n" +
                                       "┃ \033[33mYellow Gnomes\033[0m: \033[36m" + yellow + "\033[0m" + "       ┃" + "\n" +
                                       "┃ \033[35mPink Fairies\033[0m:  \033[36m" + pink + "\033[0m" + "       ┃" + "\n" +
                                       "┃ \033[32mGreen Frogs\033[0m:   \033[36m" + green + "\033[0m" + "       ┃");
                }
                System.out.println(    "┗━━━━━━━━━━━━━━━━━━━━━━━━┛" + "\n" +
                                       ">> Description: \033[40m" + characterDescription + "\033[0m");
                System.out.println();
            }
            System.out.println();
        }

        for (int i = 0; i < player.getAssistantsOwned().size(); i++) {
            int assistantValue = player.getAssistantsOwned().get(i).turnValue, assistantSteps = player.getAssistantsOwned().get(i).motherNatureMovement;
            String valueSpace, assistantSpace;
            if (i == 10) {
                assistantSpace = " ┃";
            } else {
                assistantSpace = "  ┃";
            }
            if (assistantValue == 10) {
                valueSpace = "     ┃";
            } else {
                valueSpace = "      ┃";
            }
            System.out.println("┏━━━━━━━━━━━━━━━┓" + "\n" +
                               "┃ Assistant: \033[36m" + i + "\033[0m" + assistantSpace +"\n" +
                               "┃ Value: \033[36m" + assistantValue + "\033[0m" + valueSpace + "\n" +
                               "┃ Steps: \033[36m" + assistantSteps + "\033[0m" + "      ┃" + "\n" +
                               "┗━━━━━━━━━━━━━━━┛");
            System.out.println();
        }

        System.out.println();


        for (Faction faction : Faction.values()) {
            StringBuilder factionSpace = new StringBuilder();
            String factionName = faction.toString();
            String studentsSpace;
            String hasProfessor;
            String professorSpace;
            int studentsNumber = player.getSchoolBoard().getDiningTable(faction).getStudents();
            int j = 0;

            while (j < 12 - factionName.length()) {
                factionSpace.append(" ");
                j++;
            }
            factionSpace.append(" ┃");
            if (studentsNumber == 10) {
                studentsSpace = "               ┃";
            } else {
                studentsSpace = "                ┃";
            }
            if (player.getSchoolBoard().getDiningTable(faction).hasProfessor()) {
                hasProfessor = "Yes";
                professorSpace = "             ┃";
            } else {
                hasProfessor = "No";
                professorSpace = "              ┃";
            }
            String color = "\033[36m";
            if (factionName.equals("RedDragons")) {
                color = "\033[31m";
            }
            if (factionName.equals("YellowGnomes")) {
                color = "\033[33m";
            }
            if (factionName.equals("BlueUnicorns")) {
                color = "\033[34m";
            }
            if (factionName.equals("GreenFrogs")) {
                color = "\033[32m";
            }
            if (factionName.equals("PinkFairies")) {
                color = "\033[35m";
            }
            System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                               "┃ Dining Table: " + color + factionName + "\033[0m" + factionSpace + "\n" +
                               "┃ Students: \033[36m" + studentsNumber + "\033[0m" + studentsSpace +"\n" +
                               "┃ Professor: \033[36m" + hasProfessor + "\033[0m" + professorSpace + "\n" +
                               "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
            System.out.println();
        }

        System.out.println();

        int red = player.getSchoolBoard().getEntrance().getStudents().get(Faction.RedDragons);
        int blue = player.getSchoolBoard().getEntrance().getStudents().get(Faction.BlueUnicorns);
        int yellow = player.getSchoolBoard().getEntrance().getStudents().get(Faction.YellowGnomes);
        int pink = player.getSchoolBoard().getEntrance().getStudents().get(Faction.PinkFairies);
        int green = player.getSchoolBoard().getEntrance().getStudents().get(Faction.GreenFrogs);
        System.out.println("┏━━━━━━━━━━━━━━━━━━┓" + "\n" +
                           "┃ Entrance:        ┃" + "\n" +
                           "┃ \033[31mRed Dragons\033[0m:   \033[36m" + red + "\033[0m ┃" + "\n" +
                           "┃ \033[34mBlue Unicorns\033[0m: \033[36m" + blue + "\033[0m ┃" + "\n" +
                           "┃ \033[33mYellow Gnomes\033[0m: \033[36m" + yellow + "\033[0m ┃" + "\n" +
                           "┃ \033[35mPink Fairies\033[0m:  \033[36m" + pink + "\033[0m ┃" + "\n" +
                           "┃ \033[32mGreen Frogs\033[0m:   \033[36m" + green + "\033[0m ┃" + "\n" +
                           "┗━━━━━━━━━━━━━━━━━━┛");
        System.out.println();

        String colorOfTower = String.valueOf(player.getSchoolBoard().getTowersColor());
        String colorOfTowerSpace;
        int numberOfTowers = player.getSchoolBoard().getTowers();
        if(colorOfTower.equals("Grey")) {
            colorOfTowerSpace = "    ┃";
        } else {
            colorOfTowerSpace = "   ┃";
        }
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                           "┃ Your Color: \033[36m"+ colorOfTower + "\033[0m" + colorOfTowerSpace + "\n" +
                           "┃ Remaining Towers: \033[36m" + numberOfTowers + "\033[0m ┃" + "\n" +
                           "┗━━━━━━━━━━━━━━━━━━━━━┛");
        System.out.println();

        if (lastMessage != null) {
            System.out.println("{ " + lastMessage + " }");
        }
    }

    @Override
    public void setGame() {
        String gameSettings, gameMode;
        System.out.println("{ " + PromptMessages.setPlayers() + " }");
        do {
            gameSettings = scanner.nextLine();
            if(!gameSettings.equals("2") && !gameSettings.equals("3")) {
                System.out.println("You should input a \033[36mNUMBER\033[0m that is \033[36m2\033[0m OR \033[36m3\033[0m for the number of players");
            }
        } while(!gameSettings.equals("2") && !gameSettings.equals("3"));
        System.out.println("{ " + PromptMessages.setMode() + " }");
        do {
            gameMode = scanner.nextLine();
            if(!gameMode.equals("Y") && !gameMode.equals("y") && !gameMode.equals("N") && !gameMode.equals("n")) {
                System.out.println("You should input \033[36mY\033[0m OR \033[36mn\033[0m for the expert mode");
            }
        } while (!gameMode.equals("Y") && !gameMode.equals("y") && !gameMode.equals("N") && !gameMode.equals("n"));
        gameMode = gameMode.toUpperCase();
        gameSettings += " " + gameMode.equals("Y");
        controller.actionPerformed(gameSettings);
    }

    @Override
    public void setNickname() {
        if (controller.getLastMessage() == Message.RequestNickname) {
            System.out.println("This \033[36mnickname\033[0m is not valid, please choose another one:");
        }
        else {
            System.out.println("{ " + PromptMessages.chooseNickname() + " }");
        }
        final String nickname = scanner.nextLine();
        controller.actionPerformed(nickname);
    }

    @Override
    public void chooseGame(AvailableGames availableGames) {
        new Thread(() -> {
            for (Integer id : availableGames.getIDs()) {
                System.out.println("-> Game \033[36m" + id + "\033[0m: " + availableGames.get(id).gameType + "\n" +
                                   "   Players in Lobby: " + availableGames.get(id).playersNumber + "/" + availableGames.get(id).maxPlayersNumber);
            }
            System.out.println("-> Game: " + "\033[31mNew\033[0m" + ": Start a new Game");
            System.out.println();

            if (!isChoosingGames) {
                String settings;
                boolean flagIds = false;
                isChoosingGames = true;

                System.out.println("{ " + PromptMessages.chooseGame() + " }");
                do {
                    settings = scanner.nextLine();
                    if(isANumber(settings)) {
                        flagIds = respectsIds(settings, controller.getAvailableGames());
                    } else {
                        settings = settings.toLowerCase();
                        if (settings.equals("new")) {
                            settings = "-1";
                        }
                    }

                    if(!settings.equals("-1") && !flagIds) {
                        System.out.println("You should input \033[36mNew\033[0m or \033[36m-1\033[0m OR one of the \033[36mGAME IDS\033[0m available");
                    }
                } while (!settings.equals("-1") && !flagIds);
                controller.actionPerformed(settings);
                isChoosingGames = false;
            }
        }).start();
    }

    @Override
    public void waitingPlayers() {
        System.out.println("{ "+ PromptMessages.waitingForPlayers() +" }\n");
    }

    @Override
    public void chooseCloud(Game game) {
        String inputCloud;
        int cloudSize = game.gameMap.getClouds().size(), showSize = cloudSize - 1;
        boolean flagRequirements, flagSize, wasCloudAlreadySelected, flagExpert, flagCoins;
        Player player = controller.getPlayer();

        displayMessage(PromptMessages.chooseCloud());
        do {
            flagRequirements = true;
            flagSize = true;
            wasCloudAlreadySelected = false;
            flagExpert = true;
            flagCoins = true;
            inputCloud = scanner.nextLine();
            if (isANumber(inputCloud)) {
                flagSize = respectsSize(inputCloud, cloudSize);
                if (!flagSize) {
                    System.out.println("You should input a \033[36mNUMBER\033[0m between \033[36m0\033[0m and \033[36m" + showSize + "\033[0m OR the \033[36mcharacter\033[0m or \033[36mhelp\033[0m commands");
                } else {
                    int in = Integer.parseInt(inputCloud);
                    wasCloudAlreadySelected = game.gameMap.getClouds().get(in).peekStudents().isEmpty();
                    if (wasCloudAlreadySelected) {
                        System.out.println("The \033[36mCloud\033[0m you selected is EMPTY. Choose another one");
                    }
                }
            } else {
                if(!inputCloud.startsWith("character") && !inputCloud.startsWith("help")) {
                    System.out.println("You should input a \033[36mNUMBER\033[0m between \033[36m0\033[0m and \033[36m" + showSize + "\033[0m OR the \033[36mcharacter\033[0m or \033[36mhelp\033[0m commands");
                }
                if (!game.isExpert && inputCloud.startsWith("character")) {
                    flagExpert = false;
                    System.out.println("You cannot use the character \033[36mcommand\033[0m because you are NOT in \033[36mExpert\033[0m Mode");
                } else {
                    if (inputCloud.startsWith("character")) {
                        flagRequirements = respectsCharacterRequirements(inputCloud, game);
                        if (!flagRequirements) {
                            System.out.println("You did not input the correct arguments for the \033[36mCharacter\033[0m command");
                        } else {
                            flagCoins = respectsCoins(inputCloud, game, player);
                            if (!flagCoins) {
                                System.out.println("You don't have enough \033[36mCoins\033[0m to use this \033[36mCharacter\033[0m");
                            }
                            inputCloud = reCAPSCharacter(inputCloud, game);
                        }
                    }
                }
            }
        } while (!flagExpert || wasCloudAlreadySelected || !flagSize || (!isANumber(inputCloud) && !inputCloud.startsWith("character") && !inputCloud.startsWith("help")) || (inputCloud.startsWith("character") && (!flagRequirements || !flagCoins)));
        controller.actionPerformed(inputCloud);
    }

    @Override
    public void requestAssistant(Game game, Player player) {
        String inputAssistant;
        int assistantsSize = player.getAssistantsOwned().size(), showSize = assistantsSize - 1;
        boolean flagSize, hasNotOtherAssistantsToUse, wasAssistantAlreadyUsed;

        displayMessage(PromptMessages.chooseAssistant());
        do {
            flagSize = true;
            hasNotOtherAssistantsToUse = true;
            wasAssistantAlreadyUsed = false;
            inputAssistant = scanner.nextLine();
            if (isANumber(inputAssistant)) {
                flagSize = respectsSize(inputAssistant, assistantsSize);
                if (!flagSize) {
                    System.out.println("You should input a \033[36mnumber\033[0m between \033[36m0\033[0m and \033[36m" + showSize + "\033[0m");
                } else {
                    if(((game.getPlayers().size() == 2 || game.getPlayers().size() == 3) && player.getAssistantsOwned().size() == 1) || (game.getPlayers().size() == 3 && player.getAssistantsOwned().size() == 2)) {
                        hasNotOtherAssistantsToUse = game.canUseAnyAssistant(player.getAssistantsOwned());
                        if (!hasNotOtherAssistantsToUse) {
                            System.out.println("The selected \033[36mAssistant\033[0m was already used by someone else. Choose another one");
                        }
                    } else {
                        String assistantName = player.getAssistantsOwned().get(Integer.parseInt(inputAssistant)).name;
                        for(Assistant assistant : game.getUsedAssistants()) {
                            if(assistant.name.equals(assistantName)) {
                                wasAssistantAlreadyUsed = true;
                                System.out.println("The selected \033[36mAssistant\033[0m was already used by someone else. Choose another one");
                                break;
                            }
                        }
                    }
                }
            } else {
                System.out.println("You should input a \033[36mNUMBER\033[0m between \033[36m0\033[0m and \033[36m" + showSize + "\033[0m");
            }
        } while (wasAssistantAlreadyUsed || !hasNotOtherAssistantsToUse || !flagSize || !isANumber(inputAssistant));
        controller.actionPerformed(inputAssistant);
    }

    @Override
    public void dismissAssistants() {}

    @Override
    public void moveStudent(Game game, SchoolBoard schoolBoard) {
        String input;
        String[] inputSplit;
        boolean flagRequirements, flagSize, flagFaction, flagHall, flagEntrance, flagExpert, flagCoins;
        int islandsSize = game.gameMap.getIslands().size(), showSize = islandsSize - 1;
        Player player = controller.getPlayer();

        displayMessage(PromptMessages.moveStudent());
        do {
            flagExpert = true;
            flagRequirements = true;
            flagSize = true;
            flagFaction = true;
            flagHall = false;
            flagEntrance = true;
            flagCoins = true;
            input = scanner.nextLine();
            inputSplit = input.trim().split("\\s+");
            if(!isANumber(inputSplit[0])) {
                inputSplit[0] = inputSplit[0].toLowerCase();
                flagFaction = respectsFaction(inputSplit[0]);
                if(!flagFaction && !input.startsWith("character") && !input.startsWith("help")) {
                    System.out.println("You should input the correct \033[36mFACTION\033[0m choosing between \033[31mRedDragons\033[0m, \033[33mYellowGnomes\033[0m, \033[34mBlueUnicorns\033[0m, \033[35mPinkFairies\033[0m, \033[32mGreenFrogs\033[0m OR the \033[36mcharacter\033[0m or \033[36mhelp\033[0m commands");
                } else {
                    if (!input.startsWith("help")) {
                        if (inputSplit.length == 2 && !input.startsWith("character")) {
                            if (!isANumber(inputSplit[1])) {
                                System.out.println("You should input a \033[36mNUMBER\033[0m betweeen \033[36m0\033[0m and \033[36m" + showSize + "\033[0m");
                            } else {
                                flagSize = respectsSize(inputSplit[1], islandsSize);
                                if (!flagSize) {
                                    System.out.println("You should input a \033[36mNUMBER\033[0m between \033[36m0\033[0m and \033[36m" + showSize + "\033[0m");
                                }
                            }
                        } else {
                            if (!game.isExpert && input.startsWith("character")) {
                                flagExpert = false;
                                System.out.println("You cannot use the character \033[36mcommand\033[0m because you are NOT in \033[36mExpert\033[0m Mode");
                            } else {
                                if (input.startsWith("character")) {
                                    flagRequirements = respectsCharacterRequirements(input, game);
                                    if (!flagRequirements) {
                                        System.out.println("You did not input the correct arguments for the \033[36mCharacter\033[0m command");
                                    } else {
                                        flagCoins = respectsCoins(input, game, player);
                                        if (!flagCoins) {
                                            System.out.println("You don't have enough \033[36mCoins\033[0m to use this \033[36mCharacter\033[0m");
                                        }
                                        input = reCAPSCharacter(input, game);
                                    }
                                }
                            }
                        }
                    }
                }
                if(!input.startsWith("character") && !input.startsWith("help") && flagFaction) {
                    flagEntrance = isPresentInEntrance(inputSplit[0], schoolBoard);
                    if (!flagEntrance) {
                        System.out.println("The \033[36mEntrance\033[0m does not contain the selected \033[36mStudent\033[0m");
                    }
                    if(inputSplit.length == 1 && flagEntrance) {
                        flagHall = isHallFull(inputSplit[0], schoolBoard);
                        if(flagHall) {
                            System.out.println("The selected \033[36mStudent\033[0m cannot move to the \033[36mHall\033[0m because it is FULL");
                        }
                    }
                    input = reCAPS(input);
                }
            } else {
                System.out.println("You should input the correct \033[36mFACTION\033[0m choosing between \033[31mRedDragons\033[0m, \033[33mYellowGnomes\033[0m, \033[34mBlueUnicorns\033[0m, \033[35mPinkFairies\033[0m, \033[32mGreenFrogs\033[0m OR the \033[36mcharacter\033[0m or \033[36mhelp\033[0m commands");
            }

        } while (isANumber(input) || !flagExpert || !flagEntrance || flagHall || (!flagFaction && !input.startsWith("character") && !input.startsWith("help")) || (flagFaction && !flagSize) || (input.startsWith("character") && (!flagRequirements || !flagCoins)));
        controller.actionPerformed(input);
    }

    @Override
    public void moveMotherNature(Game game, Player player) {
        String input;
        int maxMovement = player.getMotherNatureValue();
        boolean flagRequirements, flagMovement, flagExpert, flagCoins;

        displayMessage(PromptMessages.moveMotherNature());
        do {
            flagExpert = true;
            flagRequirements = true;
            flagMovement = true;
            flagCoins = true;
            input = scanner.nextLine();
            if (isANumber(input)) {
                flagMovement = respectsMovement(input, maxMovement);
                if (!flagMovement) {
                    System.out.println("You should input a \033[36mnumber\033[0m BETWEEN \033[36m1\033[0m and \033[36m" + maxMovement + "\033[0m");
                }
            } else {
                if (!input.startsWith("character") && !input.startsWith("help")) {
                    System.out.println("You should input a \033[36mNUMBER\033[0m between \033[36m1\033[0m and \033[36m" + maxMovement + "\033[0m OR the \033[36mcharacter\033[0m or \033[36mhelp\033[0m commands");
                } else {
                    if (!game.isExpert && input.startsWith("character")) {
                        flagExpert = false;
                        System.out.println("You cannot use the character \033[36mcommand\033[0m because you are NOT in \033[36mExpert\033[0m Mode");
                    } else {
                        if (input.startsWith("character")) {
                            flagRequirements = respectsCharacterRequirements(input, game);
                            if (!flagRequirements) {
                                System.out.println("You didn't input the \033[36mcorrect arguments\033[0m for the character command");
                            } else {
                                flagCoins = respectsCoins(input, game, player);
                                if (!flagCoins) {
                                    System.out.println("You don't have enough \033[36mCoins\033[0m to use this \033[36mCharacter\033[0m");
                                }
                                input = reCAPSCharacter(input, game);
                            }
                        }
                    }
                }
            }
        } while (!flagExpert || (!input.startsWith("character") && !input.startsWith("help") && !isANumber(input)) || !flagMovement || (input.startsWith("character") && (!flagRequirements || !flagCoins)));
        controller.actionPerformed(input);
    }

    @Override
    public void interruptedGame() {
        System.out.println("{ " + PromptMessages.waitingForRejoin() + " }\n");
    }

    @Override
    public void displayServerError() {
        System.out.println("{ " + PromptMessages.serverError() + " }\n");
    }

    @Override
    public void displayMessage(String message) {
        lastMessage = message;
        Game game = controller.getGame();
        Player player = controller.getPlayer();
        updateGame(game, player);
    }

    public void displayHelp() {
        System.out.println("┌────────────────────────────────────────────────────────────────────────────────────────────────────────┐\n" +
                           "│ List of needed [inputs] while doing certain interactions:                                              │\n" +
                           "│ --> Choosing an Assistant: [AssistantIndex]                                                            │\n" +
                           "│ ˃ AssistantIndex is the number specified near 'Assistant:'                                             │\n" +
                           "│                                                                                                        │\n" +
                           "│ ➧ Moving a Student to an Island: [Faction] [IslandIndex]                                               │\n" +
                           "│ --> Faction is the 'team' of the student, written all stuck together                                   │\n" +
                           "│ ˃ IslandIndex is the number specified near 'Island:'                                                   │\n" +
                           "│                                                                                                        │\n" +
                           "│ --> Moving a Student to the Hall: [Faction]                                                            │\n" +
                           "│ ˃ Faction is the 'team' of the student, written all stuck together                                     │\n" +
                           "│                                                                                                        │\n" +
                           "│ --> Moving Mother Nature: [Steps]                                                                      │\n" +
                           "│ ˃ Steps are the number of islands Mother Nature should move through                                    │\n" +
                           "│                                                                                                        │\n" +
                           "│ --> Choosing a Cloud: [CloudIndex]                                                                     │\n" +
                           "│ ˃ CloudIndex is the number specified near 'Cloud:'                                                     │\n" +
                           "│                                                                                                        │\n" +
                           "│ --> Using a Character: character [CharacterIndex] [Faction*] [IslandIndex*]                            │\n" +
                           "│ !NOTE: type character [CharacterIndex] [Faction*] [IslandIndex*] during any of the interactions above  │\n" +
                           "│        instead of their usual needed input                                                             │\n" +
                           "│ ˃ CharacterIndex is the number specified near 'Character:'                                             │\n" +
                           "│ ˃ Faction*, if required, is the 'team of the student, written all stuck together                       │\n" +
                           "│ ˃ IslandIndex*, if required, is the number specified near 'Island:'                                    │\n" +
                           "└────────────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    @Override
    public void displayWin() {
        System.out.println("$$╲     $$╲                           $$╲      $$╲                     $$╲ " + "\n" +
                           "╲$$╲   $$  │                          $$ │ $╲  $$ │                    $$ │" + "\n" +
                           " ╲$$╲ $$  /  $$$$$$╲  $$╲   $$╲       $$ │$$$╲ $$ │ $$$$$$╲  $$$$$$$╲  $$ │" + "\n" +
                           "  ╲$$$$  /  $$  __$$╲ $$ │  $$ │      $$ $$ $$╲$$ │$$  __$$╲ $$  __$$╲ $$ │" + "\n" +
                           "   ╲$$  /   $$ ╱  $$ |$$ │  $$ │      $$$$  _$$$$ │$$ ╱  $$ │$$ │  $$ │╲__│" + "\n" +
                           "    $$ │    $$ │  $$ |$$ │  $$ │      $$$  ╱ ╲$$$ │$$ │  $$ │$$ │  $$ │    " + "\n" +
                           "    $$ │    ╲$$$$$$  |╲$$$$$$  │      $$  ╱   ╲$$ │╲$$$$$$  │$$ │  $$ │$$╲ " + "\n" +
                           "    ╲__│     ╲______/  ╲______/       ╲__╱     ╲__│ ╲______╱ ╲__│  ╲__│╲__│");
        System.out.println();
    }

    @Override
    public void displayTie() {
        System.out.println("  _____   _     _                     _______   _          _ " + "\n" +
                           " │_   _│ │ │   ( )                   │__   __│ (_)        │ │" + "\n" +
                           "   │ │   │ │_  │╱   ___      __ _       │ │     _    ___  │ │" + "\n" +
                           "   │ │   │ __│     ╱ __│    ╱ _` │      │ │    │ │  ╱ _ ╲ │ │" + "\n" +
                           "  _│ │_  │ │_      ╲__ ╲   │ (_│ │      │ │    │ │ │  __╱ │_│" + "\n" +
                           " │_____│  ╲__│     │___╱    ╲__,_│      │_│    │_│  ╲___| (_)");
        System.out.println();
    }

    @Override
    public void displayLost() {
        System.out.println("     )                  (                          ____ " + "\n" +
                           "  ( /(                  )╲ )                 )    │   ╱ " + "\n" +
                           "  )╲())          (     (()╱(              ( ╱(    │  ╱  " + "\n" +
                           " ((_)╲    (     ))╲     ╱(_))   (    (    )╲())   │ ╱   " + "\n" +
                           "__ ((_)   )╲   /((_)   (_))     )╲   )╲  (_))╱    │╱    " + "\n" +
                           "╲ ╲ ╱ ╱  ((_) (_))(    │ │     ((_) ((_) │ │_    (      " + "\n" +
                           " ╲ V ╱  ╱ _ ╲ │ ││ │   │ │__  ╱ _ ╲ (_-< │  _│   )╲     " + "\n" +
                           "  │_│   ╲___╱  ╲_,_│   │____│ ╲___╱ ╱__╱  ╲__│  ((_)    ");
        System.out.println();
    }

    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException ignored) {}
    }


    private boolean isANumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean respectsFaction(String string){
        String tString = string.toLowerCase();
        return tString.equals("reddragons") || tString.equals("yellowgnomes") || tString.equals("blueunicorns") || tString.equals("pinkfairies") || tString.equals("greenfrogs");
    }

    private boolean respectsSize(String string, int maxSize) {
        int in = Integer.parseInt(string);
        return in >= 0 && in < maxSize;
    }

    private boolean respectsMovement(String string, int maxMovement) {
        int in = Integer.parseInt(string);
        return in > 0 && in <= maxMovement;
    }

    private boolean respectsIds(String string, AvailableGames availableGames) {
        int in = Integer.parseInt(string);
        return availableGames.get(in) != null;
    }

    private boolean respectsCharacterRequirements(String string, Game game) {
        int sizeMax = game.gameMap.getIslands().size(), charSize = game.getCharacters().size();
        String[] splitString = string.trim().split("\\s+");
        boolean flagFactions, flagSize, flagChars;

        if (splitString.length >= 2) {
            if (isANumber(splitString[1])) {
                flagChars = respectsSize(splitString[1], charSize);
                if(!flagChars){
                    return false;
                } else {
                    int i = Integer.parseInt(splitString[1]);
                    if(game.getCharacters().get(i).requiresFaction() && game.getCharacters().get(i).requiresIsland()) {
                        if(splitString.length < 4) {
                            return false;
                        }
                        if(isANumber(splitString[2]) || !isANumber(splitString[3])) {
                            return false;
                        }
                        flagSize = respectsSize(splitString[3], sizeMax);
                        if(!flagSize) {
                            return false;
                        }
                        flagFactions = respectsFaction(splitString[2]);
                        if(!flagFactions) {
                            return false;
                        }
                        if(game.getCharacters().get(i).getStudents() != null) {
                            return isPresentInCharacter(splitString[2], i, game);
                        }
                    } else {
                        if (game.getCharacters().get(i).requiresFaction()) {
                            if(splitString.length < 3) {
                                return false;
                            }
                            if(isANumber(splitString[2])) {
                                return false;
                            }
                            flagFactions = respectsFaction(splitString[2]);
                            if(!flagFactions) {
                                return false;
                            }
                            if(game.getCharacters().get(i).getStudents() != null) {
                                return isPresentInCharacter(splitString[2], i, game);
                            }
                            return true;
                        } else {
                            if (game.getCharacters().get(i).requiresIsland()) {
                                if(splitString.length < 3) {
                                    return false;
                                }
                                if(!isANumber(splitString[2])) {
                                    return false;
                                }
                                flagSize = respectsSize(splitString[2], sizeMax);
                                return flagSize;
                            }
                        }
                    }
                    return true;
                }
            } else {
                return false;
            }
        } else { return false; }
    }

    private boolean isPresentInEntrance (String string, SchoolBoard schoolBoard) {
        string = string.toLowerCase();
        if (string.equals("reddragons")) {
            int red = schoolBoard.getEntrance().getStudents().get(Faction.RedDragons);
            return red > 0;
        }
        if (string.equals("yellowgnomes")) {
            int yellow = schoolBoard.getEntrance().getStudents().get(Faction.YellowGnomes);
            return yellow > 0;
        }
        if (string.equals("pinkfairies")) {
            int pink = schoolBoard.getEntrance().getStudents().get(Faction.PinkFairies);
            return pink > 0;
        }
        if (string.equals("blueunicorns")) {
            int blue = schoolBoard.getEntrance().getStudents().get(Faction.BlueUnicorns);
            return blue > 0;
        }
        if (string.equals("greenfrogs")) {
            int green = schoolBoard.getEntrance().getStudents().get(Faction.GreenFrogs);
            return green > 0;
        }
        return true;
    }

    private boolean isHallFull(String string, SchoolBoard schoolBoard) {
        if (string.equals("reddragons")) {
            int red = schoolBoard.getDiningTable(Faction.RedDragons).getStudents();
            return red >= 10;
        }
        if (string.equals("yellowgnomes")) {
            int yellow = schoolBoard.getDiningTable(Faction.YellowGnomes).getStudents();
            return yellow >= 10;
        }
        if (string.equals("blueunicorns")) {
            int blue = schoolBoard.getDiningTable(Faction.BlueUnicorns).getStudents();
            return blue >= 10;
        }
        if (string.equals("pinkfairies")) {
            int pink = schoolBoard.getDiningTable(Faction.PinkFairies).getStudents();
            return pink >= 10;
        }
        if (string.equals("greenfrogs")) {
            int green = schoolBoard.getDiningTable(Faction.GreenFrogs).getStudents();
            return green >= 10;
        }
        return false;
    }

    private boolean checkIPStructure(String string) {
        String[] splitString = string.trim().split("\\.");
        int val1, val2, val3, val4;

        if (string.length() < 7 || string.length() > 15) {
            return false;
        }
        if (splitString.length !=  4) {
            return false;
        }
        if(!isANumber(splitString[0]) || !isANumber(splitString[1]) || !isANumber(splitString[2]) || !isANumber(splitString[3])) {
            return false;
        }
        val1 = Integer.parseInt(splitString[0]);
        val2 = Integer.parseInt(splitString[1]);
        val3 = Integer.parseInt(splitString[2]);
        val4 = Integer.parseInt(splitString[3]);
        return (val1 >= 0 && val1 <= 255) && (val2 >= 0 && val2 <= 255) && (val3 >= 0 && val3 <= 255) && (val4 >= 0 && val4 <= 255);
    }

    private boolean checkPortStructure(String string) {
        if(!isANumber(string)) {
            return false;
        }
        int port = Integer.parseInt(string);
        return port >= 0 && port <= 65535;
    }

    private boolean respectsCoins (String string, Game game, Player player) {
        String[] splitString = string.trim().split("\\s+");
        int charIndex = Integer.parseInt(splitString[1]), charCost, playerMoney;

        charCost = game.getCharacters().get(charIndex).getCost();
        playerMoney = player.getCoins();

        return playerMoney >= charCost;
    }

    private String reCAPS (String string) {
        String[] splitString = string.trim().split("\\s+");
        String joinedString;
        splitString[0] = splitString[0].toLowerCase();
        if (splitString[0].equals("reddragons")) {
            splitString[0] = "RedDragons";
        }
        if (splitString[0].equals("yellowgnomes")) {
            splitString[0] = "YellowGnomes";
        }
        if (splitString[0].equals("blueunicorns")) {
            splitString[0] = "BlueUnicorns";
        }
        if (splitString[0].equals("greenfrogs")) {
            splitString[0] = "GreenFrogs";
        }
        if (splitString[0].equals("pinkfairies")) {
            splitString[0] = "PinkFairies";
        }
        if (splitString.length == 2) {
            joinedString = String.join(" ", splitString);
        } else {
            joinedString = splitString[0];
        }
        return joinedString;
    }

    private String reCAPSCharacter (String string, Game game) {
        String[] splitString = string.trim().split("\\s+");
        int index = Integer.parseInt(splitString[1]);
        String joinedString;

        if(game.getCharacters().get(index).requiresFaction()) {
            splitString[2] = reCAPS(splitString[2]);
        }
        joinedString = String.join(" ", splitString);
        return joinedString;
    }

    private boolean isPresentInCharacter (String string, int index, Game game) {
        string = string.toLowerCase();
        if (string.equals("reddragons")) {
            int red = game.getCharacters().get(index).getStudents().get(Faction.RedDragons);
            return red > 0;
        }
        if (string.equals("yellowgnomes")) {
            int yellow = game.getCharacters().get(index).getStudents().get(Faction.YellowGnomes);
            return yellow > 0;
        }
        if (string.equals("pinkfairies")) {
            int pink = game.getCharacters().get(index).getStudents().get(Faction.PinkFairies);
            return pink > 0;
        }
        if (string.equals("blueunicorns")) {
            int blue = game.getCharacters().get(index).getStudents().get(Faction.BlueUnicorns);
            return blue > 0;
        }
        if (string.equals("greenfrogs")) {
            int green = game.getCharacters().get(index).getStudents().get(Faction.GreenFrogs);
            return green > 0;
        }
        return true;
    }
}
