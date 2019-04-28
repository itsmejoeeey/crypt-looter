package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class WorldController {
    public double deltaTime = 0;
    public float speed = 0.2f;

    MainController parent;
    WorldView view;
    World world;

    public WorldController(MainController parent, World world) {
        this.parent = parent;
        this.world = world;

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view = new WorldView(world, parent.screenSize);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public void update() {
        // Check for pause key press
        if(KeyStates.pauseKey.changedSinceLastChecked() && KeyStates.pauseKey.keyState()){
            // Change state to pause menu
            parent.updateState(MainController.GameState_t.PAUSED);
            return;
        }

        // Check for escape key press
        if(KeyStates.escapeKey.changedSinceLastChecked() && KeyStates.escapeKey.keyState()){
            // Change state to escape menu
            parent.updateState(MainController.GameState_t.ESCAPE);
            return;
        }
    }

    public JPanel getView() {
        return view;
    }

    public Point getPos() {
        return view.getPos();
    }

    public void moveWorldABS(int x, int y) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run(){
                    view.moveWorldAbs(x, y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public void moveWorldABSX(int x) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run(){
                    view.moveWorldAbsX(x);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public void moveWorldABSY(int y) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run(){
                    view.moveWorldAbsY(y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public boolean isCameraEnabledX() {
        return view.cameraEnabledX;
    }

    public boolean isCameraEnabledY() {
        return view.cameraEnabledY;
    }
}
