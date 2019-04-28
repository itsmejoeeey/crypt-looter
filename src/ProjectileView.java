import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProjectileView extends JPanel {
    String texturePath = "src/res/textures/fireball.png";
    String playerTexturePath = "src/res/textures/arrow.png";
    BufferedImage texture;
    Rectangle origin;
    public ProjectileView(Rectangle bounds, int direction, boolean player){
        this.origin = bounds;
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(new Dimension(bounds.width, bounds.height));
        if(player)
            this.texturePath = playerTexturePath;
        initView(direction);
    }

    private void initView(int direction){
        try {
            texture = ImageIO.read(new File(texturePath));
            if(texturePath == playerTexturePath)
                texture = texture.getSubimage(direction * texture.getWidth()/8,0, texture.getWidth()/8, texture.getHeight());
        } catch (IOException e){
            return;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);

        //setLocation(origin.x, origin.y);

        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Default rectangle
        g2.setColor(Color.black);
        g2.drawImage(texture, 0, 0, 20, 20, null);
    }

    public void moveWorld(int newX, int newY) {
        setLocation(newX, newY);
    }
}
