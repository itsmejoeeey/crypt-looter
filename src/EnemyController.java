import java.awt.*;
import javax.swing.*;

public class EnemyController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public float speed = 0.2f;

    CharacterView view;
    Character model;
    public BoxManager boxManager;
    public BoxController boxController;

    public EnemyController(Point spawnPos, BoxManager _boxManager) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    model = new Character(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), 2);
                    view = new CharacterView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
                    boxController = new BoxController(model, view);
                    boxManager = _boxManager;
                    boxManager.entities.add(boxController);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public void update() {
        double delta = deltaTime * speed * 0.1;
        Vector2 moveVector =  boxManager.move(new Vector2((float) delta,0), view.getBounds(), boxController);
        x += moveVector.x;
        view.moveWorld((int) x, 0);
    }

    public JPanel getView() {
        return view;
    }
}
