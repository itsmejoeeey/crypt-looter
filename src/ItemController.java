import java.awt.*;

public class ItemController {
    private ItemView itemView;

    public ItemController(Rectangle bounds){
        itemView = new ItemView(bounds);
    }

    public Rectangle getRect(){
        return itemView.getBounds();
    }
    public void triggerItem(){
        System.out.println("Item Triggered");
    }
}
