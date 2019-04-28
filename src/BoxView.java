import javax.swing.*;
import java.awt.*;

public class BoxView extends JPanel {
    Rectangle bounds;

    public BoxView(Rectangle bounds){
        this.bounds = bounds;
        this.setOpaque(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(new Dimension(bounds.width, bounds.height));
    }

    @Override
    protected void paintComponent(Graphics g) {

        setLocation(bounds.x, bounds.y);

        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Default rectangle
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
