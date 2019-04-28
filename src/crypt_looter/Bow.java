package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class Bow extends ItemController{
    CharacterModel playerModel;
    public Bow(Rectangle bounds, CharacterModel playerModel){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    itemView = new BowView(bounds);
                    itemView.setLocation(bounds.x, bounds.y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
        this.playerModel = playerModel;
    }

    @Override
    public void triggerItem(){
        playerModel.bowEquipped = true;
    }

    @Override
    public boolean canTrigger(){
        return !playerModel.bowEquipped;
    }
}
