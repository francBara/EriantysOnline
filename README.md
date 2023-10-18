## Eriantys Game online multiplayer implementation

[Eriantys](https://www.craniocreations.it/prodotto/eriantys) is a table game by Cranio Creations.
Here is an online multiplayer implementation of the game, where a match can be hosted locally by a player for other players to join.

###### Features
- Players can disconnect from a game and rejoin later, if other players continue player while the former is disconnected, the CPU will play for him
- A player can host multiple games with different players, leaving and rejoining as he pleases
- If a game is left by all players they will be able to rejoin, since the game is saved locally


Launch Mode - Server:
1. Launch your OS command prompt, or launch the terminal in Linux, or launch WSL in Windows
2. Change Directory to the Server Directory
3. Execute command java -jar Server.jar to start the Server
4. Insert the port for clients to connect to

Launch Mode - Command Line Game:
1. Launch the terminal in Linux, or launch WSL in Windows
2. Change Directory to the CLI Directory
3. Execute command java -jar CLI.jar to start the Client from Command Line

Launch Mode - Graphical Interface Game:
1. Launch your OS command prompt
2. Change Directory to the GUI Directory
3. Execute command java -jar GUI.jar to start the Client with the Graphical Interface

To play:
1. Insert the IP Address and the Port to connect to the game
2. Set a new Game or join an already existing one
3. Play the game
4. Use the 'help' command to check how the different commands work while playing in CLI
5. Note that the Faction of the Students must be written all together, or the game will ask again for you input while playing in CLI

### Notes

Due to copyright reasons, the graphics of the game were gitignored, since they've been provided by Cranio Creations for exclusive use.
Therefore the GUI version of the game is not playable as it is.
