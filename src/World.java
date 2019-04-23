import java.awt.*;

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
}
