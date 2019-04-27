import javax.sound.midi.Receiver;
import javax.swing.*;
import java.awt.*;

public class ProjectileController {
    protected float x = 0;
    protected float y = 0;
    double deltaX = 0, deltaY = 0;
    public double deltaTime;

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
    public void update(){
        Vector2 outputMove = boxManager.move(new Vector2(deltaX, deltaY), view.getBounds(), archer);
        x += deltaX * deltaTime / 1000;
        y += deltaY * deltaTime / 1000;
        if(outputMove.x != deltaX || outputMove.y != deltaY){
            hitWorld();
        }
        view.moveWorld((int) x, (int) y);
        //Rectangle rectangle = view.getBounds();
        //rectangle.setLocation((int) x, (int) y);
        //view.setLocation((int) x, (int) y);
        //view.setBounds(new Rectangle((int) x, (int) y, view.getBounds().width, view.getBounds().height));
        //view.repaint();
    }

    public void hitWorld(){
        projectileManager.destoryProjectile(this);
    }
}
