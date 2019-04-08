import java.awt.*;
import java.util.ArrayList;
//BoxManager class checks collisions between the player and other box controllers
//Will be responsible for checking collisions for everything not just between player and box

public class BoxManager {
    //TODO Change to BoxCollider

    public static ArrayList<BoxController> colliders = new ArrayList<BoxController>();



    //Calculates if the delta movement of the player will collide with box
    public static Vector2 move(Vector2 v, Rectangle player) {
        for (int i= 0; i < colliders.size(); i++) {
            BoxController box = colliders.get(i);
            if (v.x != 0){
                //Projects a squished version of the player vertically. If still collides then can't move horizontally
                Rectangle projRectUp = new Rectangle(player);
                projRectUp.setSize(player.width, (int) (player.height * 0.95));
                projRectUp.setLocation((int) (player.x + v.x), (int) (player.y));
                if (projRectUp.intersects(box.box)) {
                    v.x = 0;
                }
            }

            if(v.y != 0) {
                //Projects a squished version of the player horizontally. If still collides then can't move vertically
                Rectangle projRectY = new Rectangle(player);
                projRectY.setSize((int) (player.width * 0.95), player.height);
                projRectY.setLocation((int) (player.x), (int) (player.y + v.y));
                if (projRectY.intersects(box.box)) {
                    v.y = 0;
                }
            }
        }
        return v;
    }
}
