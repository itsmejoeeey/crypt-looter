package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class SmallHealthPotion extends ItemController {
    CharacterModel playerModel;
    public SmallHealthPotion(Rectangle bounds, CharacterModel playerModel){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    itemView = new SmallHealthPotionView(bounds);
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
        playerModel.increaseHealth(1);
    }

    @Override
    public boolean canTrigger(){
        if(playerModel.health != playerModel.maxHealth){
            return true;
        }
        return false;
    }
}
