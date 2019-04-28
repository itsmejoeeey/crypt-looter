package crypt_looter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class CoinView extends ItemView {
    String texturePath = "textures/coin.png";
    public CoinView(Rectangle bounds){
        super(bounds);
        initView();
    }

    public void initView(){
        try {
            texture = ImageIO.read(getClass().getClassLoader().getResourceAsStream(texturePath));
        } catch (IOException ex) {
            return;
        }
    }
}
