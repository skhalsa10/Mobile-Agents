package mobileAgents.Graphics;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;

public class Fire {

    private ArrayList<Particle> particles;
    private double x, y;
    private Emitter emitter;

    public Fire(double x, double y){
        particles  = new ArrayList<>();
        emitter = new FireEmitter();
        this.x = x;
        this.y = y;
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
