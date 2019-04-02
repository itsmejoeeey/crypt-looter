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

    public Vector2 move(Vector2 v, Rectangle player) {

        Rectangle projRectup = new Rectangle(player);
        projRectup.setSize(player.width, (int) (player.height * 0.95));
        projRectup.setLocation((int) (player.x + v.x), (int) (player.y));
        if (projRectup.intersects(box)) {
            v.x = 0;
        }

        Rectangle projRecty = new Rectangle(player);
        projRecty.setSize((int) (player.width * 0.95),player.height);
        projRecty.setLocation((int) (player.x), (int) (player.y + v.y));
        if (projRecty.intersects(box)) {
            v.y = 0;
        }
        return v;
    }
}
