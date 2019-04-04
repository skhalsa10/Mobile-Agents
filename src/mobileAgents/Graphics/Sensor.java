package mobileAgents.Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import mobileAgents.Location;
import mobileAgents.Node;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * this class is used by the GUI to control the render state of a sensor.
 *
 * Depending on what state it is in it will draw as a specific color or start the fire emitter
 */
public class Sensor{

    private ArrayList<Particle> particles;
    private double x, y;
    private Location location;
    private Emitter emitter;
    private Node.State state;
    private Paint color;
    private int radius;

    /**
     * initialize with the locationof where it exists this location should be translated to the GUI representation.
     * @param location of where the sensor will be rendered on the canvas
     */
    public Sensor(Location location){
        particles  = new ArrayList<>();
        emitter = new FireEmitter();
        this.x = (double)location.getX();
        this.y = (double)location.getY();
        this.location = location;
        state = Node.State.NOTONFIRE;
        color = Color.BLUE;
        radius = 20;
    }

    /**
     *
     * @param state the state the sensor should be changed too.
     */
    public synchronized void setState(Node.State state){
        switch(state){
            case NOTONFIRE:{
                color = Color.BLUE;
                break;
            }
            case NEARFIRE:{
                color = Color.YELLOW;
                break;
            }
            case ONFIRE:{
                color = Color.RED;
                break;
            }
            case DEAD:{
                color = Color.BLACK;
                break;
            }
        }
        this.state = state;
    }

    /**
     *
     * @return the location of the sensor
     */
    public Location getLocation() {
        return location;
    }

    /**
     * this method changes to color to green.
     */
    public synchronized void setAsBase(){
        color = Color.GREEN;
    }

    /**
     * this method will first step through the animation frame and then it renders the new state
     * @param gc the graphics context that we will draw onto
     * @param basicRender whethor we rendor an onfire sensor as a red circle or a fire animation
     * @param scale this will be used to scale the size up or down
     */
    public synchronized void updateAndRender(GraphicsContext gc, boolean basicRender, double scale){

        if(!basicRender && state == Node.State.ONFIRE) {

            particles.addAll(emitter.emit(x, y));

            Iterator<Particle> it = particles.iterator();

            while (it.hasNext()) {
                Particle p = it.next();
                p.update();

                if (!p.isAlive()) {
                    it.remove();
                    continue;
                }

                p.render(gc,scale);
            }
        }else{
            gc.setGlobalAlpha(1.0);
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            gc.setFill(color);
            gc.fillOval(x*scale,y*scale,radius*scale,radius*scale);
        }

    }
}
