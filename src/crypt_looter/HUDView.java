package crypt_looter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HUDView extends JPanel {
    private MainController parent;
    private CharacterModel character;

    ImageIcon bowIcon;
    ImageIcon daggerIcon;

    ImageIcon heartRedIcon;
    ImageIcon heartBlackIcon;

    boolean timerRunning = false;

    int previousCharacterHealth;

    JLabel[] heartViews;
    JLabel itemIcon1;
    JLabel itemIcon2;
    JLabel scoreText;

    static int itemBoxInitialX = 120;

    public HUDView(MainController parent, CharacterModel character) {
        this.parent = parent;
        this.character = character;

        this.setBounds(0,0,parent.screenSize.width,parent.screenSize.height);
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(null);

        previousCharacterHealth = character.health;

        // Define fonts
        Font textFontSmall = new Font("sans", Font.BOLD, 25);
        Font textFontLarge = new Font("sans", Font.BOLD, 90);

        // Load in images
        BufferedImage bowImage;
        BufferedImage daggerImage;
        BufferedImage heartRedImage;
        BufferedImage heartBlackImage;
        try {
            bowImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("icons/bow_icon.png"));
            daggerImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("icons/dagger_icon.png"));
            heartRedImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("icons/heart_red.png"));
            heartBlackImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("icons/heart_black.png"));
        } catch (IOException ex) {
            return;
        }
        // Resize images now to conserve performance for later on
        bowIcon = new ImageIcon(bowImage.getScaledInstance(60,60, Image.SCALE_SMOOTH));
        daggerIcon = new ImageIcon(daggerImage.getScaledInstance(60,60, Image.SCALE_SMOOTH));
        heartRedIcon = new ImageIcon(heartRedImage.getScaledInstance(70,70, Image.SCALE_SMOOTH));
        heartBlackIcon = new ImageIcon(heartBlackImage.getScaledInstance(70,70, Image.SCALE_SMOOTH));

        // Item 1 control text
        JLabel controlText1 = new JLabel("J", SwingConstants.CENTER);
        controlText1.setBounds(
                57, parent.screenSize.height - 85, 200, 100);
        controlText1.setForeground(Color.WHITE);
        controlText1.setFont(textFontSmall);
        this.add(controlText1);

        // Item 1 icon
        itemIcon1 = new JLabel();
        itemIcon1.setBounds(itemBoxInitialX + 7, parent.screenSize.height - 118, 60, 60);
        this.add(itemIcon1);

        // Item 2 control text
        JLabel controlText2 = new JLabel("K", SwingConstants.CENTER);
        controlText2.setBounds(
                127, parent.screenSize.height - 85, 200, 100);
        controlText2.setForeground(Color.WHITE);
        controlText2.setFont(textFontSmall);
        this.add(controlText2);

        // Item 2 icon
        itemIcon2 = new JLabel();
        itemIcon2.setBounds(itemBoxInitialX + 77, parent.screenSize.height - 118, 60, 60);
        this.add(itemIcon2);
        
        // Game score text
        scoreText = new JLabel("", SwingConstants.CENTER);
        scoreText.setBounds(
                parent.screenSize.width - 625, parent.screenSize.height - 140, 600, 100);
        scoreText.setForeground(Color.WHITE);
        scoreText.setFont(textFontLarge);
        this.add(scoreText);


        // Create heart images and add to the panel - we will set the image later in paintComponent()
        heartViews = new JLabel[character.maxHealth];
        for(int i = 0; i < character.maxHealth; i++) {
            JLabel heart = new JLabel();
            heart.setBounds(320 + (80*i), parent.screenSize.height - 123, 70, 70);
            this.add(heart);
            // Add to an array so we can keep track of them and change the heart icons later
            heartViews[i] = heart;
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

        // Immediately update variable if the playerModel has been healed
        if(previousCharacterHealth < character.health) {
            previousCharacterHealth = character.health;
        }
        // Show a translucent red flash on damage inflicted
        if(previousCharacterHealth > character.health) {
            g2.setColor(Color.RED);
            g2.setComposite(AlphaComposite.SrcOver.derive(0.50f));
            g2.fillRect(0,0, getWidth(),getHeight());
            g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            repaint();

            if(!timerRunning) {
                // After 100ms update previousCharacterHealth so the red disappears
                timerRunning = true;
                Timer timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        previousCharacterHealth = character.health;
                        timerRunning = false;
                        repaint();

                        // Stop the timer before it loops
                        ((Timer)e.getSource()).stop();
                    }
                });
                timer.start();
            }
        }

        g2.setColor(Color.BLACK);
        g2.setComposite(AlphaComposite.SrcOver.derive(0.80f));
        g2.fillRect(0,parent.screenSize.height - 100,getWidth(), 100);
        g2.setComposite(AlphaComposite.SrcOver.derive(1f));

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

        // Update display of the icons inside the item boxes
        if(character.daggerEquipped) {
            itemIcon1.setIcon(daggerIcon);
        } else {
            itemIcon1.setIcon(null);
        }
        if(character.bowEquipped) {
            itemIcon2.setIcon(bowIcon);
        } else {
            itemIcon2.setIcon(null);
        }

        // Update the hearts on screen to convey the character health
        for(int i = 0; i < character.health; i++) {
            heartViews[i].setIcon(heartRedIcon);
        }
        for(int i = character.health; i < character.maxHealth; i++) {
            heartViews[i].setIcon(heartBlackIcon);
        }

        // Update the score displayed on screen
        String scoreString = String.format("%08d", character.score);
        scoreText.setText(scoreString);
    }

    public void update() {

    }
}
