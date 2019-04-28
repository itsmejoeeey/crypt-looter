package crypt_looter;

import javax.swing.*;
import java.awt.*;

//Item triggers the next level transition when the player intersects it
public class NextLevel extends ItemController {
    MainController mainController;
    public NextLevel(Rectangle bounds, MainController mainController){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    itemView = new CoinView(bounds);
                    itemView.setLocation(bounds.x, bounds.y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
        this.mainController = mainController;
    }

    @Override
    public void triggerItem(){
        mainController.nextLevel();
    }

    //Will always trigger if the player intersects it
    @Override
    public boolean canTrigger(){
        return true;
    }
}
