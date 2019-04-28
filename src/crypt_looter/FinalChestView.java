package crypt_looter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FinalChestView extends ItemView {
    String texturePath = "textures/treasure_chest.png";
    public FinalChestView(Rectangle bounds){
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

    public void trigger(){
        try {
            texture = ImageIO.read(getClass().getClassLoader().getResourceAsStream(texturePath));
            texture = texture.getSubimage(texture.getWidth()/2, 0, texture.getWidth()/2, texture.getHeight());
            repaint();
        } catch (IOException ex) {
            return;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        setLocation(bounds.x, bounds.y);

        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Default rectangle
        g2.setColor(Color.black);
        g2.drawImage(texture, -2, -2, 35, 35, null);

    }
}
