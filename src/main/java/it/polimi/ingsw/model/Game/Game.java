package it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Board.SchoolBoard;
import it.polimi.ingsw.model.Board.TowerColor;
import it.polimi.ingsw.model.Cards.Assistants.Assistant;
import it.polimi.ingsw.model.Cards.Assistants.AssistantsFactory;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharacterCard;
import it.polimi.ingsw.model.Cards.Characters.CharacterTypes.CharactersFactory;
import it.polimi.ingsw.model.Cards.Characters.Characters;
import it.polimi.ingsw.model.Exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Map.GameMap;
import it.polimi.ingsw.model.Map.Island;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.model.Player.Player;
import it.polimi.ingsw.model.Exceptions.WrongNumberOfPlayersException;
import it.polimi.ingsw.model.Students;

import java.util.*;

/**
 * The class representing a playable game, it contains players, the game map, the characters and the bag.
 * @author Francesco Barabino
 */
public class Game {
  public final int numberOfPlayers;
  private final ArrayList<Player> players;
  private final Set<Assistant> assistantsUsed;
  public final Bag bag;

  private Characters characters;

  public final GameMap gameMap;

  public final boolean isExpert;

  private boolean gameHasStarted = false;

  private int availableCoins;

  public Game(int numberOfPlayers, boolean isExpert) {
    this.numberOfPlayers = numberOfPlayers;
    this.isExpert = isExpert;
    assistantsUsed = new HashSet<Assistant>();
    players = new ArrayList<Player>();
    gameMap = new GameMap(new Random().nextInt(12), numberOfPlayers);
    bag = new Bag();
  }

  /**
   * @return the players who won the game, if the array contains more than one Player, the returned players finished the game in a draw
   */
  public ArrayList<Player> getWinners() {
    ArrayList<Player> winners = new ArrayList<Player>();
    winners.add(players.get(0));

    //Checks who have fewer towers
    for (int i = 1; i < players.size(); i++) {
      if (players.get(i).getSchoolBoard().getTowers() < winners.get(0).getSchoolBoard().getTowers()) {
        winners.clear();
        winners.add(players.get(i));
      }
      else if (players.get(i).getSchoolBoard().getTowers() == winners.get(0).getSchoolBoard().getTowers()) {
        winners.add(players.get(i));
      }
    }

    ArrayList<Player> filteredWinners = new ArrayList<Player>();

    Player winner = winners.get(0);
    filteredWinners.add(winner);

    //Checks who have more professors
    for (int i = 1; i < winners.size(); i++) {
      if (winners.get(i).getSchoolBoard().getProfessors().size() > winner.getSchoolBoard().getProfessors().size()) {
        winner = winners.get(i);
        filteredWinners.clear();
        filteredWinners.add(winner);
      }
      else if (winners.get(i).getSchoolBoard().getProfessors().size() == winner.getSchoolBoard().getProfessors().size()) {
        filteredWinners.add(winners.get(i));
      }
    }

    return filteredWinners;
  }

  /**
   * @return the VictoryType for the end of this game, if the game is not over the value of VictoryType will be equal to None
   */
  public VictoryType isGameOver() {
    for (Player player : players) {
      if (player.getSchoolBoard().getTowers() == 0) {
        return VictoryType.EmptyTowers;
      }
      else if (player.getAssistantsOwned().isEmpty()) {
        return VictoryType.EmptyAssistants;
      }
    }

    if (gameMap.getIslands().size() <= 3) {
      return VictoryType.IslandsGroups;
    }

    else if (bag.getStudents().isEmpty()) {
      return VictoryType.EmptyBag;
    }

    return VictoryType.None;
  }

  /**
   * Adds a player to the game.
   * @param player
   */
  public void addPlayer(Player player) {
    if (gameHasStarted) {
      throw(new RuntimeException());
    }
    players.add(player);
  }

