import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BowView extends ItemView {
    String texturePath = "src/res/textures/bow.png";
    public BowView(Rectangle bounds){
        super(bounds);
        initView();
        setBounds(getX(), getY(), 32, 32);
    }

    public void initView(){
        try {
            texture = ImageIO.read(new File(texturePath));
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
