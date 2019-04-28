package crypt_looter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class SmallHealthPotionView extends ItemView {
    String texturePath = "textures/potions.png";
    public SmallHealthPotionView(Rectangle bounds){
        super(bounds);
        initView();
    }

    public void initView(){
        try {
            texture = ImageIO.read(getClass().getClassLoader().getResourceAsStream(texturePath));
            texture = texture.getSubimage(0, 0, texture.getWidth()/2, texture.getHeight());
        } catch (IOException ex) {
            return;
        }
    }
}
