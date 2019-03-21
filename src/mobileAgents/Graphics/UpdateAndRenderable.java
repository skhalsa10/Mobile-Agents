package mobileAgents.Graphics;

import javafx.scene.canvas.GraphicsContext;

public interface UpdateAndRenderable {

    void update();

    void render(GraphicsContext gc);

    void updateAndRender(GraphicsContext gc);
}
