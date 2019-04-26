import jdk.jfr.Enabled;

import java.awt.*;
import java.util.ArrayList;

public class EnemyManager {
    private ArrayList<EnemyController> enemies = new ArrayList<>();
    public double deltaTime;
    public EnemyManager (WorldController worldController, World world, BoxController playerBox, BoxManager boxManager, SoundController sound){
        for(int i= 0; i < world.enemiesNormal.size(); i++){
            enemies.add(new EnemyController(new Point(1100, 500), playerBox, boxManager, sound));
            worldController.getView().add(enemies.get(i).attackController);
            worldController.getView().add(enemies.get(i).getView());
        }
    }

    public void update(){
        for(int i= 0; i < enemies.size(); i++){
            enemies.get(i).update();
            enemies.get(i).deltaTime = deltaTime;
        }
    }
}
