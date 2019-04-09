import javax.swing.*;
import java.awt.*;

public class EnemyController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public float speed = 0.2f;

    CharacterView view;
    public BoxManager boxManager;
    public BoxController collider;

    public EnemyController() {
        view = new CharacterView(new Rectangle(1500,1550, 50, 50));
        collider = new BoxController(1500,1550, 50, 50, false);
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
