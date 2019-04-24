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

    BufferedImage[] sprites;

    boolean gridEnabled = false;

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
            tileset = ImageIO.read(new File("src/res/textures/tileset.png"));
        } catch (IOException ex) {
            // Invalid image file path;
            return;
        }

        tilesetColumns = tileset.getWidth()/world.mapTileSize;
        tilesetRows = tileset.getHeight()/world.mapTileSize;
        System.out.println(tilesetColumns);
        System.out.println(tilesetRows);

        sprites = new BufferedImage[tilesetColumns * tilesetRows];

        int count = 0;
        for(int mapY = 0; mapY < tilesetRows; mapY++) {
            for(int mapX = 0; mapX < tilesetColumns; mapX++) {
                BufferedImage tile = tileset.getSubimage(mapX * world.mapTileSize, mapY * world.mapTileSize, world.mapTileSize, world.mapTileSize);
                sprites[count] = tile;
                count++;
            }
        }
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
                    int tileFloorValue = world.mapFloor[y][x] - 1;
                    if(tileFloorValue > 0) {
                        BufferedImage tileFloor = sprites[tileFloorValue];
                        g2.drawImage(tileFloor, x * 50, y * 50, 50, 50, null);
                    } else {
                        g2.setColor(Color.white);
                        g2.drawRect(x * 50, y * 50, 50, 50);
                    }

                    // Secondary map texture to display ontop of the other map map texture
                    // (used for displaying flowers, bushes, tree-trunks, etc
                    int tileCosmeticValue = world.mapCosmetic[y][x] - 1;
                    if(tileCosmeticValue > 0) {
                        BufferedImage tileCosmetic = sprites[tileCosmeticValue];
                        g2.drawImage(tileCosmetic, x * 50, y * 50, 50, 50, null);
                    }

                    if(gridEnabled) {
                        g2.setStroke(new BasicStroke(2));
                        g2.setColor(Color.white);
                        g2.drawRect(x * 50, y * 50, 50, 50);
                    }
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
