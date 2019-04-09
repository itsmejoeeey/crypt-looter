import javax.swing.*;
import java.awt.*;

public class CharacterView extends JPanel {
    Rectangle baseTranform;
    public CharacterView(Rectangle transform) {
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyController());
        this.setPreferredSize(new Dimension(transform.width,transform.height));
        this.baseTranform = transform;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.black);
        g2.fillRect(baseTranform.x, baseTranform.y, baseTranform.width, baseTranform.height);
    }

    public void moveWorld(int newX, int newY) {
        this.setLocation(baseTranform.x + newX, baseTranform.y + newY);
    }

    public Point getPos() {
        return new Point(this.getX(), this.getY());
    }

}
