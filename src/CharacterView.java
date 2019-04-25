import javax.swing.*;
import java.awt.*;

public class CharacterView extends JPanel {
    public CharacterModel model;
    public double deltaTime;
    public CharacterView(Rectangle transform, CharacterModel model) {
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyController());
        this.setPreferredSize(new Dimension(transform.width,transform.height));
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.black);
        g2.fillRect(model.baseTranform.x, model.baseTranform.y, model.baseTranform.width, model.baseTranform.height);
    }

    public void moveWorld(int newX, int newY) {
        if(model != null)
            this.setLocation(model.baseTranform.x + newX, model.baseTranform.y + newY);
    }

    public Point getPos() {
        return new Point(this.getX(), this.getY());
    }
}
