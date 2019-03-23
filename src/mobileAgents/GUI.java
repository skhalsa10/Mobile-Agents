package mobileAgents;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mobileAgents.Graphics.Sensor;
import mobileAgents.Graphics.Agent;


import java.util.HashMap;


public class GUI extends AnimationTimer {

    private final int RADIUS = 20;

    private GUIState state;
    private String config;

    private Stage stage;
    private Scene scene;
    private VBox root;
    private ScrollPane logsPane;
    private VBox logs;

    //graph related panes
    private Canvas graph;
    private GraphicsContext gc;
    private StackPane graphPane;
    private ScrollPane graphScrollPane;
    private VBox graphVBox;

    //button stuff
    private HBox buttonPane;
    private Button play;


    private boolean isPlaying;

    private HashMap<Location,Sensor> sensors;
    private HashMap<Location,Location> edges;
    private int largestX;
    private int largestY;
    private Agent a;

    public GUI(Stage stage, GUIState state){
        //set stage up
        this.stage = stage;
        this.stage.setTitle("Mobile Agents");
        this.state = state;

        //initialize the mane panes
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        logsPane = new ScrollPane();
        logsPane.setMaxHeight(200);
        logsPane.setMinHeight(100);
        logs = new VBox();
        logs.setId("logs");
        logs.minWidthProperty().bind(logsPane.widthProperty());

        //graph related
        //the scroll pane will be placed in root
        graphScrollPane = new ScrollPane();
        graphScrollPane.setMaxHeight(800);

        graphVBox = new VBox();
        graphPane = new StackPane();
        graphPane.setAlignment(Pos.CENTER);
        graph = new Canvas(600,600);
        gc = graph.getGraphicsContext2D();

        buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        play = new Button("Play");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playHandle();
            }
        });
        play.getStyleClass().add("play-button");

        //add nodes to containers
        graphScrollPane.setContent(graphPane);
        graphPane.minWidthProperty().bind(graphScrollPane.widthProperty());
        graphPane.minHeightProperty().bind(graphScrollPane.heightProperty());
        graphPane.getChildren().add(graph);
        //logs
        logsPane.setContent(logs);
        //button row
        buttonPane.getChildren().add(play);
        root.getChildren().addAll(graphScrollPane,logsPane,buttonPane);
        root.setVgrow(graphScrollPane,Priority.ALWAYS);

        //create scene and set style sheet
        scene = new Scene(root, 500, 500);
        scene.getStylesheets().add("mobileAgents/GUI.css");

        //display the stage
        stage.setScene(scene);
        stage.show();

        //TESTING
        edges = new HashMap<>();
        sensors = new HashMap<>();
        largestX = 0;
        largestY = 0;
        config = "node 0 0\n" +
                "node 3 4\n" +
                "node 2 3\n" +
                "node 5 5\n" +
                //"node 3 20\n" +
                //"node 10 14\n" +
                //"node 45 11\n" +
                //"node 1 0\n" +
                //"node 3 40\n" +
                //"node 20 3\n" +
                //"node 17 65\n" +
                "node 3 20\n" +
                "node 14 87\n" +
                "node 43 11\n" +
                "edge 0 0 2 3\n" +
                "edge 2 3 3 4\n" +
                "edge 3 4 5 5\n" +
                "edge 5 5 0 0\n" +
                "station 0 0\n" +
                //"fire 20 3\n" +
                //"fire 17 65\n" +
                //"fire 3 20\n" +
                //"fire 100 14\n" +
                "fire 5 5\n";
        initForrest();
        a = new Agent(getGuiSensorLoc(2,3));

        setSize();

        this.start();
        isPlaying = true;

    }

    private void setSize() {
        graph.setWidth(largestX*RADIUS+100);
        graph.setHeight(largestY*RADIUS+100);
        stage.setMinWidth(1400);
        stage.setMinHeight(1000);
    }

    private void initForrest() {
        String[] objects = config.split("\n");
        for(int i = 0; i< objects.length;i++){
            String[] entry = objects[i].split(" ");
            switch(entry[0]){
                case "node":{
                    if(entry.length != 3){
                        System.out.println("error parsing node");
                        break;
                    }
                    //convert string to integers.
                    int x = Integer.parseInt(entry[1]);
                    int y = Integer.parseInt(entry[2]);
                    //keep track of the largest x and y for sizing the canvas
                    if(x>largestX){largestX = x;}
                    if(y>largestY){largestY = y;}
                    Location l = getGuiSensorLoc(x,y);
                    sensors.put(l, new Sensor(l));
                    break;
                }
                case "edge": {
                    if(entry.length != 5){
                        System.out.println("error processing edge");
                        System.out.println(entry.length);
                        break;
                    }
                    //convert to integer valeus
                    int lx = Integer.parseInt(entry[1]);
                    int ly = Integer.parseInt(entry[2]);
                    int rx = Integer.parseInt(entry[3]);
                    int ry = Integer.parseInt(entry[4]);
                    //get offset location for GUI
                    Location left = getGuiEdgeLoc(lx,ly);
                    Location right = getGuiEdgeLoc(rx,ry);
                    edges.put(left, right);
                    break;
                }
                case "fire":{
                    if(entry.length != 3){
                        System.out.println("error parsing fire");
                        System.out.println(entry.length);
                        break;
                    }
                    //convert to integer
                    int x = Integer.parseInt(entry[1]);
                    int y = Integer.parseInt(entry[2]);
                    Location l = getGuiSensorLoc(x,y);
                    sensors.get(l).setState(Node.State.ONFIRE);
                    break;
                }
            }
        }

        System.out.println();
    }

    /**
     * This one will convert this to the offset of an edge which includes adding in hald the radius.
     * @param lx
     * @param ly
     * @return
     */
    private Location getGuiEdgeLoc(int lx, int ly) {
        return (new Location(lx*RADIUS+ (RADIUS/2),ly*RADIUS+ (RADIUS/2)));
    }

    /**
     * this translates the X and Y location of a node and translates it to the location represented in GUI
     *
     * offsets x and y by multiplying by the Radius +
     *
     * @param x
     * @param y
     * @return
     */
    private Location getGuiSensorLoc(int x, int y) {

        return (new Location(x*RADIUS,y*RADIUS));
    }

    private void playHandle() {
        if(isPlaying){
            isPlaying = false;
            this.stop();
        }else {
            this.start();
            isPlaying = true;
        }
    }

    long lastUpdate = 0;

    @Override
    public void handle(long now) {

        //if (now - lastUpdate >= 28_000_000) {

        //draw a black rectangle to clear the canvas
        gc.setGlobalAlpha(1.0);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        //gc.setFill(Color.rgb(0,59,0));
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,graph.getWidth(),graph.getHeight());

        //set defualts
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        //draw the edges first so everything else draws on top.
        gc.setStroke(Color.WHITE);
        for (Location l:edges.keySet()) {
            gc.strokeLine(l.getX(),l.getY(),edges.get(l).getX(),edges.get(l).getY());
        }

        //draw the sensors after the edges
        gc.setStroke(Color.BLACK);
        for (Location l:sensors.keySet() ) {
            sensors.get(l).updateAndRender(gc);
        }

        //TODO we should update and render the agents here.
        a.updateAndRender(gc);


        //Text test = new Text(String.valueOf(now));
        //test.setId("log-log");
        //logs.getChildren().add(test);

    }
}
