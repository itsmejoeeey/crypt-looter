import javax.lang.model.util.Elements;
import javax.swing.*;
import java.awt.*;

public class CharacterController {
    private float x = 0;
    private float y = 0;

    public double deltaTime = 0;

    // Used to keep track of when a second has elapsed
    private double deltaTimeElapsed = 0;

    public float speed = 0.2f;

    CharacterView view;
    CharacterModel model;
    public BoxManager boxManager;
    public BoxController boxController;
    public AttackController attackController;

    public CharacterController(Point spawnPos, BoxManager _boxManager) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    model = new CharacterModel(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), 2);
                    view = new CharacterView(new Rectangle(spawnPos.x, spawnPos.y, 50, 50), model);
                    attackController = new AttackController(new Rectangle(spawnPos.x + 60, spawnPos.y, 40,40));
                    boxController = new BoxController(model, view);
                    boxManager = _boxManager;
                    boxManager.entities.add(boxController);
                    boxManager.playerAttack = attackController;
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
        collisionDetection();
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

        deltaTimeElapsed += deltaTime;
        if(deltaTimeElapsed > 1000) {
            deltaTimeElapsed-= 1000;
            model.secondsElapsed++;
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
        Vector2 v = boxManager.move(new Vector2((float) deltaX, (float) deltaY), view.getBounds(), boxController);
        x += v.x;
        y += v.y;
    }
    int attackX;
    int attackY;
    public void collisionDetection(){
        if(KeyStates.moveRightKey.keyState() ||
                KeyStates.moveLeftKey.keyState() ||
                KeyStates.moveForwardKey.keyState() ||
                KeyStates.moveBackwardsKey.keyState()){
            attackX = 0;
            attackY = 0;
        }

        if (KeyStates.moveRightKey.keyState())
            attackX += 1;
        if (KeyStates.moveLeftKey.keyState())
            attackX += -1;
        if (KeyStates.moveForwardKey.keyState())
            attackY += 1;
        if (KeyStates.moveBackwardsKey.keyState())
            attackY += -1;
        //Origins playerOrigins = new Origins(view.getBounds(), 0, 0);
        float x = view.getBounds().x + attackX * view.getBounds().height;
        float y = view.getBounds().y - attackY * view.getBounds().height;
        attackController.updateHitBox(new Rectangle((int) x, (int) y, 40, 40), 0);
        attackController.active = (KeyStates.attackKey.changedSinceLastChecked() && KeyStates.attackKey.keyState());
    }

    public JPanel getView() {
        return view;
    }

    public Point getPos() {
        return view.getPos();
    };
}
