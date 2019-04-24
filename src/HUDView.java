import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HUDView extends JPanel {
    private MainController parent;
    private CharacterModel character;

    BufferedImage heartRedImage;
    BufferedImage heartBlackImage;

    public HUDView(MainController parent, CharacterModel character) {
        this.parent = parent;
        this.character = character;

        this.setBounds(0,0,parent.screenSize.width,parent.screenSize.height);
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(null);

        Font textFontSmall = new Font("sans", Font.BOLD, 25);
        Font textFontLarge = new Font("sans", Font.BOLD, 90);

        // Item 1 control text
        JLabel controlText1 = new JLabel("J", SwingConstants.CENTER);
        controlText1.setBounds(
                57, parent.screenSize.height - 85, 200, 100);
        controlText1.setForeground(Color.WHITE);
        controlText1.setFont(textFontSmall);
        this.add(controlText1);

        // Item 2 control text
        JLabel controlText2 = new JLabel("K", SwingConstants.CENTER);
        controlText2.setBounds(
                127, parent.screenSize.height - 85, 200, 100);
        controlText2.setForeground(Color.WHITE);
        controlText2.setFont(textFontSmall);
        this.add(controlText2);

        // Game score text
        JLabel scoreText = new JLabel("00000142", SwingConstants.CENTER);
        scoreText.setBounds(
                parent.screenSize.width - 625, parent.screenSize.height - 140, 600, 100);
        scoreText.setForeground(Color.WHITE);
        scoreText.setFont(textFontLarge);
        this.add(scoreText);

        try {
            heartRedImage = ImageIO.read(new File("src/res/heart_red.png"));
            heartBlackImage = ImageIO.read(new File("src/res/heart_black.png"));
        } catch (IOException ex) {

        }

        for(int i = 0; i < 3; i++) {
            JLabel heart = new JLabel();
            heart.setBounds(320 + (80*i), parent.screenSize.height - 123, 70, 70);
            heart.setIcon(new ImageIcon(heartRedImage.getScaledInstance(70,70, Image.SCALE_SMOOTH)));
            this.add(heart);
        }

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
        g2.setComposite(AlphaComposite.SrcOver.derive(0.80f));
        g2.fillRect(0,parent.screenSize.height - 100,getWidth(), 100);
        g2.setComposite(AlphaComposite.SrcOver.derive(1f));

        int itemBoxInitialX = 120;
        // Item 1 box
        g2.setColor(Color.BLACK);
        g2.fillRect(itemBoxInitialX,parent.screenSize.height - 125,75, 75);
        g2.setColor(Color.WHITE);
        g2.fillRect(itemBoxInitialX + 5,parent.screenSize.height - 120,65, 65);

        // Item 2 box
        g2.setColor(Color.BLACK);
        g2.fillRect(itemBoxInitialX + 70,parent.screenSize.height - 125,75, 75);
        g2.setColor(Color.WHITE);
        g2.fillRect(itemBoxInitialX + 75,parent.screenSize.height - 120,65, 65);
    }
}
