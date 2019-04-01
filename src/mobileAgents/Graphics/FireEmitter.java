package mobileAgents.Graphics;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class FireEmitter implements Emitter {
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
