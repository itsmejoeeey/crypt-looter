import javax.swing.*;
import java.awt.*;

public class WorldView extends JPanel {
    public WorldView() {
        this.setBounds(-1000,-1000,10000,10000);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyController());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for(int x = 0; x < 100; x++) {
            for(int y = 0; y < 100; y++) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.white);
                g2.drawRect(x*50,y*50,50,50);

                g2.setColor(Color.blue);
                g2.fillRect(x*50,y*50,50,50);
            }
        }
    }

    public void moveWorld(int newX, int newY) {
        //this.setBounds(-1000 + newX, -1000 + newY, this.getWidth(), this.getHeight());
        this.setLocation(getPos().x + newX, getPos().y + newY);
    }

    public void moveWorldAbs2(int newX, int newY) {
        //this.setBounds(-1000 + newX, -1000 + newY, this.getWidth(), this.getHeight());
        this.setLocation(newX, newY);
    }

    public Point getPos() {
        return new Point(this.getX(), this.getY());
    }

}
