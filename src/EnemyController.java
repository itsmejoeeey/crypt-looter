import java.awt.*;
import javax.swing.*;

public class EnemyController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public double speed = 0.05;
    private double hitTimer = 0;

    CharacterView view;
    CharacterModel model;
    public BoxManager boxManager;
    public BoxController boxController;
    private EnemyAIController aiController;
    private AttackController attackController;

    public EnemyController(Point spawnPos, BoxController player, BoxManager _boxManager) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    model = new CharacterModel(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), 2);
                    view = new CharacterView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
                    boxController = new BoxController(model, view);
                    aiController = new EnemyAIController(boxController, player);
                    boxManager = _boxManager;
                    boxManager.entities.add(boxController);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public void update() {
        if(hitTimer <= 0) {
            double delta = deltaTime * speed;
            Vector2 aiVector = aiController.move();
            Vector2 moveVector = boxManager.move(new Vector2(aiVector.x, aiVector.y), view.getBounds(), boxController);
            x += moveVector.x * delta;
            y += moveVector.y * delta;
            view.moveWorld((int) x, (int) y);
        } else {
            hitTimer = hitTimer - deltaTime / 1000;
        }
        if(boxManager.detectAttackCollision(boxController, true)){
            hitTimer = 2;
            System.out.println("Hit!");
        }
    }

    public JPanel getView() {
        return view;
    }
}
