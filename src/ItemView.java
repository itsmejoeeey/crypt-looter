import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemView extends JPanel {
    BufferedImage texture;
    Rectangle bounds;
    public boolean stationary;
    public ItemView(Rectangle bounds, boolean stationary){
        this.bounds = bounds;
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(new Dimension(bounds.width, bounds.height));
        this.stationary =stationary;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setLocation(bounds.x, bounds.y);

        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Default rectangle
        g2.setColor(Color.black);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
