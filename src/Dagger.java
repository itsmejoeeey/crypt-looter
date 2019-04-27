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

    @Override
    public void triggerItem(){
        playerModel.daggerEquipped = true;
    }

    @Override
    public boolean canTrigger(){
        return !playerModel.daggerEquipped;
    }
}
