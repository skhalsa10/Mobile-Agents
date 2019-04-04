package mobileAgents.Graphics;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * this emmits particles that look like a fire;
 */
public class FireEmitter implements Emitter {

    /**
     * emits particles
     *
     * @param x x to emmit particle from
     * @param y y to emit particle at
     * @return a list of particles with random velocity
     */
    @Override
    public ArrayList<Particle> emit(double x, double y) {

        ArrayList<Particle> particles = new ArrayList<>();

        int numP = 1;

        //new Point2D((Math.random()-.5)*.8,Math.random()*-1.5)
        //Color.rgb(240,66,0)

        for(int i = 0; i < numP;i++){
            Particle p = new Particle(x,y,new Point2D((Math.random()-.5)*.8,(Math.random())*-2),
                    20,.3, Color.rgb(240,26,10), BlendMode.ADD);
            particles.add(p);
        }

        return particles;
    }
}
