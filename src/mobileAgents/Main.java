package mobileAgents;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private GUIState state;
    private GUI gui;
    private Forest forest;
    private String config;
    private boolean fire = false;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //get the parameters from args
        Parameters parameters = getParameters();
        //make sure there is either 1 or 2 parameters else exit
        if(parameters.getUnnamed().size()>=1 && parameters.getUnnamed().size()<=2){
            //process the second parameter if it even exists
            if(parameters.getUnnamed().size() == 2){
                if(parameters.getUnnamed().get(1).equals("fire")){
                    fire = true;
                }
                else{
                   printInstructions();
                   System.exit(1);
                }
            }
            config = parameters.getUnnamed().get(0);
            state = new GUIState();
            forest = new Forest(config,state);
            if(forest.isValidConfig()) {
                gui = new GUI(primaryStage, state, fire);
                forest.startSimulation();
            }
            else {
                printConfigInstructions();
                System.exit(1);
            }
        }else {
            printInstructions();
            System.exit(1);
        }

    }

    /**
     * Prints instructions for config file
     */
    private void printConfigInstructions() {
        System.err.println("Invalid Config File: \n" +
                "Locations of Base Station and Initial Fire must be a Node within file.\n" +
                "If multiple Base Stations and Initial Fires are present, only the last occurrences are used.");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stopping");
        gui.shutdown();
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {

        launch(args);

    }

    private void printInstructions(){
        System.out.println("MobileAgents Help - " +
                "MobileAgents can be launched in two ways:\n\n" +
                "1. java -jar mobileAgents [config-file]\n" +
                "2. java -jar mobileAgents [config-file] [fire]\n\n" +
                "1 will run the application with a basic render of the fire nodes\n" +
                "2 will run the application with a fire particle system which may be too resource intensive for older systems.\n\n" +
                "Example: \n\tjava -jar mobileAgents sample2.txt fire\n\tjava -jar mobileAgents sample2.txt");
    }
}
