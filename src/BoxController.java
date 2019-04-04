import javax.swing.*;
import java.awt.*;

public class BoxController {
    public Rectangle box;

    CharacterView view;

    public BoxController() {
        box = new Rectangle(1650, 1500, 50, 50);
        view = new CharacterView(box);
    }

    public void update() {
        view.moveWorld(0, 0);
    }
}
