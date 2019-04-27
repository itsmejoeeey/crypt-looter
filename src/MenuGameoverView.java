import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuGameoverView extends JPanel {
    MainController parent;
    CharacterModel character;
    HighScoreController highScoreController;
    public MenuGameoverView(MainController parent, CharacterModel character, HighScoreController highScoreController) {
        this.parent = parent;
        this.character = character;
        this.highScoreController = highScoreController;

        this.setBounds(0,0,parent.screenSize.width,parent.screenSize.height);
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(null);

        Font buttonTextFont = new Font("sans", Font.PLAIN, 16);
        Font textFontLarge = new Font("serif", Font.BOLD, 50);
        Font textFontSmall = new Font("serif", Font.ITALIC, 22);

        JPanel buttonContainer = new JPanel();
        buttonContainer.setOpaque(false);
        GridLayout buttonContainerLayout = new GridLayout(5,1);
        buttonContainerLayout.setVgap(20);
        buttonContainer.setLayout(buttonContainerLayout);

        JLabel pausedText = new JLabel("GAME OVER", SwingConstants.CENTER);
        pausedText.setBounds(
                (parent.screenSize.width - 350)/2, (parent.screenSize.height - 100)/2, 350, 100);
        pausedText.setForeground(Color.WHITE);
        pausedText.setFont(textFontLarge);
        buttonContainer.add(pausedText);

        JLabel scoreText = new JLabel("You scored " + character.score, SwingConstants.CENTER);
        scoreText.setBounds(
                (parent.screenSize.width - 350)/2, (parent.screenSize.height - 100)/2, 350, 100);
        scoreText.setForeground(Color.WHITE);
        scoreText.setFont(textFontSmall);
        buttonContainer.add(scoreText);

        // Buttons
        JButton buttonSetName = new JButton("Set player name");
        buttonSetName.setFocusable(false);
        buttonSetName.setOpaque(true);
        buttonSetName.setContentAreaFilled(false);
        buttonSetName.setForeground(Color.WHITE);
        buttonSetName.setFont(buttonTextFont);
        buttonSetName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonSetNameAction();
            }
        });
        buttonContainer.add(buttonSetName);

        buttonContainer.setSize(new Dimension(400, 200));
        buttonContainer.setLocation(
                (parent.screenSize.width - buttonContainer.getWidth())/2,
                (parent.screenSize.height - buttonContainer.getHeight())/2
        );
        this.add(buttonContainer);

        JButton buttonResume = new JButton("Return to main menu");
        buttonResume.setFocusable(false);
        buttonResume.setOpaque(true);
        buttonResume.setContentAreaFilled(false);
        buttonResume.setForeground(Color.WHITE);
        buttonResume.setFont(buttonTextFont);
        buttonResume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonMainMenuAction();
            }
        });
        buttonContainer.add(buttonResume);

        buttonContainer.setSize(new Dimension(400, 200));
        buttonContainer.setLocation(
                (parent.screenSize.width - buttonContainer.getWidth())/2,
                (parent.screenSize.height - buttonContainer.getHeight())/2
        );
        this.add(buttonContainer);

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
                (parent.screenSize.height - buttonContainer.getHeight())/2 + 25
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
        g2.setComposite(AlphaComposite.SrcOver.derive(0.85f));
        g2.fillRect(0,0,getWidth(), getHeight());
    }

    private void buttonSetNameAction() {
        String playerName = JOptionPane.showInputDialog("Enter your name", "PLAYER");
        if(playerName == null) {
            return;
        }
        try {
            // Check string has been assigned and isn't empty
            if(!playerName.isEmpty() && playerName.replaceAll("[;., ]","").length() > 0) {
                // Remove all spaces from string
                playerName = playerName.replaceAll("[;., ]","");
                if(playerName.length() < 6) {
                    for(int i = 0; i <=  playerName.length(); i++) {
                        // Check string only contains characters
                        if (!Character.isLetter(playerName.charAt(i))) {
                            throw new IllegalArgumentException();
                        }
                    }}
            } else {
                throw new IllegalArgumentException();
            }
        } catch(IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid player name input.\n" +
                    "Name must contain only characters. Maximum name size 6 characters.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String highScoreString = String.format("%08d", character.score) + " " + playerName.toUpperCase();
        highScoreController.addHighScore(highScoreString);
        System.out.println(highScoreString);
    }
    private void buttonMainMenuAction() {
        parent.updateState(MainController.GameState_t.INIT_MAIN_MENU);
    }
    private void buttonExitDesktopAction() {
        // Kill the program in a swift stroke
        System.exit(0);
    }
}
