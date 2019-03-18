import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyController implements KeyListener {
    /**
     * Handle the key-pressed event from the text field.
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                KeyStates.moveForwardKey.keyPressed();
                break;
            case KeyEvent.VK_A:
                KeyStates.moveLeftKey.keyPressed();
                break;
            case KeyEvent.VK_S:
                KeyStates.moveBackwardsKey.keyPressed();
                break;
            case KeyEvent.VK_D:
                KeyStates.moveRightKey.keyPressed();
                break;
        }
    }

    /**
     * Handle the key-released event from the text field.
     */
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                KeyStates.moveForwardKey.keyReleased();
                break;
            case KeyEvent.VK_A:
                KeyStates.moveLeftKey.keyReleased();
                break;
            case KeyEvent.VK_S:
                KeyStates.moveBackwardsKey.keyReleased();
                break;
            case KeyEvent.VK_D:
                KeyStates.moveRightKey.keyReleased();
                break;
        }
    }

    /**
     * Unused
     */
    public void keyTyped(KeyEvent e) {};
}

