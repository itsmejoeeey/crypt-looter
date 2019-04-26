import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class MenuEscapeView extends JPanel {
    MainController parent;

    CharacterModel character;

    JLabel runtimeString;

    public MenuEscapeView(MainController parent, CharacterModel character) {
        this.parent = parent;
        this.character = character;

        this.setBounds(0,0,parent.screenSize.width,parent.screenSize.height);
        this.setOpaque(false);
        this.setFocusable(true);
        this.setLayout(null);

        Font buttonTextFont = new Font("sans", Font.PLAIN, 16);

        JPanel buttonContainer = new JPanel();
        buttonContainer.setOpaque(false);
        GridLayout buttonContainerLayout = new GridLayout(5,1);
        buttonContainerLayout.setVgap(20);
        buttonContainer.setLayout(buttonContainerLayout);

        // Game run time
        runtimeString = new JLabel("TIME GOES HERE", SwingConstants.CENTER);
        runtimeString.setForeground(Color.WHITE);
        runtimeString.setFont(buttonTextFont);
        buttonContainer.add(runtimeString);

        // Buttons
        JButton buttonResume = new JButton("Resume");
        buttonResume.setFocusable(false);
        buttonResume.setOpaque(true);
        buttonResume.setContentAreaFilled(false);
        buttonResume.setForeground(Color.WHITE);
        buttonResume.setFont(buttonTextFont);
        buttonResume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonResumeAction();
            }
        });
        buttonContainer.add(buttonResume);

        JButton buttonRestart = new JButton("Restart level");
        buttonRestart.setFocusable(false);
        buttonRestart.setOpaque(true);
        buttonRestart.setContentAreaFilled(false);
        buttonRestart.setForeground(Color.WHITE);
        buttonRestart.setFont(buttonTextFont);
        buttonRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonRestartAction();
            }
        });
        buttonContainer.add(buttonRestart);

        JButton buttonExitMenu = new JButton("Exit to main menu");
        buttonExitMenu.setFocusable(false);
        buttonExitMenu.setOpaque(true);
        buttonExitMenu.setContentAreaFilled(false);
        buttonExitMenu.setForeground(Color.WHITE);
        buttonExitMenu.setFont(buttonTextFont);
        buttonExitMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonExitMenuAction();
            }
        });
        buttonContainer.add(buttonExitMenu);

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
                (parent.screenSize.height - buttonContainer.getHeight())/2
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
        g2.setComposite(AlphaComposite.SrcOver.derive(0.80f));
        g2.fillRect(0,0,getWidth(), getHeight());

        // Update game time
        Calendar calendar = Calendar.getInstance();
        calendar.set(0,0,0,0,0,character.secondsElapsed);
        String runtime = new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
        runtimeString.setText("Time elapsed: " + runtime);

    }

    private void buttonResumeAction() {
        parent.updateState(MainController.GameState_t.NORMAL_GAME);
    }
    private void buttonRestartAction() {

    }
    private void buttonExitMenuAction() {
        parent.updateState(MainController.GameState_t.INIT_MAIN_MENU);
    }
    private void buttonExitDesktopAction() {
        // Kill the program in a swift stroke
        System.exit(0);
    }
}
