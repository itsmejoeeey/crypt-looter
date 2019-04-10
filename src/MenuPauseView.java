import javax.swing.*;
import java.awt.*;

public class MenuPauseView extends JPanel {
    public MenuPauseView() {
        this.setBounds(0,0,1440,900);
        this.setOpaque(false);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Transparency needs to be done here to prevent weird artifacts and behaviour
        // https://tips4java.wordpress.com/2009/05/31/backgrounds-with-transparency/
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setComposite(AlphaComposite.SrcOver.derive(0.75f));
        g2.fillRect(0,0,getWidth(), getHeight());
    }
}
