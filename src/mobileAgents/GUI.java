package mobileAgents;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import mobileAgents.Graphics.GUIAgent;
import mobileAgents.messages.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;


public class GUI extends AnimationTimer {

    private final int RADIUS = 20;
    private final int SPEED4 = 200;
    private final int SPEED3 = 500;
    private final int SPEED2 = 1000;
    private final int SPEED1 = 2000;
    private int currentSpeed = SPEED3;
    private long lastUpdate = 0;
    private double scale = 1;

    private GUIState state;
    private String config;
    private boolean isInitialized;

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
    private Button zoomPlus;
    private Button zoomMinus;
    private Button speedPlus;
    private Button speedMinus;


    private boolean isPlaying;
    private boolean simIsOver;
    private Timer stateTimer;
    private HashMap<Location,Sensor> sensors;
    private HashMap<Location,GUIAgent> GUIAgents;
    private HashMap<Location, ArrayList<Location>> edges;
    private LinkedBlockingQueue<Text> textQueue;
    private int largestX;
    private int largestY;
    private Location baseLoc;
    private boolean basicRender;

    public GUI(Stage stage, GUIState state, boolean fire){
        basicRender = !fire;
        //set stage up
        this.stage = stage;
        this.stage.setTitle("Mobile Agents");
        this.state = state;

        //non javafx init
        textQueue = new LinkedBlockingQueue<>();

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
        graphScrollPane.setPannable(true);

        graphVBox = new VBox();
        graphPane = new StackPane();
        graphPane.setAlignment(Pos.CENTER);
        graph = new Canvas(600,600);
        gc = graph.getGraphicsContext2D();

        buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setSpacing(5);
        buttonPane.setPadding(new Insets(5, 5, 5, 5));

        play = new Button("Play or Pause");
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playHandle();
            }
        });
        play.getStyleClass().add("play-button");
        zoomPlus = new Button("Z+");
        zoomPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                zoomIn();
            }
        });
        zoomPlus.getStyleClass().add("zoom-button");
        zoomMinus= new Button("Z-");
        zoomMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                zoomOut();
            }
        });
        zoomMinus.getStyleClass().add("zoom-button");
        //lets to the speed buttons
        speedPlus = new Button("S+");
        speedMinus = new Button("S-");
        speedPlus.getStyleClass().add("speed-button");
        speedMinus.getStyleClass().add("speed-button");
        speedPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                speedUp();
            }
        });
        speedMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slowDown();
            }
        });


        //add nodes to containers
        graphScrollPane.setContent(graphPane);
        graphPane.minWidthProperty().bind(graphScrollPane.widthProperty());
        graphPane.minHeightProperty().bind(graphScrollPane.heightProperty());
        graphPane.getChildren().add(graph);
        //logs
        logsPane.setContent(logs);
        //button row
        buttonPane.getChildren().addAll(speedMinus,speedPlus,play,zoomMinus,zoomPlus);
        root.getChildren().addAll(graphScrollPane,logsPane,buttonPane);
        root.setVgrow(graphScrollPane,Priority.ALWAYS);

        //create scene and set style sheet
        scene = new Scene(root, 500, 500);
        scene.getStylesheets().add("mobileAgents/GUI.css");

        //display the stage
        stage.setScene(scene);
        stage.show();

        //start logic stuff
        simIsOver = false;
        edges = new HashMap<>();
        sensors = new HashMap<>();
        GUIAgents = new HashMap<>();
        isInitialized = false;
        largestX = 0;
        largestY = 0;
        config = getConfig();
        if(config != null) {
            initForrest();
        }


        if(isInitialized) {
            startStateTimer();
        }
        setSize();
        this.start();
        isPlaying = true;


    }

    private void slowDown() {
        if(currentSpeed == SPEED1){
            speedPlus.setDisable(false);
            return;
        }
        else if(currentSpeed == SPEED4){
            speedPlus.setDisable(false);
            currentSpeed = SPEED3;
        }
        else if(currentSpeed == SPEED3){
            speedPlus.setDisable(false);
            currentSpeed = SPEED2;
        }else if(currentSpeed == SPEED2){
            speedPlus.setDisable(false);
            speedMinus.setDisable(true);
            currentSpeed = SPEED1;
        }
        restartTimer();
    }

    private void speedUp() {
        if(currentSpeed == SPEED4){
            speedMinus.setDisable(false);
            return;
        }
        else if(currentSpeed == SPEED1){
            speedMinus.setDisable(false);
            currentSpeed = SPEED2;
        }
        else if(currentSpeed == SPEED2){
            speedMinus.setDisable(false);
            currentSpeed = SPEED3;
        }
        else if(currentSpeed == SPEED3){
            speedMinus.setDisable(false);
            currentSpeed = SPEED4;
            speedPlus.setDisable(true);
        }
        restartTimer();
    }

    private void restartTimer() {
        stateTimer.cancel();
        if(isInitialized) {
            startStateTimer();
        }
    }

    private void zoomIn() {
        scale += .3;
        setSize();
    }

    private void zoomOut() {
        scale -= .3;
        if(scale<= 1){
            scale = 1;
            setSize();
            return;
        }
        setSize();
    }

    /**
     * this method just generates fake state to test.
     */
    private void generateTestMessages() {
        Message c = new MessageGUIConfig("station 0 0\n" +
                "fire 8 1\n" +
                "node 0 0\n" +
                "edge 6 1 7 2\n" +
                "edge 7 2 7 0\n" +
                "edge 7 0 8 1\n" +
                "node 3 0\n" +
                "node 5 0\n" +
                "edge 3 0 5 0\n" +
                "edge 5 0 4 1\n" +
                "edge 5 0 6 1\n" +
                "edge 5 0 7 0\n" +
                "edge 4 1 6 1\n" +
                "node 4 1\n" +
                "node 7 2\n" +
                "edge 0 0 3 0\n" +
                "edge 1 1 0 0\n" +
                "edge 1 1 2 2\n" +
                "edge 2 2 3 0\n" +
                "node 6 1\n" +
                "edge 2 2 4 1\n" +
                "edge 8 1 7 2\n" +
                "node 7 0\n" +
                "node 8 1\n" +
                "node 1 1\n" +
                "node 2 2\n");
        state.putState(c);

    }

    /**
     * this will get the config string from the MessageGUIConfig
     * @return
     */
    private String getConfig() {
        Message test = state.peekState();
        if(test == null){
            return null;
        }
        if(test instanceof MessageGUIConfig){
            MessageGUIConfig m = (MessageGUIConfig) state.pollState();
            return m.readMessage();
        }else{
            return null;
        }
    }

    /**
     * this sets up the dimensions of the canvas and the stage/window
     */
    private void setSize() {
        graph.setWidth((largestX*RADIUS+100)*scale);
        graph.setHeight((largestY*RADIUS+100)*scale);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
    }

    /**
     * this assumes that the config file was already checked for correctness
     */
    private void initForrest() {
        String[] objects = config.split("\n");
        //System.out.println(config);
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
                    //lets also add a null value to the GUIAgents map
                    GUIAgents.put(l,null);
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
                    if(!edges.containsKey(left)) {
                        edges.put(left, new ArrayList<>());
                    }
                    edges.get(left).add(right);
                    break;
                }
                case "station":{
                    if(entry.length != 3){
                        System.out.println("error parsing fire");
                        System.out.println(entry.length);
                        break;
                    }
                    //convert to integer
                    int x = Integer.parseInt(entry[1]);
                    int y = Integer.parseInt(entry[2]);
                    baseLoc = getGuiSensorLoc(x,y);
                    break;
                }
            }
        }

        sensors.get(baseLoc).setAsBase();

        isInitialized = true;
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

    /**
     * this method /translates the location coordinates from a GUI representation to the original Simulation coordinates
     * @param GUILoc Location to convert
     * @return new Location with original SImulation coordinates
     */
    private Location sensorLocGUItoSim(Location GUILoc){
        return (new Location(GUILoc.getX()/RADIUS,GUILoc.getY()/RADIUS));
    }

    /**
     * This is the button handler method that is called when the play/pause button is pressed
     */
    private void playHandle() {
        if(isPlaying){
            isPlaying = false;
            this.stop();
            stateTimer.cancel();
        }else {
            this.start();
            isPlaying = true;
            if(isInitialized) {
                startStateTimer();
            }
        }
    }

    public void shutdown(){
        this.stop();
        stateTimer.cancel();
    }


    /**
     * this will start the State Timer
     */
    public synchronized void startStateTimer() {
        stateTimer = new Timer();

        stateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                processNextState();
            }
        }, 1000,currentSpeed);
    }

    /**
     * this will pop off a message from the shared state QUEUE and update the state accordingly
     */
    private synchronized void processNextState() {

        Message m = state.pollState();
        if(m == null) {
            System.out.println("handling null in queue");
            return;
        }
        if(m instanceof MessageGUIConfig){
            System.out.println("should nto be processing a config file this should already be configured");
            return;
        }
        if(m instanceof MessageGUIFire){
            MessageGUIFire f = (MessageGUIFire) m;
            //first we will change the fire node
            Location fl = getGuiSensorLoc(f.getFireLoc().getX(),f.getFireLoc().getY());
            sensors.get(fl).setState(Node.State.ONFIRE);
            //if an agent exists we need to delete it
            Text t2 = null;
            if(GUIAgents.containsKey(fl)){
                if(GUIAgents.get(fl)!= null) {
                    GUIAgents.replace(fl, null);
                    t2 = new Text("Agent at (" + f.getFireLoc().getX() + "," + f.getFireLoc().getY() + ") is now dead");
                    t2.setId("log-end");
                }
            }

            //now we need to set all the NEARFIRE
            for(Location l: f.getNearFireList()){
                sensors.get(getGuiSensorLoc(l.getX(),l.getY())).setState(Node.State.NEARFIRE);
            }
            Text t = new Text(f.readMessage());
            t.setId("log-fire");
            try {
                textQueue.put(t);
                if(t2 != null) {
                    textQueue.put(t2);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if(m instanceof MessageGUIAgent){
            MessageGUIAgent a = (MessageGUIAgent) m;
            //first process removing the move from
            if(a.getMovedFrom() != null){
                Location l = getGuiSensorLoc(a.getMovedFrom().getX(),a.getMovedFrom().getY());
                if(!GUIAgents.containsKey(l)){
                    System.out.println("GUI AGENT ERROR");
                }else{
                    GUIAgents.replace(l, null);
                }
            }
            //now deal with the new Agent
            Location l = getGuiSensorLoc(a.getAgentLoc().getX(),a.getAgentLoc().getY());
            GUIAgents.replace(l, new GUIAgent(l));
            //create text for log
            Text t = new Text(a.readMessage());
            t.setId("log-state");
            try {
                textQueue.put(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(m instanceof MessageGUICopyAgents){
            MessageGUICopyAgents a = (MessageGUICopyAgents) m;
            Location l2 = null;
            //loop through the list and add them to be rendered
            for(Location l: a.getNewAgentsList()){
                l2 = getGuiSensorLoc(l.getX(),l.getY());
                if(GUIAgents.containsKey(l2)){
                    GUIAgents.replace(l2, new GUIAgent(l2));
                }
                else{
                    System.out.println("Error processing KillAgent");
                }
            }

            Text t = new Text(a.readMessage());
            t.setId("log-state");
            try {
                textQueue.put(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(m instanceof MessageGUILog){
            Text t = new Text(m.readMessage());
            t.setId("log-log");
            try {
                textQueue.put(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(m instanceof MessageGUIEnd){
            simIsOver = true;
            Text t = new Text(m.readMessage());
            t.setId("log-end");
            try {
                textQueue.put(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }


    @Override
    public void handle(long now) {
        if(!isInitialized){
            System.out.println("not initialized");
            config = getConfig();
            if(config != null) {
                initForrest();
            }
            setSize();
            startStateTimer();
        }

        //this forces the animation to run a 60 frames a second roughly.
        //there are 1000 miliseconds in a second. if we divide this by 60 there are 16.666667 ms between frame draws
        if (now - lastUpdate >= 16_667_000) {

            //draw a black rectangle to clear the canvas
            gc.setGlobalAlpha(1.0);
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            //gc.setFill(Color.rgb(0,59,0));
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, graph.getWidth(), graph.getHeight());

            //set defualts
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);

            //draw the edges first so everything else draws on top.
            gc.setStroke(Color.WHITE);
            for (Location l1 : edges.keySet()) {
                for(Location l2: edges.get(l1)){
                    gc.strokeLine(l1.getX()*scale,l1.getY()*scale,l2.getX()*scale,l2.getY()*scale);
                }
                //gc.strokeLine(l.getX(), l.getY(), edges.get(l).getX(), edges.get(l).getY());
            }

            //draw the sensors after the edges
            gc.setStroke(Color.BLACK);
            for (Location l : sensors.keySet()) {
                sensors.get(l).updateAndRender(gc, basicRender , scale);
            }

            //TODO we should update and render the agents here.
            for (Location l:GUIAgents.keySet()) {
                if(GUIAgents.get(l) != null){
                    GUIAgents.get(l).updateAndRender(gc, scale);
                }

            }

            //update the log and add all the text object that are have been added to the textQueue
            Text t;
            while ((t = textQueue.poll()) != null) {
                logs.getChildren().add(t);
            }

            lastUpdate = now;
        }
    }
}
