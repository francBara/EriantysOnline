package it.polimi.ingsw.server;
import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.server.Exceptions.ClientNotFound;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Server class that manages the interaction with a client.
 */
public class ClientIO {
    private final Scanner scanner;
    private final PrintWriter writer;

    private String lastRead;
    private boolean isConnected = true;

    final ClientLogin clientLogin;

    public ClientIO(Socket socket, ClientLogin clientLogin) throws IOException  {
        scanner = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        this.clientLogin = clientLogin;
        scan();
    }

    /**
     * It sends a Message request to the client, clears all previous client input.
     * @param message the Message to send.
     * @param arguments arguments that will be sent in order after the main Message.
     * @throws ClientNotFound if the client couldn't be reached.
     */
    public synchronized void write(Message message, String... arguments) throws ClientNotFound {
        if (!isConnected) {
            return;
        }
        try {
            lastRead = null;
            writer.println(message.name());
            for (String argument : arguments) {
                writer.println(argument);
            }
        } catch(Exception e) {
            isConnected = false;
            throw(new ClientNotFound());
        }
    }

    /**
     * Reads a message from the client.
     * @return the client message.
     * @throws ClientNotFound if the client couldn't be reached.
     */
    public synchronized String readLine() throws ClientNotFound {
        try {
            if (!isConnected) {
                throw(new ClientNotFound());
            }
            while (lastRead == null && isConnected) {
                wait();
            }

            if (!isConnected) {
                throw(new ClientNotFound());
            }
            return lastRead;
        } catch(InterruptedException e) {
            throw(new RuntimeException());
        }
    }

    /**
     * @return true if the client is connected and reachable, false if not.
     */
    public synchronized boolean isConnected() {
        if (!isConnected) {
            return false;
        }
        try {
            write(Message.Ping);
        } catch(ClientNotFound e) {
            isConnected = false;
            return false;
        }

        return true;
    }

    /**
     * Listens for client messages in a new thread, caching them in lastRead field.
     * If the client couldn't be reached isConnected is set to false.
     */
    private void scan() {
        new Thread(() -> {
            while (true) {
                try {
                    lastRead = scanner.nextLine();
                    if (lastRead.equals("quit")) {
                        isConnected = false;
                        synchronized (this) {
                            notifyAll();
                        }
                        clientLogin.refreshIO();
                        clientLogin.sendAvailableGames();
                        return;
                    }
                } catch(Exception e) {
                    isConnected = false;
                    synchronized (this) {
                        notifyAll();
                        return;
                    }
                }
                synchronized (this) {
                    notifyAll();
                }
            }
        }).start();
    }
}
