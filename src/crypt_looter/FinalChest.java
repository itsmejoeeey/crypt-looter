package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class FinalChest extends ItemController {
    public MainController mainController;
    private CharacterModel[] bossModel;
    public boolean open = false;
    boolean oldOpen = false;

    public FinalChest(Rectangle bounds, CharacterModel[] bossModel, MainController mainController){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    itemView = new FinalChestView(bounds);
                    itemView.setLocation(bounds.x, bounds.y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
        this.bossModel = bossModel;
        this.mainController = mainController;
    }

    //Check if chest can trigger if so change chest texture in itemView
    public void update(){
        if(canTrigger()){
            open = true;
        }
        if (oldOpen == false && open == true) {
            itemView.trigger();
            oldOpen = true;
        }
    }

    @Override
    public void triggerItem(){
        mainController.updateState(MainController.GameState_t.GAME_OVER);
    }

    //If all of the bosses are dead then finalChest then the chest can trigger
    @Override
    public boolean canTrigger(){
        for (int i = 0; i < bossModel.length; i++){
            if(!bossModel[i].dead){
                return false;
            }
        }
        return true;
    }
}
