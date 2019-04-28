package crypt_looter;

import java.awt.*;

// Class controls boss attack and movement
public class BossController extends EnemyController {
    private ProjectileManager projectileManager;
    public BossController(Point spawnPos, BoxManager _boxManager, SoundController _soundController, CharacterModel playerModel, ProjectileManager projectileManager){
        super(spawnPos, _boxManager, _soundController, playerModel);
        this.projectileManager = projectileManager;

        //Increases the health and reduces the speed of the boss
        model.maxHealth = 6;
        model.health = 6;
        speed = 0.03;

        //Boss will attack back if you provoke him
        aiController.attackDistance = 200;
    }

    @Override
    public void initView(Point spawnPos){
        view = new BossView(new Rectangle(spawnPos.x, spawnPos.y, 80, 80), model);
    }

    public void update(){
        super.update();
        //If the boss has taken damage chase the player
        if(model.health != model.maxHealth){
            aiController.attackDistance = 10000;
        }
    }

    public void attackDetection(){
        //Retrieve direction of attack
        Point aiVector = aiController.attackDir();

        int attackX = aiVector.x;
        int attackY = aiVector.y;

        setDirection(attackX, -attackY);

        //If the boss can attack then spawn projectiles at radial locations
        if(model.attackTimer <= 0 && aiController.canAttack(aiController.attackDistance, model.height)) {
            double segmentAngle = 2 * Math.PI / 8;
            for(int i =0; i < 8; i++){
                double x = Math.cos(segmentAngle * i) * 2;
                double y = Math.sin(segmentAngle * i) * 2;
                Rectangle transform = model.getTransform();
                projectileManager.spawnProjectile(new Point((int) (transform.x + transform.getWidth()/2), (int) (transform.y + transform.getHeight()/2)), new Vector2(Math.signum((int)x), Math.signum((int)y)), boxController, false);
            }
            model.attackBow = true;
            model.attackTimer = attackTime;
            aiController.attackDistance = 10000;
        }else {
            //Set hit box to inactive and the attacking bow to fox when not attacking
            model.attackBow = false;
            attackController.active = false;
            model.attackTimer -= deltaTime / 1000;
        }
    }
}
