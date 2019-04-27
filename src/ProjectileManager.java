import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ProjectileManager {
    public ArrayList<ProjectileController> projectiles = new ArrayList<>();
    public double deltaTime;
    private BoxManager boxManager;
    private WorldController worldController;
    public ProjectileManager (WorldController worldController, BoxManager boxManager){
        this.worldController = worldController;
        this.boxManager = boxManager;
        boxManager.projectileManager = this;
    }

    public void update(){
        //System.out.println(projectiles.size());
        for(int i= 0; i < projectiles.size(); i++){
            try {
                projectiles.get(i).update();
                projectiles.get(i).deltaTime = deltaTime;
            } catch (IndexOutOfBoundsException e){
                System.out.println(e.getMessage());
                continue;
            }
        }
    }

    public void spawnProjectile(Rectangle spawnRect, Vector2 direction, BoxController archer){
        System.out.println("Fire!");
        ProjectileController projectile = new ProjectileController(spawnRect, direction, archer, this);
        projectiles.add(projectile);
        projectile.boxManager = boxManager;
        worldController.getView().add(projectile.view);
        //worldController.getView().revalidate();
        //worldController.getView().repaint();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {

        }
    }

    public void destoryProjectile(ProjectileController projectileController){
        projectiles.remove(projectileController);
        worldController.getView().remove(projectileController.view);
        worldController.getView().repaint();
    }
}
