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
            //validateConfig();
            state = new GUIState();
            gui = new GUI(primaryStage, state, fire);
            forest = new Forest(config, state);
            forest.startSimulation();
        }else {
            printInstructions();
            System.exit(1);
        }

    }

    private void validateConfig() {
        //TODO if the config file fails print out config failure message and instructions and System.exit(1)
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