  /**
   * Initializes the game, by placing school boards, towers, assistant cards, character cards and students in the bag.
   * @throws WrongNumberOfPlayersException if the number of players is different from the actual players in the game.
   * @throws GameAlreadyStartedException if the initGame method has already been called.
   */
  public void initGame() throws WrongNumberOfPlayersException, GameAlreadyStartedException {
    if (numberOfPlayers != players.size()) {
      throw(new WrongNumberOfPlayersException());
    }
    if (gameHasStarted) {
      throw(new GameAlreadyStartedException());
    }
    int availableStudents = 130;

    final int numberOfTowers = numberOfPlayers == 2 ? 8 : 6;

    //Sets school boards and towers for each player
    int i = 0;
    for (TowerColor towerColor : TowerColor.values()) {
      players.get(i).setSchoolBoard(new SchoolBoard(numberOfTowers, numberOfTowers, towerColor));
      i++;
      if (i == numberOfPlayers) {
        break;
      }
    }

    //Sets the assistants for each player
    ArrayList<Assistant> assistants = new AssistantsFactory().getAssistants();
    for (Player player : players) {
      player.chooseAssistantDeck(new HashSet<Assistant>(assistants));
    }

    //Sets the characters if the game is set in expert mode
    if (isExpert) {
      characters = new Characters(this);
    }

    //Places two students of each faction in the bag.
    for (Faction faction : Faction.values()) {
      bag.addBackStudents(faction, 2);
      availableStudents -= 2;
    }

    int oppositeIsland = 12 - (6 - gameMap.getMotherNaturePosition());
    if (oppositeIsland >= 12) {
      oppositeIsland -= 12;
    }

    //Places one student on each island.
    for (i = 0; i < 12; i++) {
      if (i == gameMap.getMotherNaturePosition() || i == oppositeIsland) {
        continue;
      }
      gameMap.getIslands().get(i).placeStudent(bag.takeStudent());
    }

    //Fills the bag
    for (i = 0; i < availableStudents; i++) {
      bag.addBackStudents(Faction.getRandom(), 1);
    }

    //Places students in the entrances of the players.
    final int studentsInEntrance = players.size() == 2 ? 7 : 9;
    for (Player player : players) {
      Students students = new Students();
      for (i = 0; i < studentsInEntrance; i++) {
        students.add(bag.takeStudent());
      }
      player.getSchoolBoard().getEntrance().addStudents(students);
    }
    availableCoins = 20 - numberOfPlayers;

    gameHasStarted = true;
  }

  /**
   * Builds the characters starting from the cache, useful in deserialization.
   */
  public void buildCharacters() {
    if (!isExpert || characters == null) {
      return;
    }
    characters.build(this);
  }

  /**
   * Caches the characters starting from the CharacterCard instances, useful for serialization.
   */
  public void cacheCharacters() {
    if (!isExpert || characters == null) {
      return;
    }
    characters.cache();
  }

  /**
   *
   * @param assistant to add to usedAssistants.
   */
  public void addUsedAssistant(Assistant assistant) {
    assistantsUsed.add(assistant);
  }

  /**
   *
   * @return a copy of usedAssistants set
   */
  public Set<Assistant> getUsedAssistants() {
    return new HashSet<Assistant>(assistantsUsed);
  }

  /**
   * @param assistants owned by the player who called this method
   * @return true if the player has no choice about his assistants, and is therefore allowed to play any assistant he prefers.
   */
  public boolean canUseAnyAssistant(ArrayList<Assistant> assistants) {
    return assistants.stream().allMatch(assistant -> this.assistantsUsed.stream().anyMatch(usedAssistant -> assistant.turnValue == usedAssistant.turnValue));
  }

