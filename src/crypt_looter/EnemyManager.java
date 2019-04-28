package crypt_looter;

import java.awt.*;
import java.util.ArrayList;

//Class spawns list of enemies and updates the each frame
public class EnemyManager {
    private ArrayList<EnemyController> enemies = new ArrayList<>();
    public double deltaTime;
    int basicEnemyCount = 0;

    //Spawn all the enemies and bosses, add them to the world view to be rendered
    public EnemyManager (WorldController worldController, World world, BoxManager boxManager, SoundController sound, ProjectileManager projectileManager){
        for(int i= 0; i < world.enemiesNormal.size(); i++){
            enemies.add(new EnemyController(new Point((int)world.enemiesNormal.get(i).x * world.tileSize,(int) world.enemiesNormal.get(i).y * world.tileSize), boxManager, sound, worldController.parent.character.model));
            worldController.getView().add(enemies.get(i).attackController);
            worldController.getView().add(enemies.get(i).getView());
            basicEnemyCount++;
        }

        //Gets list of character models for the bosses to the final chest canTrigger()
        CharacterModel[] bossModels = new CharacterModel[world.enemiesBoss.size()];
        for(int i= 0; i < world.enemiesBoss.size(); i++){
            enemies.add(new BossController(new Point((int)world.enemiesBoss.get(i).x * world.tileSize,(int) world.enemiesBoss.get(i).y * world.tileSize), boxManager, sound, worldController.parent.character.model, projectileManager));
            worldController.getView().add(enemies.get(i+basicEnemyCount).attackController);
            worldController.getView().add(enemies.get(i+basicEnemyCount).getView());
            bossModels[i] = enemies.get(i+basicEnemyCount).model;
        }
    }

    //Gets all the boss models as character models
    public CharacterModel[] getBossModels (){
        CharacterModel[] bossModels = new CharacterModel[enemies.size() - basicEnemyCount];
        for(int i= 0; i < enemies.size()-basicEnemyCount; i++) {
            bossModels[i] = enemies.get(i+basicEnemyCount).model;
        }
        return bossModels;
    }

    //Updates all enemies and passes deltaTime through to the enemy
    public void update(){
        for(int i= 0; i < enemies.size(); i++){
            enemies.get(i).update();
            enemies.get(i).deltaTime = deltaTime;
        }
    }
}
