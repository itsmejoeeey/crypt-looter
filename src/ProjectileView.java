import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProjectileView extends JPanel {
    BufferedImage texture;
    Rectangle origin;
    public boolean stationary;
    public ProjectileView(Rectangle bounds, boolean stationary){
        this.origin = bounds;
        this.setOpaque(false);
        this.setFocusable(true);
        setPreferredSize(new Dimension(bounds.width, bounds.height));
        this.stationary =stationary;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //setLocation(origin.x, origin.y);

        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Default rectangle
        g2.setColor(Color.black);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    public void moveWorld(int newX, int newY) {
        this.setLocation(origin.x + newX, origin.y + newY);
    }
}
