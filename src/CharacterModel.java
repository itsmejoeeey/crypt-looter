import java.awt.*;

public class CharacterModel {
    public Rectangle baseTransform;

    private float x = 0;
    private float y = 0;
    public int direction = 4;
    public boolean attackDagger = false;
    public boolean daggerEquipped = false;
    public boolean attackBow = false;
    public boolean bowEquipped = false;
    public boolean walking = false;
    public boolean dead = false;

    public double attackTimer = 0;
    public double projectileTimer = 0;
    int height;

    public int maxHealth = 3;

    public int score = 0;
    public int health = maxHealth;

    public boolean weapon1Available = false;
    public boolean weapon2Available = false;

    public int secondsElapsed = 0;

    CharacterModel(Rectangle transform, int height){
        this.height = height;
        baseTransform = transform;
        x = baseTransform.x;
        y = baseTransform.y;
    }

    public void increaseScore(int increaseAmount) {
        score += increaseAmount;
    }

    public void increaseHealth(int increaseAmount) {
        if((health + increaseAmount) > maxHealth) {
            health = 3;
        } else {
            health += increaseAmount;
        }
    }

    public Rectangle getTransform(){
        return new Rectangle((int) x, (int) y, baseTransform.width, baseTransform.height);
    }

    public void moveWorld(double deltaX, double deltaY){
        x += deltaX;
        y += deltaY;
        baseTransform.setLocation((int) x, (int)y);
    }

    public void setWorld(int setX, int setY){
        x = setX;
        y = setY;
        baseTransform.setLocation((int) x, (int)y);

    }

    public int getX(){
        return (int) (x);
    }

    public int getY(){
        return (int) (y);
    }


    public void decreaseHealth(int decreaseAmount) {
        if((health - decreaseAmount) <= 0) {
            health = 0;
            dead = true;
        } else {
            health -= decreaseAmount;
        }
    }
}