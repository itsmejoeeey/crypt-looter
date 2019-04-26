import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class World {
    public Dimension mapSize;
    public int mapTileSize;

    public boolean[][] collisions;
    public boolean[][] death;

    public boolean[][][] heightCollisions;

    public int[][] heightMap;

    public int[][] mapFloor;
    public int[][] mapCosmetic;
    public int[][] mapFloated;

    // Actual tile size implemented in our game
    public int tileSize = 50;

    // Enemy position (represented in tiles)
    ArrayList<Point2D.Double> enemiesNormal = new ArrayList<Point2D.Double>();
    ArrayList<Point2D.Double> enemiesBoss = new ArrayList<Point2D.Double>();
}
