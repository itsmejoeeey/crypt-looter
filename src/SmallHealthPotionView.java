import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SmallHealthPotionView extends ItemView {
    String texturePath = "src/res/textures/potions.png";
    public SmallHealthPotionView(Rectangle bounds){
        super(bounds);
        initView();
    }

    public void initView(){
        try {
            texture = ImageIO.read(new File(texturePath));
            texture = texture.getSubimage(0, 0, texture.getWidth()/2, texture.getHeight());
        } catch (IOException ex) {
            return;
        }
    }
}
