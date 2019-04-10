public class Key {
    private boolean pressed = false;
    private boolean lastState = pressed;

    public void keyPressed() {
        pressed = true;
    }

    public void keyReleased() {
        pressed = false;
    }

    public boolean keyState() {
        lastState = pressed;
        return pressed;
    }

    public boolean changedSinceLastChecked() {
        return (lastState != pressed);
    }
}
