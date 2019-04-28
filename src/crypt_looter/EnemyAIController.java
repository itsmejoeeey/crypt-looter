package crypt_looter;

import java.awt.*;

public class EnemyAIController {
    public CharacterModel enemy;
    public CharacterModel player;
    public World world;
    public int attackDistance = 300;

    public EnemyAIController(CharacterModel enemy, CharacterModel player){
        this.enemy = enemy;
        this.player = player;
    }

    //Get move vector for the enemy
    //returns a vector that points towards the player snapped to the 8 directions
    public Vector2 move (int height){
        if(!canAttack(attackDistance, height)){
            return new Vector2(0, 0);
        }
        float x = (player.getX() - enemy.getX());
        float y = (player.getY() - enemy.getY());
        x = x != 0?Math.signum(x):0f;
        y = y != 0?Math.signum(y):0f;
        return new Vector2(x, y);
    }

    //Get direction to face when attacking
    //Faces player when attacking
    public Point attackDir(){
        float x = (player.getX() - enemy.getX());
        float y = (player.getY() - enemy.getY());

        if(Math.abs(x) > Math.abs(y)){
            return new Point((int) Math.signum(x), 0);
        } else {
            return new Point(0,(int) Math.signum(y));
        }
    }

    //Check if the enemy is within a distance and on the same height as the player
    public boolean canAttack(int attackDistance, int height){

        if(height == player.height || (height == -1 || player.health == -1)) {
            float x = (player.getX() - enemy.getX());
            float y = (player.getY() - enemy.getY());
            if (Math.sqrt(x * x + y * y) < attackDistance) {
                return true;
            }
        }
        return false;
    }
}
