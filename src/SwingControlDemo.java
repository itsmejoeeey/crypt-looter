import javax.swing.*;

public class SwingControlDemo {
    public double deltaTime = 0;

    private float x = 0;
    private float y = 0;

    JFrame frame = new JFrame();
    JButton button = new JButton("Button");
    JPanel panel = new JPanel();

    public float speed = 0.05f;

    public void showEventDemo() {
        frame.setDefaultCloseOperation(3);
        frame.setSize(2880, 1800);
        frame.setVisible(true);

        button.setBounds(40, 100, 100, 60);
        button.setEnabled(false);

        panel.setLayout(null);
        panel.setFocusable(true);
        panel.addKeyListener(new KeyController());

        panel.add(button);
        frame.add(panel);
    }

    public void Update() {
        // Check for changes in key states
        if (KeyStates.moveRightKey.keyState())
            x += deltaTime * speed;
        if (KeyStates.moveLeftKey.keyState())
            x -= deltaTime * speed;
        if (KeyStates.moveForwardKey.keyState())
            y -= deltaTime * speed;
        if (KeyStates.moveBackwardsKey.keyState())
            y += deltaTime * speed;

        // Set new button position
        button.setBounds(40 + (int) x, 100 + (int) y, 100, 60);
    }
}
