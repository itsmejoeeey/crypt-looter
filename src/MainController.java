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
    EnemyController enemy;

    public MainController() {
        world = new WorldController();
        character = new CharacterController();
        box = new BoxController(1650, 1500, 50, 50, true);
        box1 = new BoxController(1800, 1500, 150, 100, true);
        boxManager = new BoxManager();
        enemy = new EnemyController();
        character.boxManager = boxManager;
        boxManager.colliders.add(enemy.collider);
        boxManager.colliders.add(box);
        boxManager.colliders.add(box1);

        frame.setDefaultCloseOperation(3);
        frame.setSize(2880, 1800);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.addKeyListener(new KeyController());

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().add(box.view); //TODO getview
        world.getView().add(box1.view);
        world.getView().add(enemy.view);
    }

    public void update() {
        world.deltaTime = this.deltaTime;
        character.deltaTime = this.deltaTime;
        enemy.deltaTime = this.deltaTime;
        character.update();
        world.update();
        box.update();
        box1.update();
        enemy.update();
    }
}
