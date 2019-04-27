import java.awt.*;
import javax.swing.*;

public class EnemyController extends CharacterController {
    public double speed = 0.05;
    private double stunTimer = 0;

    private EnemyAIController aiController;
    public AttackController attackController;

    public EnemyController(Point spawnPos, BoxController player, BoxManager _boxManager, SoundController _soundController) {
        super(spawnPos, _soundController, _boxManager);
        view = new EnemyView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
        boxController = new BoxController(model, view);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    aiController = new EnemyAIController(boxController, player);
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

    public void update() {
        if(stunTimer <= 0) {
            groundMovement();
            attackDetection();
        } else {
            stunTimer = stunTimer - deltaTime / 1000;
            model.walking = false;
        }
        if(boxManager.detectPlayerAttackCollision(boxController)){
            stunTimer = 2;
            soundController.playEnemyHit();
            model.decreaseHealth(1);
        }
        if(boxManager.detectPlayerProjectileCollision(boxController)){
            stunTimer = 1;
            soundController.playEnemyHit();
            model.decreaseHealth(1);
        }
        view.deltaTime = deltaTime;
        view.update();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view.moveWorld((int) x, (int) y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    void groundMovement(){
        double delta = deltaTime * speed;
        Vector2 aiVector = aiController.move(model.height);
        Vector2 moveVector = boxManager.move(new Vector2(aiVector.x, aiVector.y), view.getBounds(), boxController, true);
        x += moveVector.x * delta;
        y += moveVector.y * delta;
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

        Rectangle attackRectangle = new Rectangle(new Rectangle(view.getBounds().x + attackX * view.getBounds().height + (view.getBounds().height - attackController.getHeight())/2, view.getBounds().y + attackY * view.getBounds().width + (view.getBounds().width - attackController.getWidth())/2, attackController.getWidth(), attackController.getHeight()));
        attackController.updateHitBox(attackRectangle, 0);
        attackController.attackHeight = model.height;

        if(model.attackTimer <= 0 && aiController.canAttack(60, model.height)) {
            attackController.active = true;
            model.attackDagger = true;
            if(model.attackDagger){
                model.attackTimer = 2;
            }
        } else {
            model.attackDagger = false;
            attackController.active = false;
            model.attackTimer -= deltaTime / 1000;
        }
    }

}
