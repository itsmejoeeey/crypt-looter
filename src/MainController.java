import javax.swing.*;

public class MainController {


    public double deltaTime = 0;

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    WorldController world;
    CharacterController character;
    BoxManager boxManager;
    BoxController box;

    public MainController() {
        world = new WorldController();
        character = new CharacterController();
        box = new BoxController();
        boxManager = new BoxManager();
        character.boxManager = boxManager;
        boxManager.box = box.box;

        frame.setDefaultCloseOperation(3);
        frame.setSize(2880, 1800);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.addKeyListener(new KeyController());

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().add(box.view); //TODO getview
    }

    public void update() {
        world.deltaTime = this.deltaTime;
        character.deltaTime = this.deltaTime;
        character.update();
        world.update();
        box.update();
    }
}
