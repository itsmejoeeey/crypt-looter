import javax.swing.*;
import java.awt.*;

public class ProjectileController {
    protected float x = 0;
    protected float y = 0;
    double deltaX = 0, deltaY = 0;
    int height = 0;

    BoxManager boxManager;
    ProjectileManager projectileManager;
    public BoxController archer;
    public ProjectileView view;

    public ProjectileController(Rectangle spawnRect, Vector2 direction, BoxController archer, ProjectileManager projectileManager){
        this.projectileManager = projectileManager;
        deltaX = direction.x;
        deltaY = direction.y;
        x = spawnRect.x;
        y = spawnRect.y;
        this.archer = archer;
        height = archer.getHeight();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view = new ProjectileView(spawnRect, false);
                    view.getBounds(spawnRect);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }

    }
    public void update(double deltaTime){
        if(boxManager.projectileMove(new Rectangle((int) x, (int) y, view.getWidth(), view.getHeight()), height)){
            hitWorld();
        }
        x += deltaX * deltaTime / 1000;
        y += deltaY * deltaTime / 1000;
        view.moveWorld((int) x, (int) y);
    }

    public void hitWorld(){
        projectileManager.destroyProjectile(this);
    }
}
