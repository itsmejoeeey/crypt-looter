import java.awt.*;
import javax.swing.*;

public class EnemyController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public float speed = 0.2f;

    CharacterView view;
    CharacterModel model;
    public BoxManager boxManager;
    public BoxController collider;

    public EnemyController() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view = new CharacterView(new Rectangle(1500,1500, 50, 50));
                    model = new CharacterModel(new Rectangle(1500,1500, 50, 50));
                    view.model = model;
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public void update() {
        double delta = deltaTime * speed * 0.1;
        x += delta;
        boxManager.move(new Vector2(x,0), view.getBounds());
        view.moveWorld((int) x, 0);
    }

    public JPanel getView() {
        return view;
    }
}
