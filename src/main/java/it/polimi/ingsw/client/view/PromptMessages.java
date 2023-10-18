package it.polimi.ingsw.client.view;

/**
 * Class useful to retrieve commonly used messages in the game, both in CLI and in GUI.
 * @author Francesco Barabino
 * @author Gianluca Bertolini
 */
public class PromptMessages {
    public static String moveStudent() {
        return "Move a student from the entrance to an island or from the entrance to the dining room.";
    }

    public static String moveMotherNature() {
        return "Move mother nature to another island";
    }

    public static String chooseCloud() {
        return "Choose a cloud to take students from";
    }

    public static String waitingPlanningPhase() {
        return "Waiting for other players planning phase";
    }

    public static String waitingActionPhase() {
        return "Waiting for other players action phase";
    }

    public static String chooseAssistant() { return "Choose an assistant to play"; }

    public static String waitingForPlayers() { return "Waiting for other players..."; }

    public static String waitingForRejoin() { return  "Awaiting for players to rejoin the game..."; }

    public static String serverError() { return "Sorry, it appears that something bad happened to this server..."; }

    public static String chooseNickname() { return "Choose a nickname to start your Experience"; }

    public static String chooseGame() { return "Choose a game to join or create a new one"; }

    public static String setPlayers() { return "Set the number of players (2 or 3)"; }

    public static String setMode() { return "Do you want to play in Expert Mode? (Y or n)"; }
}
