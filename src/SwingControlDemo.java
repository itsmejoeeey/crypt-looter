import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SwingControlDemo {
    public double deltaTime = 0;
    private float x = 0;
    private float y = 0;
    JButton button;
    JTextArea displayArea;
    JFrame f;

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
        p.add(displayArea);
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
