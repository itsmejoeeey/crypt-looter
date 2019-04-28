package crypt_looter;

import javax.swing.*;
import java.awt.*;

public class MenuPauseView extends JPanel {
    public MenuPauseView(MainController parent) {
        this.setBounds(0,0,parent.screenSize.width,parent.screenSize.height);
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(null);

        JLabel pausedText = new JLabel("paused", SwingConstants.CENTER);
        pausedText.setBounds(
                (parent.screenSize.width - 200)/2, (parent.screenSize.height - 100)/2, 200, 100);
        pausedText.setForeground(Color.WHITE);
        Font textFont = new Font("serif", Font.BOLD + Font.ITALIC, 40);
        pausedText.setFont(textFont);
        this.add(pausedText);

        this.repaint();
        this.revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Transparency needs to be done here to prevent weird artifacts and behaviour
        // https://tips4java.wordpress.com/2009/05/31/backgrounds-with-transparency/
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setComposite(AlphaComposite.SrcOver.derive(0.70f));
        g2.fillRect(0,0,getWidth(), getHeight());
    }
}
