package mobileAgents.Graphics;

import javafx.scene.canvas.GraphicsContext;
import mobileAgents.Location;

import java.util.ArrayList;
import java.util.Iterator;

public class Fire implements UpdateAndRenderable{

    private ArrayList<Particle> particles;
    private double x, y;
    private Emitter emitter;

    public Fire(Location location){
        particles  = new ArrayList<>();
        emitter = new FireEmitter();
        this.x = (double)location.getX();
        this.y = (double)location.getY();
    }

    public void update(){
        particles.addAll(emitter.emit(x,y));
        Iterator<Particle> it = particles.iterator();

        while(it.hasNext()){
            Particle p = it.next();
            p.update();

            if(!p.isAlive()){
                it.remove();
            }
        }
    }

    public void render(GraphicsContext gc){
        Iterator<Particle> it = particles.iterator();

        while(it.hasNext()){
            Particle p = it.next();
            p.render(gc);
        }
    }

    public void updateAndRender(GraphicsContext gc){
        particles.addAll(emitter.emit(x,y));

        Iterator<Particle> it = particles.iterator();

        while(it.hasNext()){
            Particle p = it.next();
            p.update();

            if(!p.isAlive()){
                it.remove();
                continue;
            }

            p.render(gc);
        }
    }
}
