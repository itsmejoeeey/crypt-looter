import java.awt.*;

public class ItemController {
    private ItemView itemView;

    public ItemController(Rectangle bounds){
        itemView = new ItemView(bounds);
        itemView.setLocation(bounds.x, bounds.y);
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
