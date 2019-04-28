package crypt_looter;

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

    //Create box manager and create a boxCollider for each tile
    public BoxManager(World world, WorldController worldController){
        colliders = new BoxController[world.mapSize.width][world.mapSize.height];
        this.world = world;
        for(int x = 0; x < world.mapSize.width; x++){
            for (int y = 0; y < world.mapSize.height; y++){
                colliders[y][x] = new BoxController(new Rectangle(x * world.tileSize, y * world.tileSize, 50, 50), worldController, world.heightMap[y][x] , false);
            }
        }
    }

    //Update the box controller of each tile
    public void update(){
        for(int x = 0; x < world.mapSize.width; x++){
            for (int y = 0; y < world.mapSize.height; y++) {
                colliders[x][y].update();
            }
        }
    }

    //Used to detect the projectile hitting the environment
    public boolean projectileMove(Rectangle arrow, int height){
        //Get the x and y of the closet tile of the projectile
        int tileX = Math.floorMod(((arrow.x + arrow.width / 2) / world.tileSize) , world.mapSize.width);
        int tileY = Math.floorMod(((arrow.y + arrow.height / 2) / world.tileSize), world.mapSize.height);


        //Work out the surrounding tiles of the closest tile
        int minX =  (tileX - arrow.width / world.tileSize * 2) % world.mapSize.width;
        int maxX =  (tileX + arrow.width / world.tileSize * 2 + 1) % world.mapSize.width;
        int minY =  (tileY - arrow.height / world.tileSize * 2 % world.mapSize.height);
        int maxY =  (tileY + arrow.height / world.tileSize * 2 + 1) % world.mapSize.height;

        //Make sure the minimum x and minimum y tile
        minX = minX < 0 ? 0 : minX;
        minY = minY < 0 ? 0 : minY;

        //Only check the surrounding tiles
        for(int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                //Check if the tile height is the same and has no collisions
                //if so ignore this tile
                if(height != -1){
                    if (!world.heightCollisions[height][y][x] && !world.collisions[y][x]) {
                        continue;
                    }
                }
                //Don't collide if it is a ladder
                if(height == -1 && !world.collisions[y][x]){
                    continue;
                }

                //Return true if the tile and projectile collide
                BoxController box = colliders[y][x];
                if(box.getRect().intersects(arrow)){
                    return true;
                }
            }
        }
        return false;
    }

    public Vector2 move(Vector2 velocity, Rectangle character, BoxController entity){
        //Get the x and y of the closest tile
        int tileY = Math.floorMod(((character.y + character.width / 2) / world.tileSize) , world.mapSize.width);
        int tileX = Math.floorMod(((character.x + character.height / 2) / world.tileSize), world.mapSize.height);

        //Get the height of a tile and check if it is a death tile
        int boxHeight = world.heightMap[tileY][tileX];
        entity.setDeath(world.death[tileY][tileX]);
        //Set the height of the player to be the height of the tile
        entity.setHeight(boxHeight);

        int minX =  (tileX - character.height / world.tileSize * 2) % world.mapSize.height;
        int maxX =  tileX + character.height / world.tileSize * 2 + 1;
        int minY =  (tileY - character.width / world.tileSize * 2 % world.mapSize.width);
        int maxY =  tileY + character.width / world.tileSize * 2 + 1;
        minX = minX < 0 ? 0 : minX;
        minY = minY < 0 ? 0 : minY;

        if(maxX > world.mapSize.height){
            maxX = world.mapSize.height;
        }

        if(maxY > world.mapSize.width){
            maxY = world.mapSize.width;
        }

        //Get the calculate the vertex positions of the player with height offset and skin width
        Origins origins = new Origins(character, playerHeightOffset, skinWidth);
        for(int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if(boxHeight != -1){
                    //Check if the tile height is the same and has no collisions
                    //if so ignore this tile
                    if (!world.heightCollisions[boxHeight][y][x] && !world.collisions[y][x]) {
                        continue;
                    }
                }
                //Don't collide if it is a ladder
                if(boxHeight == -1 && !world.collisions[y][x]){
                    continue;
                }
                BoxController box = colliders[y][x];
                //Collide the entity and tile with the given velocity
                velocity = collideBoxes(box, velocity, origins);
            }
        }

        //Collide with other entities without a height offset
        Origins characterOrigins = new Origins(character, 0, skinWidth);
        for(int i= 0; i < entities.size(); i++){
            if(entities.get(i) != entity){
                velocity = collideBoxes(entities.get(i), velocity, characterOrigins);
            }
        }
        return velocity;
    }



    private Vector2 collideBoxes(BoxController box, Vector2 velocity, Origins origins){
        //Calculate horizontal collisions
        Vector2 horizontalOriginBot = (velocity.x < 0) ? origins.botLeft : origins.botRight;
        Vector2 horizontalOriginTop = (velocity.x < 0) ? origins.topLeft : origins.topRight;

        if (contains(horizontalOriginBot.x + velocity.x + Math.signum(velocity.x) * skinWidth, horizontalOriginBot.y, box.getRect()) || contains(horizontalOriginTop.x + velocity.x + Math.signum(velocity.x) * skinWidth, horizontalOriginTop.y, box.getRect())) {
            velocity.x = 0;
        }

        //Calculate vertical collisions
        Vector2 verticalOriginLeft = (velocity.y < 0) ? origins.topLeft : origins.botLeft;
        Vector2 verticalOriginRight = (velocity.y < 0) ? origins.topRight : origins.botRight;

        if (contains(verticalOriginLeft.x, verticalOriginLeft.y + velocity.y + Math.signum(velocity.y), box.getRect()) || contains(verticalOriginRight.x, verticalOriginRight.y + velocity.y + Math.signum(velocity.y), box.getRect())) {
            velocity.y = 0;
        }
        return velocity;
    }

    public void detectItemCollision(BoxController player){
        //Detect if the player has hit any items if so check if it can be triggered then trigger and use item
        for(int i = 0; i < itemManager.items.size(); i++) {
            if (player.getRect().intersects(itemManager.items.get(i).getRect())) {
                if(itemManager.items.get(i).canTrigger()) {
                    itemManager.items.get(i).triggerItem();
                    itemManager.useItem(itemManager.items.get(i));
                }
            }
        }
    }

    //Detect if the player attack attackControllers are overlapping and is active
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

    // Detect if the enemies hitboxes are overlapping and active
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

    //Detect if the enemy projectiles are colliding with the player
    public boolean detectEnemyProjectileCollision(BoxController boxController){
        for (int i= 0; i < projectileManager.projectiles.size(); i++){
            //Ignore the original archer for collisions
            if(projectileManager.projectiles.get(i).archer == boxController){
                continue;
            }
            //if projectile hit player return true
            if(projectileManager.projectiles.get(i).view.getBounds().intersects(boxController.getRect())){
                projectileManager.destroyProjectile(projectileManager.projectiles.get(i));
                return true;
            }
        }
        return false;
    }

    public boolean detectPlayerProjectileCollision(BoxController enemy){
        for (int i= 0; i < projectileManager.projectiles.size(); i++){
            //Ignore the player for collisions
            if(projectileManager.projectiles.get(i).archer != player){
                continue;
            }
            //if projectile hit enemy return true
            if (projectileManager.projectiles.get(i).view.getBounds().intersects(enemy.getRect())) {
                projectileManager.destroyProjectile(projectileManager.projectiles.get(i));
                return true;
            }
        }
        return false;
    }


    //Return true if a point is inside a rectangle bound
    private boolean contains(double x, double y, Rectangle rectangle){
        return rectangle.x <= x && x <= rectangle.x + rectangle.width &&
                rectangle.y <= y && y <= rectangle.y + rectangle.height;
    }
}
