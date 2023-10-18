package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Messages.Message;
import it.polimi.ingsw.server.Exceptions.ClientNotFound;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerHostHandler extends ServerClientHandler {
    public ServerHostHandler(ClientIO io, String nickname, ServerGame serverGame) {
        super(io, nickname, serverGame);
    }

    public String[] requestGameSettings() throws IOException, ClientNotFound {
        io.write(Message.SetGame);

        while (true) {
            String[] gameSettings = io.readLine().split(" ");
            if (gameSettings.length != 2) {
                io.write(Message.KO);
                continue;
            }
            int numberOfPlayers;
            try {
                numberOfPlayers = Integer.parseInt(gameSettings[0]);
            } catch(NumberFormatException e) {
                io.write(Message.KO);
                continue;
            }

            if (numberOfPlayers < 2 || numberOfPlayers > 3) {
                io.write(Message.KO);
                continue;
            }

            if (!gameSettings[1].equals("false") && !gameSettings[1].equals("true")) {
                io.write(Message.KO);
                continue;
            }
            io.write(Message.OK);
            return gameSettings;
        }
    }
}
