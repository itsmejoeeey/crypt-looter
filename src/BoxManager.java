import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
//BoxManager class checks collisions between the player and other box controllers
//Will be responsible for checking collisions for everything not just between player and box

public class BoxManager {
    public World world;
    public BoxController[][] worldColliders;
    public ArrayList<BoxController> colliders = new ArrayList<BoxController>();
    int skinWidth = 2;
    int playerHeightOffset = 20;

    public BoxManager(World world){
        worldColliders = new BoxController[world.mapSize.width][world.mapSize.height];
        this.world = world;
        for(int x = 0; x < world.mapSize.width; x++){
            for (int y = 0; y < world.mapSize.height; y++){
                worldColliders[y][x] = new BoxController(new Rectangle(x * world.tileSize, y * world.tileSize, 50, 50), world.heightMap[y][x], false);
            }
        }
    }

    public void update(){
        for(int x = 0; x < world.mapSize.width; x++){
            for (int y = 0; y < world.mapSize.height; y++) {
                worldColliders[x][y].update();
            }
        }
    }

    public Vector2 move(Vector2 velocity, Rectangle player, BoxController exclude){
        int tileY = ((player.y + player.width / 2) / world.tileSize) % world.mapSize.width;
        int tileX = ((player.x + player.height / 2) / world.tileSize) % world.mapSize.height;

        tileX = tileX < 0 ? 0 : tileX;
        tileY = tileY < 0 ? 0 : tileY;

        int boxHeight = world.heightMap[tileY][tileX];

        int minX =  (tileX - player.height / world.tileSize * 2) % world.mapSize.height;
        int maxX =  (tileX + player.height / world.tileSize * 2 + 1) % world.mapSize.height;
        int minY =  (tileY - player.width / world.tileSize * 2) % world.mapSize.width;
        int maxY =  (tileY + player.width / world.tileSize * 2 + 1) % world.mapSize.width;

        minX = minX < 0 ? 0 : minX;
        minY = minY < 0 ? 0 : minY;

        Origins origins = new Origins(player, playerHeightOffset);

        for(int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if(boxHeight != -1){
                    if (!world.heightCollisions[boxHeight][y][x] && !world.collisions[y][x]) {
                        continue;
                    }
                }
                if(boxHeight == -1 && !world.collisions[y][x]){
                    continue;
                }
                BoxController box = worldColliders[y][x];
                velocity = collideBoxes(box, velocity, origins);
            }
        }

        Origins originsCharacter = new Origins(player, 0);
        for (int i =0; i < colliders.size(); i++){
            if(colliders.get(i) != exclude)
                velocity = collideBoxes(colliders.get(i), velocity, originsCharacter);
        }
        return velocity;
    }

    public Vector2 collideBoxes(BoxController box, Vector2 velocity, Origins origins){
        Vector2 horizontalOriginBot = (velocity.x < 0) ? origins.botLeft : origins.botRight;
        Vector2 horizontalOriginTop = (velocity.x < 0) ? origins.topLeft : origins.topRight;

        if (contains(horizontalOriginBot.x + velocity.x + Math.signum(velocity.x) * skinWidth, horizontalOriginBot.y, box.getRect()) || contains(horizontalOriginTop.x + velocity.x + Math.signum(velocity.x) * skinWidth, horizontalOriginTop.y, box.getRect())) {
            velocity.x = 0;
        }

        Vector2 verticalOriginLeft = (velocity.y < 0) ? origins.topLeft : origins.botLeft;
        Vector2 verticalOriginRight = (velocity.y < 0) ? origins.topRight : origins.botRight;

        if (contains(verticalOriginLeft.x, verticalOriginLeft.y + velocity.y + Math.signum(velocity.y), box.getRect()) || contains(verticalOriginRight.x, verticalOriginRight.y + velocity.y + Math.signum(velocity.y), box.getRect())) {
            velocity.y = 0;
        }
        return velocity;
    }

    private class Origins{
        Vector2 topLeft, topRight, botLeft, botRight;
        public Origins(Rectangle rectangle, int heightOffset) {
            topLeft = new Vector2(rectangle.x + skinWidth, rectangle.y + skinWidth + heightOffset);
            topRight = new Vector2(rectangle.x + rectangle.width - skinWidth, rectangle.y + skinWidth + heightOffset);
            botLeft = new Vector2(rectangle.x + skinWidth, rectangle.y + rectangle.height - skinWidth);
            botRight = new Vector2(rectangle.x + rectangle.width - skinWidth, rectangle.y + rectangle.height - skinWidth);
        }
    }

    private boolean contains(float x, float y, Rectangle rectangle){
        return rectangle.x <= x && x <= rectangle.x + rectangle.width &&
                rectangle.y <= y && y <= rectangle.y + rectangle.height;
    }
    /*
        //Calculates if the delta movement of the player will collide with box
        public Vector2 move(Vector2 v, Rectangle player, int height) {
            int tileY = (int) ((player.y + player.width / 2) / world.tileSize);
            int tileX = (int) ((player.x + player.height / 2) / world.tileSize);
            tileX = tileX < 0 ? 0 : tileX;
            tileY = tileY < 0 ? 0 : tileY;
            int boxHeight = world.heightMap[tileY][tileX];
            int minX =  (tileX - player.height / world.tileSize * 4) % world.mapSize.height;
            int maxX =  (tileX + player.height / world.tileSize * 4 + 1) % world.mapSize.height;
            int minY =  (tileY - player.width / world.tileSize * 4) % world.mapSize.width;
            int maxY =  (tileY + player.width / world.tileSize * 4 + 1) % world.mapSize.width;

            minX = minX < 0 ? 0 : minX;
            minY = minY < 0 ? 0 : minY;

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
                        if (projectRectangle(player, box, v.x, 0, 0.96, 0.9, 0)) {
                            v.x = 0;
                        }
                    }

                    if (v.y != 0) {
                        if (projectRectangle(player, box, 0, v.y, 0.9, 0.96, 0)) {
                            v.y = 0;
                        }
                    }


                    if (v.x != 0 && v.y != 0) {
                        if (projectRectangle(player, box, v.x, v.y, 0.9, 0.9, 1)) {
                            System.out.println("Diagonal Collision" + v.x + "-" + v.y);
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
        */
    private boolean projectRectangle(Rectangle player, BoxController box, float horizontal, float vertical, double widthFactor, double heightFactor, int horizontalOffset){
        Rectangle projRect = new Rectangle(player);
        projRect.setSize((int) (player.width * widthFactor) , (int) (player.height * heightFactor));
        projRect.setLocation((int) (player.x + horizontalOffset + horizontal), (int) (player.y + vertical));
        if (projRect.intersects(box.getRect())) {
            return true;
        }
        return false;
    }
}
