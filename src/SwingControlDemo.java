import javax.swing.*;

public class SwingControlDemo {
    public double deltaTime = 0;

    private float x = 0;
    private float y = 0;

    JFrame frame = new JFrame();
    JButton player = new JButton("player");
    JButton box = new JButton("box");
    JPanel panel = new JPanel();
    BoxController boxController = new BoxController();

    public float speed = 0.05f;

    public void showEventDemo() {
        frame.setDefaultCloseOperation(3);
        frame.setSize(2880, 1800);
        frame.setVisible(true);

        player.setBounds(40, 100, 100, 60);
        player.setEnabled(false);

        box.setBounds(250, 100, 100, 60);
        box.setEnabled(false);

        panel.setLayout(null);
        panel.setFocusable(true);
        panel.addKeyListener(new KeyController());

        panel.add(player);
        panel.add(box);
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

        boxController.player = player.getBounds();
        boxController.box = box.getBounds();

        Vector2 move = boxController.Collisions(new Vector2(x,y), (float) deltaTime * speed);
        System.out.println(x + " : " + y);
        // Set new button position
        player.setBounds(40 + (int) move.x, 100 + (int) move.y, 100, 60);
    }
}
