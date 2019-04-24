import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
//BoxManager class checks collisions between the player and other box controllers
//Will be responsible for checking collisions for everything not just between player and box

public class BoxManager {
    public World world;
    public BoxController[][] colliders;

    public BoxManager(World world){
        colliders = new BoxController[world.mapSize.width][world.mapSize.height];
        this.world = world;
        for(int x = 0; x < world.mapSize.width; x++){
            for (int y = 0; y < world.mapSize.height; y++){
                colliders[y][x] = new BoxController(new Rectangle(x * world.tileSize, y * world.tileSize, 50, 50), world.heightMap[y][x] , false);
            }
        }
    }

    public void update(){
        for(int x = 0; x < world.mapSize.width; x++){
            for (int y = 0; y < world.mapSize.height; y++) {
                colliders[x][y].update();
            }
        }
    }

    //Calculates if the delta movement of the player will collide with box
    public Vector2 move(Vector2 v, Rectangle player, int height) {
        int tileY = (int) ((player.y + player.width / 2) / world.tileSize);
        int tileX = (int) ((player.x + player.height / 2) / world.tileSize);
        tileX = tileX < 0 ? 0 : tileX;
        tileY = tileY < 0 ? 0 : tileY;
        int boxHeight = world.heightMap[tileY][tileX];
        int playerWidth = player.width / world.tileSize;
        int playerHeight = player.height / world.tileSize;
        int minX =  (tileX - playerHeight * 4) % world.mapSize.height;
        int maxX =  (tileX + playerHeight * 4 + 1) % world.mapSize.height;
        int minY =  (tileY - playerWidth * 4) % world.mapSize.width;
        int maxY =  (tileY + playerWidth * 4 + 1) % world.mapSize.width;

        minX = minX < 0 ? 0 : minX;
        minY = minY < 0 ? 0 : minY;

        System.out.println(minX + "-" + maxX + ":" + minY + "-" + maxY);
        for(int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                BoxController box = colliders[y][x];
                int tileHeight = world.heightMap[y][x];

                if((!world.heightCollisions[height][y][x] || !world.heightCollisions[boxHeight + 1][tileY][tileX]) && !world.collisions[y][x]){
                    continue;
                }

                height = boxHeight;

                if (v.x != 0) {
                    //Projects a squished version of the player vertically. If still collides then can't move horizontally
                    if (projectRectangle(player, box, v.x, 0, 1, 0.9, 0)) {
                        v.x = 0;
                    }
                }

                if (v.y != 0) {
                    if (projectRectangle(player, box, 0, v.y, 0.9, 1, 0)) {
                        v.y = 0;
                    }
                }


                if (v.x != 0 && v.y != 0) {
                    if (projectRectangle(player, box, v.x, v.y, 0.9, 0.9, 1)) {
                        v.x = 0;
                        v.y = 0;
                    }
                }
            }
        }
        return v;
    }

    private double distanceBetween(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.abs(x1-x2) ^ 2 + Math.abs(y1-y2) ^ 2);
    }

    private boolean projectRectangle(Rectangle player, BoxController box, float horizontal, float vertical, double widthFactor, double heightFactor, int horizontalOffset){
        Rectangle projRect = new Rectangle(player);
        projRect.setSize((int) (player.width * widthFactor) , (int) (player.height * heightFactor));
        projRect.setLocation((int) (player.x + horizontalOffset + horizontal), (int) (player.y + vertical));
        if (projRect.intersects(box.rect)) {
            return true;
        }
        return false;
    }
}
