package crypt_looter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class DaggerView extends ItemView{
    String texturePath = "textures/sword.png";
    public DaggerView(Rectangle bounds){
        super(bounds);
        initView();
        setBounds(getX(), getY(), 32, 32);
    }

    public void initView(){
        try {
            texture = ImageIO.read(getClass().getClassLoader().getResourceAsStream(texturePath));
        } catch (IOException ex) {
            return;
        }
    }

    protected void paintComponent(Graphics g) {
        setLocation(bounds.x, bounds.y);

        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Default rectangle
        g2.setColor(Color.black);
        if(texture == null)
            g2.fillRect(0, 0, getWidth(), getHeight());
        else
            g2.drawImage(texture, 0, 0, 32, 32, null);
    }
}
