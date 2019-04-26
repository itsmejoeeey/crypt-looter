import java.awt.*;
import javax.swing.*;

public class EnemyController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public double speed = 0.05;
    private double stunTimer = 0;

    public boolean enemyDead = false;

    CharacterView view;
    CharacterModel model;
    public BoxManager boxManager;
    public BoxController boxController;
    private EnemyAIController aiController;
    public AttackController attackController;

    public EnemyController(Point spawnPos, BoxController player, BoxManager _boxManager) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    model = new CharacterModel(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), 2);
                    view = new CharacterView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
                    boxController = new BoxController(model, view);
                    aiController = new EnemyAIController(boxController, player);
                    attackController = new AttackController(new Rectangle(spawnPos.x, spawnPos.y, 20,20));
                    model.direction = 4;
                    boxManager = _boxManager;
                    boxManager.entities.add(boxController);
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
        }
        if(boxManager.detectAttackCollision(boxController, true)){
            stunTimer = 2;
            System.out.println("Hit!");
        }
    }

    void groundMovement(){
        double delta = deltaTime * speed;
        Vector2 aiVector = aiController.move();
        Vector2 moveVector = boxManager.move(new Vector2(aiVector.x, aiVector.y), view.getBounds(), boxController);
        x += moveVector.x * delta;
        y += moveVector.y * delta;
        view.moveWorld((int) x, (int) y);
    }

    void attackDetection(){
        Point aiVector = aiController.attackDir();

        int attackX = aiVector.x;
        int attackY = aiVector.y;

        if(attackX == 0 && attackY == 1){
            model.direction = 0;
        } else if(attackX == 1 && attackY == 1){
            model.direction = 7;
        } else if(attackX == 1 && attackY == 0){
            model.direction = 6;
        } else if(attackX == 1 && attackY == -1){
            model.direction = 5;
        } else if(attackX == 0 && attackY == -1){
            model.direction = 4;
        }else if(attackX == -1 && attackY == -1){
            model.direction = 3;
        }else if(attackX == -1 && attackY == 0){
            model.direction = 2;
        }else if(attackX == -1 && attackY == 1){
            model.direction = 1;
        }


        Rectangle attackRectangle = new Rectangle(new Rectangle(view.getBounds().x + attackX * view.getBounds().height + (view.getBounds().height - attackController.getHeight())/2, view.getBounds().y + attackY * view.getBounds().width + (view.getBounds().width - attackController.getWidth())/2, attackController.getWidth(), attackController.getHeight()));
        attackController.updateHitBox(attackRectangle, 0);

        if(model.attackTimer <= 0) {
            attackController.active = (KeyStates.attackKey.changedSinceLastChecked() && KeyStates.attackKey.keyState());
            model.attackDagger = attackController.active;
            if(model.attackDagger){
                model.attackTimer = 1;
            }
        } else {
            model.attackDagger = false;
            attackController.active = false;
            model.attackTimer -= deltaTime / 1000;
        }
    }

    public JPanel getView() {
        return view;
    }
}
