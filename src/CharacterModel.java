import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CharacterModel {
    public Rectangle baseTranform;
    public int direction = 4;
    public boolean attackDagger = false;
    public boolean attackBow = false;
    public boolean walking = false;
    public boolean dead = false;

    public double attackTimer = 0;
    int height;

    static int maxHealth = 3;

    public int score = 0;
    public int health = maxHealth;

    public boolean weapon1Available = false;
    public boolean weapon2Available = false;

    public int secondsElapsed = 0;

    CharacterModel(Rectangle transform, int height){
        this.height = height;
        baseTranform = transform;
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

    public void decreaseHealth(int decreaseAmount) {
        if((health - decreaseAmount) < 0) {
            health = 0;
        } else {
            health -= decreaseAmount;
        }
    }
}