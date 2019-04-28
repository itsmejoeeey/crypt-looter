import java.awt.*;
import java.util.ArrayList;
//BoxManager class checks collisions between the playerModel and other box controllers
//Will be responsible for checking collisions for everything not just between playerModel and box

public class BoxManager {
    public World world;
    public BoxController[][] colliders;
    public ArrayList<BoxController> entities = new ArrayList<>();
    public ArrayList<AttackController> enemyAttacks = new ArrayList<>();
    public AttackController[] playerAttacks;
    public ItemManager itemManager;
    public ProjectileManager projectileManager;
    public BoxController player;

    int skinWidth = 2;
    int playerHeightOffset = 20;

    public BoxManager(World world, WorldController worldController){
        colliders = new BoxController[world.mapSize.width][world.mapSize.height];
        this.world = world;
        for(int x = 0; x < world.mapSize.width; x++){
            for (int y = 0; y < world.mapSize.height; y++){
                colliders[y][x] = new BoxController(new Rectangle(x * world.tileSize, y * world.tileSize, 50, 50), worldController, world.heightMap[y][x] , false);
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

    public boolean projectileMove(Rectangle arrow, int height){
        int tileY = Math.floorMod(((arrow.y + arrow.width / 2) / world.tileSize) , world.mapSize.width);
        int tileX = Math.floorMod(((arrow.x + arrow.height / 2) / world.tileSize), world.mapSize.height);

        int minX =  (tileX - arrow.height / world.tileSize * 2) % world.mapSize.height;
        int maxX =  (tileX + arrow.height / world.tileSize * 2 + 1) % world.mapSize.height;
        int minY =  (tileY - arrow.width / world.tileSize * 2 % world.mapSize.width);
        int maxY =  (tileY + arrow.width / world.tileSize * 2 + 1) % world.mapSize.width;

        minX = minX < 0 ? 0 : minX;
        minY = minY < 0 ? 0 : minY;

        for(int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if(height != -1){
                    if (!world.heightCollisions[height][y][x] && !world.collisions[y][x]) {
                        continue;
                    }
                }
                if(height == -1 && !world.collisions[y][x]){
                    continue;
                }
                BoxController box = colliders[y][x];
                if(box.getRect().intersects(arrow)){
                    return true;
                }
            }
        }
        return false;
    }

    public Vector2 move(Vector2 velocity, Rectangle character, BoxController entity){
        int tileY = Math.floorMod(((character.y + character.width / 2) / world.tileSize) , world.mapSize.width);
        int tileX = Math.floorMod(((character.x + character.height / 2) / world.tileSize), world.mapSize.height);

        int boxHeight = world.heightMap[tileY][tileX];
        entity.setDeath(world.death[tileY][tileX]);
        entity.setHeight(boxHeight);

        int minX =  (tileX - character.height / world.tileSize * 2) % world.mapSize.height;
        int maxX =  (tileX + character.height / world.tileSize * 2 + 1) % world.mapSize.height;
        int minY =  (tileY - character.width / world.tileSize * 2 % world.mapSize.width);
        int maxY =  (tileY + character.width / world.tileSize * 2 + 1) % world.mapSize.width;
        minX = minX < 0 ? 0 : minX;
        minY = minY < 0 ? 0 : minY;


        Origins origins = new Origins(character, playerHeightOffset, skinWidth);
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
                BoxController box = colliders[y][x];
                velocity = collideBoxes(box, velocity, origins);
            }
        }

        Origins characterOrigins = new Origins(character, 0, skinWidth);
        for(int i= 0; i < entities.size(); i++){
            if(entities.get(i) != entity){
                velocity = collideBoxes(entities.get(i), velocity, characterOrigins);
            }
        }
        return velocity;
    }



    private Vector2 collideBoxes(BoxController box, Vector2 velocity, Origins origins){
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

    public void detectItemCollision(BoxController player){
        for(int i = 0; i < itemManager.items.size(); i++) {
            if (player.getRect().intersects(itemManager.items.get(i).getRect())) {
                if(itemManager.items.get(i).canTrigger()) {
                    itemManager.items.get(i).triggerItem();
                    itemManager.useItem(itemManager.items.get(i));
                }
            }
        }
    }

    public boolean detectPlayerAttackCollision(BoxController entity){
        if((entity.getHeight() != playerAttacks[0].attackHeight) && !(playerAttacks[0].attackHeight == -1 || entity.getHeight() == -1)){
            return false;
        }
        if(playerAttacks[0].active){
            for(int j= 0; j < 3; j++) {
                AttackController playerAttack = playerAttacks[j];
                Point[] points = playerAttack.getPoints();
                for (int i = 0; i < points.length; i++) {
                    if (contains(points[i].x, points[i].y, entity.getRect())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean detectEnemyAttackCollision(BoxController player){
        for(int j = 0; j < enemyAttacks.size(); j++) {
            AttackController enemyAttack = enemyAttacks.get(j);
            if ((player.getHeight() != enemyAttack.attackHeight) && !(player.getHeight() == -1 || enemyAttack.attackHeight == -1)) {
                continue;
            }
            if(enemyAttack.active) {
                Point[] points = enemyAttack.getPoints();
                for (int i = 0; i < points.length; i++) {
                    if (contains(points[i].x, points[i].y, player.getRect())) {
                        return true;
                    }
                }
            }
            continue;
        }
        return false;
    }

    public boolean detectEnemyProjectileCollision(BoxController boxController){
        for (int i= 0; i < projectileManager.projectiles.size(); i++){
            if(projectileManager.projectiles.get(i).archer == boxController){
                continue;
            }
            if(projectileManager.projectiles.get(i).view.getBounds().intersects(boxController.getRect())){
                projectileManager.destroyProjectile(projectileManager.projectiles.get(i));
                return true;
            }
        }
        return false;
    }

    public boolean detectPlayerProjectileCollision(BoxController enemy){
        for (int i= 0; i < projectileManager.projectiles.size(); i++){
            if(projectileManager.projectiles.get(i).archer != player){
                continue;
            }
            if (projectileManager.projectiles.get(i).view.getBounds().intersects(enemy.getRect())) {
                projectileManager.destroyProjectile(projectileManager.projectiles.get(i));
                return true;
            }
        }
        return false;
    }


    private boolean contains(double x, double y, Rectangle rectangle){
        return rectangle.x <= x && x <= rectangle.x + rectangle.width &&
                rectangle.y <= y && y <= rectangle.y + rectangle.height;
    }
}
