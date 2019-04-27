import javax.swing.*;
import java.awt.*;

public class ProjectileController {
    protected float x = 0;
    protected float y = 0;
    double deltaX = 0, deltaY = 0;
    int height = 0;
    public int speed = 300;

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
                    view = new ProjectileView(spawnRect, getDirection((int) Math.signum(direction.x), -(int) Math.signum(direction.y)));
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
        x += deltaX * deltaTime * speed / 1000;
        y += deltaY * deltaTime * speed / 1000;
        view.moveWorld((int) x, (int) y);
    }

    protected int getDirection(int attackX, int attackY){
        if(attackX == 0 && attackY == 1){
            return 0;
        } else if(attackX == 1 && attackY == 1){
            return 7;
        } else if(attackX == 1 && attackY == 0){
            return 6;
        } else if(attackX == 1 && attackY == -1){
            return 5;
        } else if(attackX == 0 && attackY == -1){
            return 4;
        }else if(attackX == -1 && attackY == -1){
            return 3;
        }else if(attackX == -1 && attackY == 0){
            return 2;
        }else if(attackX == -1 && attackY == 1){
            return 1;
        }
        return 4;
    }
    public void hitWorld(){
        projectileManager.destroyProjectile(this);
    }
}
