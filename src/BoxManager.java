import java.awt.*;

public class BoxManager {
    public static Rectangle box;
    public static Vector2 move(Vector2 v, Rectangle player) {

        Rectangle projRectUp = new Rectangle(player);
        projRectUp.setSize(player.width, (int) (player.height * 0.95));
        projRectUp.setLocation((int) (player.x + v.x), (int) (player.y));
        if (projRectUp.intersects(box)) {
            v.x = 0;
        }

        Rectangle projRectY = new Rectangle(player);
        projRectY.setSize((int) (player.width * 0.95),player.height);
        projRectY.setLocation((int) (player.x), (int) (player.y + v.y));
        if (projRectY.intersects(box)) {
            v.y = 0;
        }
        return v;
    }
}
