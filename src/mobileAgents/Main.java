package mobileAgents;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private GUIState state;
    private GUI gui;

    @Override
    public void start(Stage primaryStage) throws Exception {
        state = new GUIState();
        gui = new GUI(primaryStage, state);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
