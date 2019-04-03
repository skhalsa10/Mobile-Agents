package mobileAgents.Graphics;

import java.util.ArrayList;


/**
 * interface that defines the behavior to emits partcles and returns them in a list
 */
public interface Emitter {

    ArrayList<Particle> emit(double x, double y);
}
