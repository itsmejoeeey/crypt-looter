import jdk.jfr.Enabled;

import java.awt.*;
import java.util.ArrayList;

public class EnemyManager {
    private ArrayList<EnemyController> enemies = new ArrayList<>();
    public double deltaTime;
    int basicEnemyCount = 0;
    public EnemyManager (WorldController worldController, World world, BoxController playerBox, BoxManager boxManager, SoundController sound, ProjectileManager projectileManager){
        for(int i= 0; i < world.enemiesNormal.size(); i++){
            enemies.add(new EnemyController(new Point((int)world.enemiesNormal.get(i).x * world.tileSize,(int) world.enemiesNormal.get(i).y * world.tileSize), playerBox, boxManager, sound, worldController.parent.character.model));
            worldController.getView().add(enemies.get(i).attackController);
            worldController.getView().add(enemies.get(i).getView());
            basicEnemyCount++;
        }

        CharacterModel[] bossModels = new CharacterModel[world.enemiesBoss.size()];
        for(int i= 0; i < world.enemiesBoss.size(); i++){
            enemies.add(new BossController(new Point((int)world.enemiesBoss.get(i).x * world.tileSize,(int) world.enemiesBoss.get(i).y * world.tileSize), playerBox, boxManager, sound, worldController.parent.character.model, projectileManager));
            worldController.getView().add(enemies.get(i+basicEnemyCount).attackController);
            worldController.getView().add(enemies.get(i+basicEnemyCount).getView());
            bossModels[i] = enemies.get(i+basicEnemyCount).model;
        }
    }

    public CharacterModel[] getBossModels (){
        CharacterModel[] bossModels = new CharacterModel[enemies.size() - basicEnemyCount];
        for(int i= 0; i < enemies.size()-basicEnemyCount; i++) {
            bossModels[i] = enemies.get(i+basicEnemyCount).model;
        }
        return bossModels;
    }

    public void update(){
        for(int i= 0; i < enemies.size(); i++){
            enemies.get(i).update();
            enemies.get(i).deltaTime = deltaTime;
        }
    }
}
