import javax.swing.*;
import java.awt.*;

public class FinalChest extends ItemController {
    public MainController mainController;
    private CharacterModel[] bossModel;

    public FinalChest(Rectangle bounds, CharacterModel[] bossModel){
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
    }

    @Override
    public void triggerItem(){
        mainController.updateState(MainController.GameState_t.GAME_OVER);
        //itemView.trigger();
    }

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
