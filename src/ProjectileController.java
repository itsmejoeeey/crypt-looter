import javax.sound.midi.Receiver;
import javax.swing.*;
import java.awt.*;

public class ProjectileController extends AttackController {
    protected float x = 0;
    protected float y = 0;
    double deltaX = 0, deltaY = 0;
    public double deltaTime;

    BoxManager boxManager;
    ProjectileManager projectileManager;
    public BoxController archer;
    ItemView view;

    public ProjectileController(Rectangle spawnRect, Vector2 direction, BoxController archer, ProjectileManager projectileManager){
        super(spawnRect);
        this.projectileManager = projectileManager;
        deltaX = direction.x;
        deltaY = direction.y;
        x = spawnRect.x;
        y = spawnRect.y;
        this.archer = archer;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view = new ItemView(spawnRect);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }

    }
    public void update(){
        Vector2 outputMove = boxManager.move(new Vector2(deltaX, deltaY), view.getBounds(), archer);
        x += deltaX * deltaTime / 1000;
        y += deltaY * deltaTime / 1000;
        if(outputMove.x != deltaX || outputMove.y != deltaY){
            hitWorld();
        }
        Rectangle rectangle = view.getBounds();
        rectangle.setLocation((int) x, (int) y);
        view.bounds = rectangle;
        view.repaint();
    }

    public void hitWorld(){
        projectileManager.destoryProjectile(this);
    }
}
