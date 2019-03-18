public class Key {
    public boolean pressed;

    public void keyPressed() {
        pressed = true;
    }

    public void keyReleased() {
        pressed = false;
    }

    public boolean keyState() {
        return pressed;
    }
}