  /**
   * Useful at the end of each turn, resetting characters effects, and clearing used assistants.
   */
  public void reset() {
    for (Player player : players) {
      player.setProfessorsBonus(false);
      player.removeBonusMotherNatureSteps();
      player.setBonusInfluence(0);
    }
    for (Island island : gameMap.getIslands()) {
      island.setIgnoreTowers(false);
      island.removeIgnoredFaction();
    }
    assistantsUsed.clear();
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  /**
   * Sets the professors for every player, by checking the number of students in the dining tables and the influence on the islands.
   */
  public void setProfessors() {
    for (Faction faction : Faction.values()) {
      Player playerWithProfessor = players.get(0);
      playerWithProfessor.getSchoolBoard().getDiningTable(faction).updateProfessor(playerWithProfessor.getSchoolBoard().getDiningTable(faction).getStudents() > 0);
      for (int i = 1; i < players.size(); i++) {
        if (players.get(i).getSchoolBoard().getDiningTable(faction).getStudents() > playerWithProfessor.getSchoolBoard().getDiningTable(faction).getStudents()) {
          playerWithProfessor.getSchoolBoard().getDiningTable(faction).updateProfessor(false);
          playerWithProfessor = players.get(i);
          playerWithProfessor.getSchoolBoard().getDiningTable(faction).updateProfessor(true);
        }
        else if (players.get(i).getSchoolBoard().getDiningTable(faction).getStudents() == playerWithProfessor.getSchoolBoard().getDiningTable(faction).getStudents()) {
          if (players.get(i).getProfessorsBonus() && !playerWithProfessor.getProfessorsBonus()) {
            playerWithProfessor.getSchoolBoard().getDiningTable(faction).updateProfessor(false);
            playerWithProfessor = players.get(i);
            playerWithProfessor.getSchoolBoard().getDiningTable(faction).updateProfessor(true);
          }
          else if (playerWithProfessor.getProfessorsBonus() && !players.get(i).getProfessorsBonus()) {
            players.get(i).getSchoolBoard().getDiningTable(faction).updateProfessor(false);
          }
          else {
            if (players.get(i).getSchoolBoard().getDiningTable(faction).hasProfessor()) {
              playerWithProfessor.getSchoolBoard().getDiningTable(faction).updateProfessor(false);
              playerWithProfessor = players.get(i);
            }
          }
        }
        else {
          players.get(i).getSchoolBoard().getDiningTable(faction).updateProfessor(false);
        }
      }
    }
  }

  /**
   * Places a tower, if possible, where mother nature is.
   */
  public void placeTower() {
    boolean willPlaceTower = false;
    int maxInfluence = 0;
    Player towerPlayer = null;

    for (Player player : players) {
      int currentInfluence = gameMap.getMotherNatureIsland().getInfluence(player);
      if (currentInfluence > maxInfluence) {
        maxInfluence = currentInfluence;
        towerPlayer = player;
        willPlaceTower = true;
      }
      else if (currentInfluence == maxInfluence) {
        willPlaceTower = false;
      }
    }

    if (towerPlayer != null && (towerPlayer.getSchoolBoard().getTowers() == 0 || gameMap.getMotherNatureIsland().getTowersColor() == towerPlayer.getSchoolBoard().getTowersColor())) {
      return;
    }

    if (willPlaceTower) {
      if (gameMap.getMotherNatureIsland().getTowersNumber() > 0) {
        for (Player player : players) {
          if (player != towerPlayer && player.getSchoolBoard().getTowersColor() == gameMap.getMotherNatureIsland().getTowersColor()) {
            player.getSchoolBoard().addTowers(gameMap.getMotherNatureIsland().getTowersNumber());
            break;
          }
        }
      }

      gameMap.getMotherNatureIsland().placeTower(towerPlayer.getSchoolBoard().getTowersColor());
      towerPlayer.getSchoolBoard().removeTower();
    }
  }

  /**
   *
   * @return a copy of the characters array.
   */
  public ArrayList<CharacterCard> getCharacters() {
    return characters.get();
  }

  public int studentsToAddToClouds() {
    return numberOfPlayers == 2 ? 3 : 4;
  }

  public int studentsMovements() {
    return numberOfPlayers == 2 ? 3 : 4;
  }

  public int getAvailableCoins() {
    return availableCoins;
  }

  public void decrementAvailableCoins(int coins) {
    this.availableCoins -= coins;
  }

  public void addAvailableCoins(int coins) {
    this.availableCoins += coins;
  }
}


  
