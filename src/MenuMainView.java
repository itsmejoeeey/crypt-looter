import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuMainView extends JPanel {
    MainController parent;

    BufferedImage menuBackground;

    JLabel runtimeString;

    public MenuMainView(MainController parent) {
        this.parent = parent;

        this.setBounds(0,0,parent.screenSize.width,parent.screenSize.height);
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(null);

        // Get menu background
        try {
            menuBackground = ImageIO.read(new File("src/res/ui/main_menu.png"));
        } catch (IOException ex) {
            // Invalid image file path;
            return;
        }

        Font buttonTextFont = new Font("sans", Font.PLAIN, 16);

        JPanel buttonContainer = new JPanel();
        buttonContainer.setOpaque(false);
        GridLayout buttonContainerLayout = new GridLayout(4,1);
        buttonContainerLayout.setVgap(20);
        buttonContainer.setLayout(buttonContainerLayout);

        // Buttons
        JButton buttonPlay = new JButton("Play!");
        buttonPlay.setFocusable(false);
        buttonPlay.setOpaque(true);
        buttonPlay.setContentAreaFilled(false);
        buttonPlay.setForeground(Color.WHITE);
        buttonPlay.setFont(buttonTextFont);
        buttonPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPlayAction();
            }
        });
        buttonContainer.add(buttonPlay);

        JButton buttonOpenMap = new JButton("Open a custom map");
        buttonOpenMap.setFocusable(false);
        buttonOpenMap.setOpaque(true);
        buttonOpenMap.setContentAreaFilled(false);
        buttonOpenMap.setForeground(Color.WHITE);
        buttonOpenMap.setFont(buttonTextFont);
        buttonOpenMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonOpenMapAction();
            }
        });
        buttonContainer.add(buttonOpenMap);

        JButton buttonHighScores = new JButton("View high scores");
        buttonHighScores.setFocusable(false);
        buttonHighScores.setOpaque(true);
        buttonHighScores.setContentAreaFilled(false);
        buttonHighScores.setForeground(Color.WHITE);
        buttonHighScores.setFont(buttonTextFont);
        buttonHighScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonHighScoresAction();
            }
        });
        buttonContainer.add(buttonHighScores);

        JButton buttonExitDesktop = new JButton("Exit to desktop");
        buttonExitDesktop.setFocusable(false);
        buttonExitDesktop.setOpaque(true);
        buttonExitDesktop.setContentAreaFilled(false);
        buttonExitDesktop.setForeground(Color.WHITE);
        buttonExitDesktop.setFont(buttonTextFont);
        buttonExitDesktop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonExitDesktopAction();
            }
        });
        buttonContainer.add(buttonExitDesktop);

        buttonContainer.setSize(new Dimension(400, 400));
        buttonContainer.setLocation(
                (parent.screenSize.width - buttonContainer.getWidth())/2,
                (parent.screenSize.height - buttonContainer.getHeight())/2 + 150
        );
        this.add(buttonContainer);

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
        g2.drawImage(menuBackground, 0, 0, menuBackground.getWidth(), menuBackground.getHeight(), null);
    }

    private void buttonPlayAction() {
        parent.updateState(MainController.GameState_t.INIT_NORMAL_GAME);
    }
    private void buttonOpenMapAction() {

    }
    private void buttonHighScoresAction() {
        parent.updateState(MainController.GameState_t.HIGH_SCORES);
    }
    private void buttonExitDesktopAction() {
        // Kill the program in a swift stroke
        System.exit(0);
    }
}
