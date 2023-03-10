package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class Coin extends ItemController{
    CharacterModel playerModel;
    public Coin(Rectangle bounds, CharacterModel playerModel){
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
        this.playerModel = playerModel;
    }

    //Increase the players score
    @Override
    public void triggerItem(){
        playerModel.increaseScore(1000);
    }

    //Will always activate when picked up
    @Override
    public boolean canTrigger(){
        return true;
    }
}
