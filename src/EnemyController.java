import java.awt.*;
import javax.swing.*;

public class EnemyController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public float speed = 0.05f;

    CharacterView view;
    Character model;
    public BoxManager boxManager;
    public BoxController boxController;
    private EnemyAIController aiController;

    public EnemyController(Point spawnPos, BoxController player, BoxManager _boxManager) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    model = new Character(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), 2);
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
        double delta = deltaTime * speed;
        System.out.println(aiController.move().x + "-" + aiController.move().y);
        Vector2 aiVector = aiController.move();
        Vector2 moveVector =  boxManager.move(new Vector2((float) (aiVector.x), (float) (aiVector.y)) , view.getBounds(), boxController);
        x += moveVector.x * delta;
        y += moveVector.y * delta;
        view.moveWorld((int) x, (int) y);
    }

    public JPanel getView() {
        return view;
    }
}
