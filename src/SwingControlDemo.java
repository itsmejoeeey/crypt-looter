import javax.swing.*;

public class SwingControlDemo {


    public double deltaTime = 0;

    JFrame frame = new JFrame();
    JButton button = new JButton("Button");
    JPanel panel = new JPanel();

    WorldController world;

    public SwingControlDemo() {
        world = new WorldController();
        frame.setDefaultCloseOperation(3);
        frame.setSize(2880, 1800);
        frame.setVisible(true);
        frame.setLayout(null);

        button.setBounds(40, 100, 100, 60);
        button.setEnabled(false);

        panel.setLayout(null);
        panel.setFocusable(true);
        panel.addKeyListener(new KeyController());

        panel.add(button);



        frame.add(world.getView());
        //frame.add(panel);
    }

    //public float speed = 0.05f;


    public void update() {
        world.deltaTime = this.deltaTime;
        world.update();
    }
}
