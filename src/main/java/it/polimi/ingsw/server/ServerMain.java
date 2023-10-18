package it.polimi.ingsw.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static final ServerGames serverGames = new ServerGames();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ServerSocket mainServerSocket;
        int port;

        System.out.println("Insert a port for clients to connect to:");

        while (true) {
            try {
                port = Integer.parseInt(scanner.nextLine());
                mainServerSocket = new ServerSocket(port);
                break;
            } catch(NumberFormatException e) {
                System.out.println("This port is not valid, please insert another one");
            } catch(IOException | SecurityException | IllegalArgumentException e) {
                System.out.println("This port is not available, please insert another one.");
            }
        }

        final ExecutorService executor = Executors.newCachedThreadPool();

        try {
            System.out.println("Server started on port " + port + ".");
            System.out.println("Type QUIT to stop the server");

            new Thread(() -> {
                while (true) {
                    if (scanner.nextLine().equals("QUIT")) {
                        System.exit(0);
                    }
                }
            }).start();

            while (true) {
                Socket clientSocket = mainServerSocket.accept();
                executor.submit(new ClientLogin(clientSocket, serverGames));
            }

        } catch(Exception e) {
            System.out.println("Internal server error");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
