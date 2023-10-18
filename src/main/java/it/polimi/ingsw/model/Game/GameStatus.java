package it.polimi.ingsw.model.Game;

/**
 * Enum containing all the phases of a game.
 */
public enum GameStatus {
    MakeGame,
    PlanningPhase,
    AddToClouds,
    MoveStudent,
    MoveMotherNature,
    ChooseCloud;

    public boolean isActionPhase() {
        return this == MoveStudent || this == MoveMotherNature || this == ChooseCloud;
    }
}
