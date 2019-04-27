import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuHighScoreView extends JPanel {
    MainController parent;
    public MenuHighScoreView(MainController parent, int[] highScores) {
        this.parent = parent;
        this.setBounds(0,0,parent.screenSize.width,parent.screenSize.height);
        this.setOpaque(true);
        this.setFocusable(true);
        this.setLayout(null);

        Font buttonTextFont = new Font("sans", Font.PLAIN, 16);
//        Font textFontLarge = new Font("serif", Font.BOLD, 50);
        Font textFontSmall = new Font("serif", Font.PLAIN, 22);

        JLabel pausedText = new JLabel("High Scores", SwingConstants.CENTER);
        pausedText.setBounds(
                (parent.screenSize.width - 200)/2, 125, 200, 100);
        pausedText.setForeground(Color.WHITE);
        Font textFont = new Font("serif", Font.PLAIN, 40);
        pausedText.setFont(textFont);
        this.add(pausedText);

        JPanel textContainer = new JPanel();
        textContainer.setOpaque(false);
        GridLayout textContainerLayout = new GridLayout(10,1);
        textContainerLayout.setVgap(20);
        textContainer.setLayout(textContainerLayout);

        for(int i = 0; i < highScores.length; i++) {
            JLabel highScoreEntry = new JLabel(String.format("%08d", highScores[i]), SwingConstants.CENTER);
            highScoreEntry.setForeground(Color.WHITE);
            highScoreEntry.setFont(textFontSmall);
            textContainer.add(highScoreEntry);
        }
        textContainer.setBounds(
                (parent.screenSize.width - 400)/2, (parent.screenSize.height - 400)/2, 400, 400);
        this.add(textContainer);

        // Buttons
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
        buttonResume.setSize(new Dimension(400,75));
        buttonResume.setLocation((parent.screenSize.width - 400)/2, parent.screenSize.height - 200);
        this.add(buttonResume);

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
        g2.fillRect(0,0,getWidth(), getHeight());
    }

    private void buttonMainMenuAction() {
        parent.updateState(MainController.GameState_t.INIT_MAIN_MENU);
    }
}
