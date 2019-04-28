package crypt_looter;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

//Spawns, destroys and updates all projectiles
public class ProjectileManager {
    public ArrayList<ProjectileController> projectiles = new ArrayList<>();
    public double deltaTime;
    private BoxManager boxManager;
    private WorldController worldController;
    private Dimension mapSize;
    public ProjectileManager (WorldController worldController, BoxManager boxManager, Dimension mapSize){
        this.worldController = worldController;
        this.boxManager = boxManager;
        boxManager.projectileManager = this;
        this.mapSize = mapSize;
    }

    public void update(){
        projectiles.trimToSize();
        for(int i= 0; i < projectiles.size(); i++){
            try {
                projectiles.get(i).update(deltaTime);
            } catch (IndexOutOfBoundsException e){
                System.out.println(e.getMessage());
                continue;
            }
        }
    }

    //Spawn projectile and add view to world view
    //Requires revalidate and repaint in UI thread
    public void spawnProjectile(Point spawn, Vector2 direction, BoxController archer, boolean player){
        ProjectileController projectile = new ProjectileController(new Rectangle(spawn.x, spawn.y, 20, 20),  direction, player, archer, this, mapSize);
        projectiles.add(projectile);
        projectile.boxManager = boxManager;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    worldController.getView().add(projectile.view);
                    worldController.getView().setComponentZOrder(projectile.view, 0);
                    worldController.getView().repaint();
                    worldController.getView().validate();
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            System.out.println(ex);
        }
    }


    //Destroys projectile removes and repaint in UI thread
    public void destroyProjectile(ProjectileController projectileController){
        projectiles.remove(projectileController);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    worldController.getView().remove(projectileController.view);
                    worldController.getView().repaint();
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {

        }
    }
}
