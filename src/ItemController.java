import javax.swing.*;
import java.awt.*;

public class ItemController {
    private ItemView itemView;

    public ItemController(Rectangle bounds){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    itemView = new ItemView(bounds, true);
                    itemView.setLocation(bounds.x, bounds.y);
                }
            });
        } catch (Exception e) {
            // Required to catch potential exception
        }
    }

    public Rectangle getRect(){
        return itemView.getBounds();
    }
    public void triggerItem(){
        System.out.println("Item Triggered");
    }

    public ItemView getView(){
        return itemView;
    }

    public boolean canTrigger(){
        return true;
    }
}
