import javax.swing.*;
import java.awt.*;

public class MainController {
    public double deltaTime = 0;

    JFrame frame = new JFrame();

    WorldController world;
    CameraController camera;
    CharacterController character;
    BoxManager boxManager;
    BoxController box;
    BoxController box1;

    public MainController() {
        world = new WorldController();
        character = new CharacterController();

        box = new BoxController(1650, 1500, 50, 50, true);
        box1 = new BoxController(1750, 1500, 50, 100, true);
        boxManager = new BoxManager();
        character.boxManager = boxManager;
        boxManager.colliders.add(box);
        boxManager.colliders.add(box1);

        frame.setDefaultCloseOperation(3);
        frame.setSize(1440, 900);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.addKeyListener(new KeyController());

        camera = new CameraController(world, character, frame.getSize());

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().add(box.view); //TODO getview
        world.getView().add(box1.view);
    }

    public void update() {
        world.deltaTime = this.deltaTime;
        character.deltaTime = this.deltaTime;
        camera.deltaTime = this.deltaTime;
        character.update();
        world.update();
        box.update();
        box1.update();
        camera.update();
    }
}
