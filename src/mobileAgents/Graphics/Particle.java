package mobileAgents.Graphics;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;

/**
 * this will be a particle that is generated in a particle emmiter
 *
 *  Thanks https://www.youtube.com/watch?v=vLcJRm6Y72U
 */
public class Particle {

    private double x;
    private double y;
    private Point2D velocity;
    private double radius;
    private double life = 1.0;
    private double decay;
    private Paint color;
    private BlendMode blendMode;

    /**
     * this has a lot of parameters that represent the property of how the particle behaves
     * @param x x location
     * @param y y location
     * @param v velocity
     * @param r how big the radius of the circle is
     * @param decay how fast it decays
     * @param color the color of the particle
     * @param bm blend mode
     */
    public Particle(double x, double y, Point2D v, double r, double decay, Paint color, BlendMode bm) {
        this.x = x;
        this.y = y;
        this.velocity = v;
        this.radius = r;
        this.decay = 0.0167 / decay;
        this.color = color;
        this.blendMode = bm;
    }

    /**
     * update new location of this particle  my adding the velocity and decaying the life
     */
    public synchronized void update(){
        x += velocity.getX();
        y += velocity.getY();

        life -= decay;
    }

    /**
     * render the particle
     * @param gc graphics context to draw onto
     * @param scale the scale to scale up or down
     */
    public synchronized void render(GraphicsContext gc, double scale){
        gc.setGlobalAlpha(life);
        gc.setGlobalBlendMode(blendMode);
        gc.setFill(color);
        gc.fillOval(x*scale,y*scale,radius*scale,radius*scale);
    }

    /**
     *
     * @return true if life is greater than 0
     */
    public synchronized boolean isAlive(){
        return life > 0;
    }


}
