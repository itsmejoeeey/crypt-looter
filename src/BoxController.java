import javax.swing.*;
import java.awt.*;

public class BoxController {
    public Rectangle box;

    CharacterView view;

    public BoxController(){
        box = new Rectangle(1650, 1500, 50,50);
        view = new CharacterView(box);
    }

    public void update(){
        view.moveWorld(0, 0);
    }

    public Vector2 move(Vector2 v, Rectangle player){
        Rectangle projRect = new Rectangle(player);
        projRect.setLocation((int) (player.x + v.x), (int) (player.y + v.y));
        if (projRect.intersects(box)){
            return new Vector2(0,0);
        }
        return v;
    }
}
