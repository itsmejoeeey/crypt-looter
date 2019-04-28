package crypt_looter;

import java.awt.*;
import javax.swing.*;

public class EnemyController extends CharacterController {
    public double speed = 0.05;
    private double stunTimer = 0;
    protected double stunTime = 2;
    protected double attackTime = 2;

    protected EnemyAIController aiController;
    public AttackController attackController;
    protected CharacterModel playerModel;

    public EnemyController(Point spawnPos, BoxManager _boxManager, SoundController _soundController, CharacterModel playerModel) {
        super(spawnPos, _soundController, _boxManager);
        //view = new EnemyView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), playerModel);
        initView(spawnPos);
        this.playerModel = playerModel;
        boxController = new BoxController(model, view);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    aiController = new EnemyAIController(model, playerModel);
                    attackController = new AttackController(new Rectangle(spawnPos.x, spawnPos.y, 20,20));
                    model.direction = 4;
                    boxManager.entities.add(boxController);
                    boxManager.enemyAttacks.add(attackController);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public void initView(Point spawnPos){
        view = new EnemyView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
    }

    public void update() {
        deltaX = 0;
        deltaY = 0;
        if(!model.dead) {
            //If the enemy is not stunned then move and attack
            if (stunTimer <= 0) {
                groundMovement();
                attackDetection();
            } else {
                //If stunned decrease stun timer and don't walk
                stunTimer = stunTimer - deltaTime / 1000;
                model.walking = false;
            }
            //Detects the player attacks, apply stun, play sound and decrease health
            if (boxManager.detectPlayerAttackCollision(boxController)) {
                stunTimer = stunTime;
                soundController.playEnemyHit();
                model.decreaseHealth(1);
            }
            //Detects the player projectiles, stuns for half the length of attacks
            if (boxManager.detectPlayerProjectileCollision(boxController)) {
                stunTimer = stunTime / 2;
                soundController.playEnemyHit();
                model.decreaseHealth(1);
            }
            //Trigger die function when killed
            if(model.dead){
                Die();
            }
        } else{
            //Sets values for when the enemy is dead
            model.walking = false;
            model.attackDagger = false;
            model.attackBow = false;
            model.health = 0;
            boxManager.entities.remove(boxController);
            boxManager.enemyAttacks.remove(attackController);
        }
        //Apply the movement calculated to the view
        view.deltaTime = deltaTime;
        view.update();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    model.moveWorld(deltaX, deltaY);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    protected void Die(){
        playerModel.increaseScore(400);
    }

    void groundMovement(){
        //Get the speed of enemy
        double delta = deltaTime * speed;
        deltaX = 0;
        deltaY = 0;

        //Get the direction of movement and check if it is colliding
        Vector2 aiVector = aiController.move(model.height);
        Vector2 moveVector = boxManager.move(new Vector2(aiVector.x, aiVector.y), model.getTransform(), boxController);

        //Apply movement vector to the delta
        deltaX += moveVector.x * delta;
        deltaY += moveVector.y * delta;

        //If the movement vector is 0 then the enemy is walking
        if(moveVector.x > 0 || moveVector.y > 0){
            model.walking = true;
        } else {
            model.walking = false;
        }
    }

    void attackDetection(){
        //Calculates the direction the enemy should be facing and sets the direction
        Point aiVector = aiController.attackDir();

        int attackX = aiVector.x;
        int attackY = aiVector.y;

        setDirection(attackX, -attackY);

        //Updates the enemy hit box based on its transform and height
        Rectangle transform = model.getTransform();
        Rectangle attackRectangle = new Rectangle(new Rectangle(transform.x + attackX * transform.height + (transform.height - attackController.getHeight())/2, transform.y + attackY * transform.width + (transform.width - attackController.getWidth())/2, attackController.getWidth(), attackController.getHeight()));
        attackController.updateHitBox(attackRectangle, 0);
        attackController.attackHeight = model.height;

        //If it can attack then attack the player if not reduce the cooldown on timer and set hit box active to false
        if(model.attackTimer <= 0 && aiController.canAttack(70, model.height)) {
            attackController.active = true;
            model.attackDagger = true;
            model.attackTimer = attackTime;
        } else {
            model.attackDagger = false;
            attackController.active = false;
            model.attackTimer -= deltaTime / 1000;
        }
    }

}
