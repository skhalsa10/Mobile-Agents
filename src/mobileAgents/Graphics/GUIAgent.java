package mobileAgents.Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import mobileAgents.Location;

/**
 * this class represents the info needed to render an agent
 */
public class GUIAgent {
    private double x;
    private double y;
    private double radius;
    private final double RADIUSCAP = 21.0;
    private Paint color;
    private double offset;

    /**
     * initialize with a location translated for the GUI coordinates
     * @param l
     */
    public GUIAgent(Location l){
        this.x = (double)l.getX();
        this.y = (double)l.getY();
        this.radius = 0;
        this.offset = 10;
        this.color = Color.LIME;
    }

    /**
     * updates the state and then renders it after the update
     * @param gc graphics context to render to
     * @param scale will scale the size bigger or smaller
     */
    public synchronized void updateAndRender(GraphicsContext gc, double scale){
        if(radius >= RADIUSCAP){
            radius = 0;
            offset = 10;
        }
        else{
            radius += .4;
            offset -= .2;
        }
        render(gc, scale);
    }

    /**
     * renders the GUIAgent
     * @param gc graphics context to draw on
     * @param scale scale to size up or down
     */
    private synchronized void render(GraphicsContext gc, double scale) {
        gc.setGlobalAlpha(1.0);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        gc.setStroke(color);
        gc.setLineWidth(3);
        gc.strokeOval((x+offset)*scale,(y+offset)*scale,radius*scale,radius*scale);
    }
}
