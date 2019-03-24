import javax.swing.*;

public class WorldController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public float speed = 0.2f;

    WorldView view;

    public WorldController() {
        view = new WorldView();
    }

    public void update() {
        if (KeyStates.moveRightKey.keyState())
            x += deltaTime * speed;
        if (KeyStates.moveLeftKey.keyState())
            x -= deltaTime * speed;
        if (KeyStates.moveForwardKey.keyState())
            y -= deltaTime * speed;
        if (KeyStates.moveBackwardsKey.keyState())
            y += deltaTime * speed;

        view.moveWorld((int)x, (int)y);
    }

    public JPanel getView() {
        return view;
    }
}
