package it.polimi.ingsw.model.Messages;

import it.polimi.ingsw.server.ServerGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * ADT representing a list of games that a player can join, with the current number of players, the maximum number of players and the games ids.
 * Useful to communicate the games which the clients can join.
 */
public class AvailableGames {
    private final HashMap<GameType, ArrayList<AvailableGame>> availableGames = new HashMap<>();

    /**
     * @param availableGame the AvailableGame instance to add
     */
    public void addGame(AvailableGame availableGame) {
        if (!availableGames.containsKey(availableGame.gameType)) {
            availableGames.put(availableGame.gameType, new ArrayList<>());
        }
        availableGames.get(availableGame.gameType).add(availableGame);
    }


    /**
     * @param gameType
     * @return All games in the ADT of the given GameType
     */
    public ArrayList<AvailableGame> getType(GameType gameType) {
        if (!availableGames.containsKey(gameType)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(availableGames.get(gameType));
    }

    /**
     *
     * @return All ids of the games in the collection
     */
    public Collection<Integer> getIDs() {
        HashSet<Integer> ids = new HashSet<>();

        for (ArrayList<AvailableGame> availableGames : availableGames.values()) {
            for (AvailableGame availableGame : availableGames) {
                ids.add(availableGame.id);
            }
        }

        return ids;
    }

    /**
     *
     * @param gameId
     * @return the AvailableGame with the given gameId
     */
    public AvailableGame get(int gameId) {
        for (ArrayList<AvailableGame> availableGames : availableGames.values()) {
            for (AvailableGame availableGame : availableGames) {
                if (availableGame.id == gameId) {
                    return availableGame;
                }
            }
        }
        return null;
    }
}

