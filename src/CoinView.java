import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CoinView extends ItemView {
    String texturePath = "src/res/textures/coin.png";
    public CoinView(Rectangle bounds){
        super(bounds);
        initView();
    }

    public void initView(){
        try {
            texture = ImageIO.read(new File(texturePath));
        } catch (IOException ex) {
            return;
        }
    }
}
