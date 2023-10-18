package it.polimi.ingsw.model.Messages;

public enum Message {
    RequestNickname,
    ChooseGame,
    SetGame,
    RequestUseAssistant,
    WaitingPlanningPhase,
    MoveStudent,
    MoveMotherNature,
    ChooseCloud,
    WaitingActionPhase,
    InterruptedGame,
    Synchronize,
    Won,
    Tie,
    Lost,
    KO,
    OK,
    Ping;

    public boolean isActionPhase() {
        return this == MoveStudent || this == MoveMotherNature || this == ChooseCloud;
    }
}
