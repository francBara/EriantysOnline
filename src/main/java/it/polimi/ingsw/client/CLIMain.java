package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientConnectionHandler;
import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.CLI.CLIView;
import it.polimi.ingsw.client.view.View;

public class CLIMain {
    public static void main(String[] args) {
        Controller controller = new Controller();
        View view = new CLIView(controller);
        controller.setConnectionHandler(new ClientConnectionHandler(controller));
        view.startScreen();
    }
}
