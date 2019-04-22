import javax.swing.*;
import java.awt.*;

import static java.lang.Math.abs;

public class WorldView extends JPanel {
    public int x = 0;
    public int y = 0;

    public boolean cameraEnabledX;
    public boolean cameraEnabledY;

    public WorldView(Dimension screenSize) {
        int initialX;
        int initialY;

        Dimension mapSize = new Dimension(5000,5000);

        if(mapSize.height <= screenSize.height) {
            cameraEnabledY = false;
            initialY = (screenSize.height - mapSize.height)/2;
        } else {
            cameraEnabledY = true;
            initialY = 0;
        }

        if(mapSize.width <= screenSize.width) {
            cameraEnabledX = false;
            initialX = (screenSize.width - mapSize.width)/2;
        } else {
            cameraEnabledX = true;
            initialX = 0;
        }

        this.setBounds(initialX, initialY, mapSize.width, mapSize.height);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        System.out.println(screenSize.width);
        System.out.println(screenSize.height);
        System.out.println(cameraEnabledX);
        System.out.println(cameraEnabledY);
        System.out.println(initialX);
        System.out.println(initialY);
        System.out.println(this.getWidth());
        System.out.println(this.getHeight());
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

    public void moveWorldAbs(int newX, int newY) {
        x = newX;
        y = newY;

        this.setLocation(x, y);
    }

    public void moveWorldAbsX(int newX) {
        x = newX;
        this.setLocation(x, this.getY());
    }

    public void moveWorldAbsY(int newY) {
        y = newY;
        this.setLocation(this.getX(), y);
    }

    public Point getPos() {
        return new Point(this.getX(), this.getY());
    }

}
