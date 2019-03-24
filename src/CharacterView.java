import javax.swing.*;
import java.awt.*;

public class CharacterView extends JPanel {
    public CharacterView() {
        System.out.println("Init");
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyController());
        this.setPreferredSize(new Dimension(50,50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.black);
        g2.fillRect(0,0,50,50);
    }

    public void moveWorld(int newX, int newY) {
        System.out.println(this.getBounds());
        System.out.println(this.getWidth());
        this.setLocation(1500 + newX, 1500 + newY);

    }
}
