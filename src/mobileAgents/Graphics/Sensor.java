package mobileAgents.Graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import mobileAgents.Location;
import mobileAgents.Node;

import java.util.ArrayList;
import java.util.Iterator;

public class Sensor{

    private ArrayList<Particle> particles;
    private double x, y;
    private Location location;
    private Emitter emitter;
    private Node.State state;
    private Paint color;
    private int radius;

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

    public void setState(Node.State state){
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

    public Location getLocation() {
        return location;
    }

    public void update(){
        if (state == Node.State.ONFIRE){
                particles.addAll(emitter.emit(x, y));
                Iterator<Particle> it = particles.iterator();

                while (it.hasNext()) {
                    Particle p = it.next();
                    p.update();

                    if (!p.isAlive()) {
                        it.remove();
                    }
                }
            }

    }

    public void render(GraphicsContext gc){
        if(state == Node.State.ONFIRE) {
            Iterator<Particle> it = particles.iterator();

            while (it.hasNext()) {
                Particle p = it.next();
                p.render(gc);
            }
        }else{
            gc.setFill(color);
            gc.fillOval(x,y,radius,radius);
        }
    }

    public void updateAndRender(GraphicsContext gc){

        if(state == Node.State.ONFIRE) {

            particles.addAll(emitter.emit(x, y));

            Iterator<Particle> it = particles.iterator();

            while (it.hasNext()) {
                Particle p = it.next();
                p.update();

                if (!p.isAlive()) {
                    it.remove();
                    continue;
                }

                p.render(gc);
            }
        }else{
            gc.setGlobalAlpha(1.0);
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            gc.setFill(color);
            gc.fillOval(x,y,radius,radius);
        }

    }
}
