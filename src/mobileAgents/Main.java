package mobileAgents;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private GUIState state;
    private GUI gui;
    private Forest forest;
    private String config;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parameters parameters = getParameters();
        config = parameters.getUnnamed().get(0);
        state = new GUIState();
        gui = new GUI(primaryStage, state);
        forest = new Forest(config,state);

    }

    public static void main(String[] args) {

        launch(args);

    }
}
