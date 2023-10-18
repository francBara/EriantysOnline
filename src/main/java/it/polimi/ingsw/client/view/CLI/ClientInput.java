package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.Controller;

import java.util.Locale;
import java.util.Scanner;

/**
 * Manages the input of the client in command line mode.
 * @author Francesco Barabino
 */
public class ClientInput {
    private final CLIView view;
    private final Scanner scanner = new Scanner(System.in);
    private final Controller controller;

    String lastRead;

    public ClientInput(CLIView view, Controller controller) {
        this.view = view;
        this.controller = controller;
        read();
    }

    /**
     * Continuously listens for user input in a separate thread, handling special input and notifies the nextLine method.
     */
    private void read() {
        new Thread(() -> {
            while (true) {
                lastRead = scanner.nextLine();
                if (lastRead.toLowerCase(Locale.ROOT).equals("help")) {
                    view.displayHelp();
                    lastRead = null;
                }
                else if (lastRead.equals("QUIT")) {
                    controller.actionPerformed("quit");
                }
                else {
                    synchronized (this) {
                        notifyAll();
                    }
                }
            }
        }).start();
    }

    /**
     *
     * @return the first string inserted by the user after this method was called.
     */
    public synchronized String nextLine() {
        lastRead = null;

        while (lastRead == null) {
            try {
                wait();
            } catch(InterruptedException ignored) {}
        }

        return lastRead;
    }
}
