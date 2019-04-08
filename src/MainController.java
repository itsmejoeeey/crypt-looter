import javax.swing.*;
import java.awt.*;

public class MainController {


    public double deltaTime = 0;

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    WorldController world;
    CharacterController character;
    BoxManager boxManager;
    BoxController box;
    BoxController box1;

    public MainController() {
        world = new WorldController();
        character = new CharacterController();
        box = new BoxController(1650, 1500, 50, 50);
        box1 = new BoxController(1750, 1500, 50, 100);
        boxManager = new BoxManager();
        character.boxManager = boxManager;
        boxManager.colliders.add(box);
        boxManager.colliders.add(box1);

        frame.setDefaultCloseOperation(3);
        frame.setSize(2880, 1800);
        frame.setVisible(true);
        frame.setLayout(null);

        panel.setLayout(null);
        panel.setFocusable(true);
        panel.addKeyListener(new KeyController());

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().add(box.view); //TODO getview
        world.getView().add(box1.view);
    }

    public void update() {
        world.deltaTime = this.deltaTime;
        character.deltaTime = this.deltaTime;
        character.update();
        world.update();
        box.update();
        box1.update();
    }
}
