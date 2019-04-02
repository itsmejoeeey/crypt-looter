import javax.swing.*;
import java.awt.*;

public class CharacterController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public float speed = 0.2f;

    CharacterView view;
    //public BoxController controller;

    public CharacterController() {
        view = new CharacterView(new Rectangle(1500,1500, 50, 50));
    }

    public void update() {
        //float oldx = (float) x;
        //float oldy = (float) y;

        if (KeyStates.moveRightKey.keyState())
            x += deltaTime * speed;
        if (KeyStates.moveLeftKey.keyState())
            x -= deltaTime * speed;
        if (KeyStates.moveForwardKey.keyState())
            y -= deltaTime * speed;
        if (KeyStates.moveBackwardsKey.keyState())
            y += deltaTime * speed;
        //Vector2 v = controller.move(new Vector2(x,y), new Vector2(oldx, oldy), view.baseTranform);
        view.moveWorld((int)x, (int)y);
    }

    public JPanel getView() {
        return view;
    }
}
