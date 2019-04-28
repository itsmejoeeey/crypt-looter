package crypt_looter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class Dagger extends ItemController {
    CharacterModel playerModel;
    public Dagger(Rectangle bounds, CharacterModel playerModel){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    itemView = new DaggerView(bounds);
                    itemView.setLocation(bounds.x, bounds.y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
        this.playerModel = playerModel;
    }

    //Set dagger equipped to true to enable to attack
    @Override
    public void triggerItem(){
        playerModel.daggerEquipped = true;
    }

    //Only trigger if the dagger is not already equipped
    @Override
    public boolean canTrigger(){
        return !playerModel.daggerEquipped;
    }
}
