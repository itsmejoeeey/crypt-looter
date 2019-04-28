package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class PreviousLevel extends ItemController{
    MainController mainController;
    public PreviousLevel(Rectangle bounds, MainController mainController){
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

    @Override
    public boolean canTrigger(){
        return true;
    }
}
