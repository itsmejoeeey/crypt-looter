import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CharacterModel {
    public Rectangle baseTranform;
    public BufferedImage bufferedImage;

    CharacterModel(Rectangle trasform){
        baseTranform = trasform;
        try {
            bufferedImage = ImageIO.read(getClass().getResourceAsStream("/enemy.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
