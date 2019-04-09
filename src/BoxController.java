import javax.swing.*;
import java.awt.*;

//Simple class with a Character view
//TODO add BoxModel
public class BoxController {
    public Rectangle box;
    CharacterView view;

    public BoxController(int x, int y, int width, int height, boolean addView) {
        box =  new Rectangle(x,y,width,height);
        if(addView) {
            view = new CharacterView(box);
        }
    }

    public void update() {
        view.moveWorld(0, 0);
    }
}
