package mobileAgents.Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import mobileAgents.Location;

public class GUIAgent {
    private double x;
    private double y;
    private double radius;
    private final double RADIUSCAP = 21.0;
    private Paint color;
    private double offset;

    public GUIAgent(Location l){
        this.x = (double)l.getX();
        this.y = (double)l.getY();
        this.radius = 0;
        this.offset = 10;
        this.color = Color.LIME;
    }

    public void updateAndRender(GraphicsContext gc){
        if(radius >= RADIUSCAP){
            radius = 0;
            offset = 10;
        }
        else{
            radius += .4;
            offset -= .2;
        }
        render(gc);
    }

    private void render(GraphicsContext gc) {
        gc.setGlobalAlpha(1.0);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.setStroke(color);
        gc.setLineWidth(3);
        gc.strokeOval(x+offset,y+offset,radius,radius);
    }
}
