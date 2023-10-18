package it.polimi.ingsw.model.Messages;

/**
 * Helper class containing useful info about a game that a client can join.
 */
public class AvailableGame {
    public final int id;
    public final int playersNumber;
    public final int maxPlayersNumber;
    public final GameType gameType;

    public AvailableGame(int id, int playersNumber, int maxPlayersNumber, GameType gameType) {
        this.id = id;
        this.playersNumber = playersNumber;
        this.maxPlayersNumber = maxPlayersNumber;
        this.gameType = gameType;
    }
}
