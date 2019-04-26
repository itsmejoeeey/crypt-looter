import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemView extends JPanel {
    BufferedImage texture;
    public ItemView(Rectangle bounds){
        setBounds(bounds);
        setPreferredSize(new Dimension(bounds.width, bounds.height));
    }
}
