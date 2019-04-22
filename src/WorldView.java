import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.abs;

public class WorldView extends JPanel {
    public int x = 0;
    public int y = 0;

    public boolean cameraEnabledX;
    public boolean cameraEnabledY;

    private World world;

    BufferedImage tileset;
    int tilesetColumns;
    int tilesetRows;

    public WorldView(World world, Dimension screenSize) {
        this.world = world;

        int initialX;
        int initialY;

        Dimension mapSize = new Dimension(world.mapSize.width*world.tileSize,world.mapSize.height*world.tileSize);
        System.out.println(mapSize);

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

        try {
            tileset = ImageIO.read(new File("src/res/tileset.png"));
        } catch (IOException ex) {
            // Invalid image file path;
            return;
        }

        tilesetColumns = tileset.getWidth()/world.mapTileSize;
        tilesetRows = tileset.getHeight()/world.mapTileSize;
        System.out.println(tilesetColumns);
        System.out.println(tilesetRows);
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
                if(y < world.mapSize.height && x < world.mapSize.width) {
                    int tileValue = world.mapFloor[y][x] - 1;
                    BufferedImage tile = tileset.getSubimage(tileValue % tilesetColumns * world.mapTileSize, tileValue / tilesetColumns * world.mapTileSize, world.mapTileSize, world.mapTileSize);
                    g2.drawImage(tile, x * 50, y * 50, 50, 50, null);

                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(Color.white);
                    g2.drawRect(x * 50, y * 50, 50, 50);
//
//                g2.setColor(Color.blue);
//                g2.fillRect(x*50,y*50,50,50);
                }
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
