package mobileAgents.Graphics;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class FireEmitter implements Emitter {
    @Override
    public ArrayList<Particle> emit(double x, double y) {

        ArrayList<Particle> particles = new ArrayList<>();

        int numP = 2;

        //new Point2D((Math.random()-.5)*.8,Math.random()*-1.5)

        for(int i = 0; i < numP;i++){
            Particle p = new Particle(x,y,new Point2D((Math.random()-.5)*.8,(Math.random())*-2),
                    10,.7, Color.rgb(190,90,45), BlendMode.ADD);
            particles.add(p);
        }

        return particles;
    }
}
