import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Character {
    public Rectangle baseTranform;
    public int height;
    public BufferedImage bufferedImage;

    Character(Rectangle transform, int height){
        baseTranform = transform;
        this.height = height;
        /*
        try {
            bufferedImage = ImageIO.read(getClass().getResourceAsStream("/enemy.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        */
    }
}