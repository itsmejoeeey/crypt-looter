import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class SwingControlDemo {
    public double deltaTime = 0;
    private float x = 0;
    private float y = 0;
    JButton button;
    JTextField textField;
    JTextArea displayArea;
    JFrame f;
    InputDemo in;

    public float speed = 0.05f;

    public static final int W = 0;
    public static final int A = 1;
    public static final int S = 2;
    public static final int D = 3;
    public boolean[] keyButtons = new boolean[4];

    private class InputDemo implements KeyListener{

        public InputDemo(){
            System.out.print("Create");
            f.addKeyListener(this);
        }
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_W:
                    keyButtons[W] = true;
                    break;
                case KeyEvent.VK_A:
                    keyButtons[A] = true;
                    break;
                case KeyEvent.VK_S:
                    keyButtons[S] = true;
                    break;
                case KeyEvent.VK_D:
                    keyButtons[D] = true;
                    break;
            }

        }

        /** Handle the key-pressed event from the text field. */
        public void keyTyped(KeyEvent e) {

        }

        /** Handle the key-released event from the text field. */
        public void keyReleased(KeyEvent e) {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_W:
                    keyButtons[W] = false;
                    break;
                case KeyEvent.VK_A:
                    keyButtons[A] = false;
                    break;
                case KeyEvent.VK_S:
                    keyButtons[S] = false;
                    break;
                case KeyEvent.VK_D:
                    keyButtons[D] = false;
                    break;
            }
        }
    }

    public void showEventDemo() {
        f = new JFrame();
        JPanel p = new JPanel();
        p.setLayout(null);
        button = new JButton("Button");
        button.setBounds(40, 100, 100, 60);
        p.add(button);
        f.add(p);


        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.addKeyListener(new InputDemo());
        p.add(textField);
        // setLayout(null);
        f.setDefaultCloseOperation(3);
        f.setSize(2880, 1800);
        f.setVisible(true);
    }

    public void Update() {
        if (keyButtons[D])
            x += deltaTime * speed;
        if (keyButtons[A])
            x -= deltaTime * speed;
        if (keyButtons[W])
            y -= deltaTime * speed;
        if (keyButtons[S])
            y += deltaTime * speed;

        button.setBounds(40 + (int) x, 100 + (int) y, 100, 60);
        if (deltaTime != 0)
            button.setText(Double.toString(1/deltaTime));
    }
}
    /*

    public SwingControlDemo(){
        prepareGUI();
    }
    private void prepareGUI(){
        mainFrame = new JFrame("Java SWING Examples");
        mainFrame.setSize(1440,900);
        mainFrame.setLayout(new GridLayout(1, 1));


        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        controlPanel = new JPanel();
        controlPanel.setLocation(0,0);
        controlPanel.setLayout(null);

        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }
    public void showEventDemo(){
        headerLabel.setText("Control in action: Button");

        JButton okButton = new JButton("OK");
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        okButton.setBounds(100, 100, 50, 50);

        okButton.setActionCommand("OK");
        submitButton.setActionCommand("Submit");
        cancelButton.setActionCommand("Cancel");

        okButton.addActionListener(new ButtonClickListener());
        submitButton.addActionListener(new ButtonClickListener());
        cancelButton.addActionListener(new ButtonClickListener());

        controlPanel.add(okButton);
        controlPanel.add(submitButton);
        controlPanel.add(cancelButton);

        mainFrame.setVisible(true);
    }
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if( command.equals( "OK" ))  {
                statusLabel.setText("Ok Button clicked.");
            } else if( command.equals( "Submit" ) )  {
                statusLabel.setText("Submit Button clicked.");
            } else {
                statusLabel.setText("Cancel Button clicked.");
            }
        }
    }
    */
