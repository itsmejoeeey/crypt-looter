package crypt_looter;

import javax.swing.*;
import java.awt.*;

//Item sets the player to maxHealth if taken damage
public class LargeHealthPotion extends ItemController {
    CharacterModel playerModel;
    public LargeHealthPotion(Rectangle bounds, CharacterModel playerModel){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    itemView = new LargeHealthPotionView(bounds);
                    itemView.setLocation(bounds.x, bounds.y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
        this.playerModel = playerModel;
    }

    //Increases the players health by 3
    @Override
    public void triggerItem(){
         playerModel.increaseHealth(3);
    }

    //Returns if the player has taken damage;
    @Override
    public boolean canTrigger(){
        if(playerModel.health != playerModel.maxHealth){
            return true;
        }
        return false;
    }
}
