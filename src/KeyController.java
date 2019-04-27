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
            case KeyEvent.VK_ESCAPE:
                KeyStates.escapeKey.keyPressed();
                break;
            case KeyEvent.VK_P:
                KeyStates.pauseKey.keyPressed();
                break;
            case KeyEvent.VK_J:
                KeyStates.attackKey.keyPressed();
                break;
            case KeyEvent.VK_K:
                KeyStates.projKey.keyPressed();
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
            case KeyEvent.VK_ESCAPE:
                KeyStates.escapeKey.keyReleased();
                break;
            case KeyEvent.VK_P:
                KeyStates.pauseKey.keyReleased();
                break;
            case KeyEvent.VK_J:
                KeyStates.attackKey.keyReleased();
                break;
            case KeyEvent.VK_K:
                KeyStates.projKey.keyReleased();
                break;
        }
    }

    /**
     * Unused
     */
    public void keyTyped(KeyEvent e) {};
}

