import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character {
    public Rectangle baseTranform;
    public  int height;

    static int maxHealth = 3;

    public int score = 0;
    public int health = maxHealth;

    public boolean weapon1Available = false;
    public boolean weapon2Available = false;

    public int secondsElapsed = 0;

    Character(Rectangle transform, int height){
        baseTranform = transform;
        this.height = height;
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