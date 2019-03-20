package mobileAgents.Graphics;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;

/**
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

    public Particle(double x, double y, Point2D v, double r, double decay, Paint color, BlendMode bm) {
        this.x = x;
        this.y = y;
        this.velocity = v;
        this.radius = r;
        this.decay = 0.0167 / decay;
        this.color = color;
        this.blendMode = bm;
    }

    public void update(){
        x += velocity.getX();
        y += velocity.getY();

        life -= decay;
    }

    public void render(GraphicsContext gc){
        gc.setGlobalAlpha(life);
        gc.setGlobalBlendMode(blendMode);
        gc.setFill(color);
        gc.fillOval(x,y,radius,radius);
    }

    public boolean isAlive(){
        return life > 0;
    }


}
