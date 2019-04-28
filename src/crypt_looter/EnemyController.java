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
        if(!model.dead) {
            if (stunTimer <= 0) {
                groundMovement();
                attackDetection();
            } else {
                stunTimer = stunTimer - deltaTime / 1000;
                model.walking = false;
            }
            if (boxManager.detectPlayerAttackCollision(boxController)) {
                stunTimer = 2;
                soundController.playEnemyHit();
                model.decreaseHealth(1);
            }
            if (boxManager.detectPlayerProjectileCollision(boxController)) {
                stunTimer = 1;
                soundController.playEnemyHit();
                model.decreaseHealth(1);
            }
            if(model.dead){
                Die();
            }
        } else{
            model.walking = false;
            model.attackDagger = false;
            model.attackBow = false;
            model.health = 0;
            boxManager.entities.remove(boxController);
            boxManager.enemyAttacks.remove(attackController);
        }
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
        double delta = deltaTime * speed;
        deltaX = 0;
        deltaY = 0;
        Vector2 aiVector = aiController.move(model.height);
        Vector2 moveVector = boxManager.move(new Vector2(aiVector.x, aiVector.y), model.getTransform(), boxController);
        deltaX += moveVector.x * delta;
        deltaY += moveVector.y * delta;
        if(moveVector.x > 0 || moveVector.y > 0){
            model.walking = true;
        } else {
            model.walking = false;
        }
    }

    void attackDetection(){
        Point aiVector = aiController.attackDir();

        int attackX = aiVector.x;
        int attackY = aiVector.y;

        setDirection(attackX, -attackY);

        Rectangle transform = model.getTransform();
        Rectangle attackRectangle = new Rectangle(new Rectangle(transform.x + attackX * transform.height + (transform.height - attackController.getHeight())/2, transform.y + attackY * transform.width + (transform.width - attackController.getWidth())/2, attackController.getWidth(), attackController.getHeight()));
        attackController.updateHitBox(attackRectangle, 0);
        attackController.attackHeight = model.height;

        if(model.attackTimer <= 0 && aiController.canAttack(60, model.height)) {
            attackController.active = true;
            model.attackDagger = true;
            if(model.attackDagger){
                model.attackTimer = attackTime;
            }
        } else {
            model.attackDagger = false;
            attackController.active = false;
            model.attackTimer -= deltaTime / 1000;
        }
    }

}
