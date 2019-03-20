package mobileAgents;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mobileAgents.Graphics.Fire;


public class GUI extends AnimationTimer {
    private Stage stage;
    private GUIState state;

    private Scene scene;
    private VBox root;
    private ScrollPane logsPane;
    private VBox logs;
    private Canvas graph;
    private GraphicsContext gc;
    private Pane graphPane;
    private HBox buttonPane;
    private Button play;

    Fire f;
    Fire f2;

    public GUI(Stage stage, GUIState state){
        this.stage = stage;
        //stage.setMinWidth(400);
        //stage.setMinHeight(400);
        this.state = state;

        //initialize the mane panes
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        logsPane = new ScrollPane();
        logsPane.setMaxHeight(200);
        logs = new VBox();
        logs.setId("logs");
        logs.minWidthProperty().bind(logsPane.widthProperty());
        graphPane = new Pane();
        graph = new Canvas(600,600);
        gc = graph.getGraphicsContext2D();
        buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        play = new Button("Play");

        //add nodes to containers
        logsPane.setContent(logs);
        graphPane.getChildren().add(graph);
        buttonPane.getChildren().add(play);
        root.getChildren().addAll(graphPane,logsPane,buttonPane);

        //create scene and set style sheet
        scene = new Scene(root, 500, 500);
        scene.getStylesheets().add("mobileAgents/GUI.css");

        //display the stage
        stage.setScene(scene);
        stage.show();

        f = new Fire(300,300);
        f2 = new Fire(100,300);


        this.start();

    }

    long lastUpdate = 0;

    @Override
    public void handle(long now) {

        //if (now - lastUpdate >= 28_000_000) {
            gc.setGlobalAlpha(1.0);
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            gc.setFill(Color.BLACK);
            gc.fillRect(0,0,600,600);
            f.updateAndRender(gc);
            f2.updateAndRender(gc);

            Text test = new Text(String.valueOf(lastUpdate));
            test.setId("log-log");


            logs.getChildren().add(test);
            //lastUpdate = now ;
        //}


    }
}
