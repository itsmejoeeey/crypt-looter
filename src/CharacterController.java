import javax.swing.*;
import java.awt.*;

public class CharacterController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;
    public float speed = 0.2f;

    CharacterView view;
    public CharacterModel model;
    public BoxManager boxManager;

    public CharacterController(Point spawnPos) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view = new CharacterView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50));
                    model = new CharacterModel(new Rectangle(spawnPos.x, spawnPos.y, 50, 50));
                    view.model = model;
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    //Moves player based on key inputs
    //delta is equal to newPosition - oldPosition so if 0 the player will stand still
    public void update() {
        groundMovement();

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

    public void groundMovement(){
        double deltaX = 0, deltaY = 0;
        if (KeyStates.moveRightKey.keyState())
            deltaX = deltaTime * speed;
        if (KeyStates.moveLeftKey.keyState())
            deltaX = -deltaTime * speed;
        if (KeyStates.moveForwardKey.keyState())
            deltaY = -deltaTime * speed;
        if (KeyStates.moveBackwardsKey.keyState())
            deltaY = deltaTime * speed;
        //Checks with the box manager if it will hit a box and returns movement vector based on collisions
        Vector2 v = boxManager.move(new Vector2((float) deltaX, (float) deltaY), view.getBounds());
        x += v.x;
        y += v.y;
    }

    public JPanel getView() {
        return view;
    }

    public Point getPos() {
        return view.getPos();
    };
}
