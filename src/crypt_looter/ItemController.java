package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class ItemController {
    protected ItemView itemView;

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

    public void update(){}
}
