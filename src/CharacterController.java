import javax.swing.*;
import java.awt.*;

public class CharacterController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public float speed = 0.2f;

    CharacterView view;
    public BoxManager boxManager;

    public CharacterController() {
        view = new CharacterView(new Rectangle(1500,1500, 50, 50));
    }

    public void update() {
        double deltaX = 0, deltaY = 0;
        if (KeyStates.moveRightKey.keyState())
            deltaX = deltaTime * speed;
        if (KeyStates.moveLeftKey.keyState())
            deltaX = -deltaTime * speed;
        if (KeyStates.moveForwardKey.keyState())
            deltaY = -deltaTime * speed;
        if (KeyStates.moveBackwardsKey.keyState())
            deltaY = deltaTime * speed;
        Vector2 v = boxManager.move(new Vector2((float) deltaX, (float) deltaY), view.getBounds());
        x += v.x;
        y += v.y;
        view.moveWorld((int)x, (int)y);
    }

    public JPanel getView() {
        return view;
    }
}
