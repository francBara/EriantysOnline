package it.polimi.ingsw.client;
import it.polimi.ingsw.client.ClientConnectionHandler;
import it.polimi.ingsw.client.Controller;
import it.polimi.ingsw.client.view.GUI.GUIControllers.GUIController;
import it.polimi.ingsw.client.view.GUI.GUIView;
import it.polimi.ingsw.client.view.GUI.Screens;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIMain extends Application {
    private final Controller controller = new Controller();
    private final GUIView view = new GUIView();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        controller.setView(view);
        controller.setConnectionHandler(new ClientConnectionHandler(controller));
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws IOException  {
        final Screens screens = new Screens("StartScreen", controller);
        view.setScreens(screens);

        Scene scene = new Scene(screens.get());
        screens.setMain(scene);


        primaryStage.setScene(scene);
        primaryStage.setHeight(720);
        primaryStage.setWidth(1280);
        primaryStage.setResizable(false);

        primaryStage.setTitle("Eriantys");
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
