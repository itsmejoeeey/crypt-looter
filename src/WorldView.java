import javax.swing.*;
import java.awt.*;

import static java.lang.Math.abs;

public class WorldView extends JPanel {
    public int x;
    public int y;

    public WorldView() {
        this.setBounds(-773,-1061,10000,10000);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
    }

    private int roundDownNearest(int num, int round) {
        return num - (num % round);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int xMin = roundDownNearest(abs(x), 50) / 50;
        int yMin = roundDownNearest(abs(y), 50) / 50;
        int xMax = xMin + 29;
        int yMax = yMin + 29;

        for(int x = xMin; x <= xMax; x++) {
            for(int y = yMin; y <= yMax; y++) {
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.white);
                g2.drawRect(x*50,y*50,50,50);

                g2.setColor(Color.blue);
                g2.fillRect(x*50,y*50,50,50);
            }
        }
    }

    public void moveWorld(int newX, int newY) {
        this.setLocation(getPos().x + newX, getPos().y + newY);
    }

    public void moveWorldAbs2(int newX, int newY) {
        this.setLocation(newX, newY);
    }

    public Point getPos() {
        return new Point(this.getX(), this.getY());
    }

}
