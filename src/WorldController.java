import javax.swing.*;
import java.awt.*;

public class WorldController {
    public double deltaTime = 0;
    public float speed = 0.2f;

    MainController parent;
    WorldView view;

    public WorldController(MainController parent) {
        this.parent = parent;
        view = new WorldView();
    }

    public void update() {
        if(KeyStates.pauseKey.changedSinceLastChecked() && KeyStates.pauseKey.keyState()){
            parent.updateState(MainController.GameState_t.PAUSED);
            return;
        }
    }

    public JPanel getView() {
        return view;
    }

    public Point getPos() {
        return view.getPos();
    }

    public void moveWorld(int deltaX, int deltaY) {
        view.moveWorld(deltaX, deltaY);
    }

    public void moveWorldABS(int x, int y) {
        view.x = x;
        view.y = y;
        view.moveWorldAbs2(x, y);
    }
}
