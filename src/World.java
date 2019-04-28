import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class World {
    public Dimension mapSize;
    public int mapTileSize;

    public int spawnX;
    public int spawnY;

    public boolean[][] collisions;
    public boolean[][] death;

    public boolean[][][] heightCollisions;

    public int[][] heightMap;

    public int[][] mapFloor;
    public int[][] mapCosmetic;

    // Actual tile size implemented in our game
    public int tileSize = 50;

    // Enemy position (represented in tiles)
    public ArrayList<Point2D.Double> enemiesNormal = new ArrayList<Point2D.Double>();
    public ArrayList<Point2D.Double> enemiesBoss = new ArrayList<Point2D.Double>();

    // Item position (represented in tiles)
    public ArrayList<Point2D.Double> itemsHealthPotBig = new ArrayList<Point2D.Double>();
    public ArrayList<Point2D.Double> itemsHealthPotSmall = new ArrayList<Point2D.Double>();
    public ArrayList<Point2D.Double> itemsCoin = new ArrayList<Point2D.Double>();
    public ArrayList<Point2D.Double> itemsFinalChest = new ArrayList<Point2D.Double>();
    public ArrayList<Point2D.Double> itemsBow = new ArrayList<Point2D.Double>();
    public ArrayList<Point2D.Double> itemsDagger = new ArrayList<Point2D.Double>();
}
