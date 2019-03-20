import javax.swing.*;
import java.awt.*;

public class CharacterView extends JPanel {
    public CharacterView() {
        this.setBounds(0,0,50,50);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyController());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.fillRect(0,0,50,50);
    }

    public void moveWorld(int newX, int newY) {
        this.setBounds(1500 + newX, 1500 + newY, this.getWidth(), this.getHeight());
    }
}
