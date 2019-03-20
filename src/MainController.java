import javax.swing.*;

public class MainController {


    public double deltaTime = 0;

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    WorldController world;
    CharacterController character;

    public MainController() {
        world = new WorldController();
        character = new CharacterController();

        frame.setDefaultCloseOperation(3);
        frame.setSize(2880, 1800);
        frame.setVisible(true);
        frame.setLayout(null);

        panel.setLayout(null);
        panel.setFocusable(true);
        panel.addKeyListener(new KeyController());

        frame.add(world.getView());
        world.getView().add(character.getView());
    }

    public void update() {
        world.deltaTime = this.deltaTime;
        character.deltaTime = this.deltaTime;
        character.update();
        world.update();
    }
}
