import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainController {
    // EXPERIMENTAL SHOULD NOT STAY
    // TODO FIX AND REMOVE
    // ENABLE THIS FLAG IF RUNNING ON WINDOWS - FIXES MOVEMENT
    boolean runningOnWindows = true;

    public double deltaTime = 0;

    JFrame frame = new JFrame();

    WorldController world;
    CameraController camera;
    CharacterController character;
    BoxManager boxManager;
    BoxController box;
    BoxController box1;

    MenuPauseController pauseMenu;

    enum GameState_t {
        PAUSED, MAIN_MENU, NORMAL_GAME, GAME_EXIT_CONFIRMATION
    }

    GameState_t state = GameState_t.NORMAL_GAME;
    GameState_t prevState = GameState_t.NORMAL_GAME;

    public MainController() {
        if(runningOnWindows) {
            ImageIcon icon = new ImageIcon("src/res/icon.png");
            frame.setIconImage(icon.getImage());
        }

        world = new WorldController(this);
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

        if(!runningOnWindows) {
            ImageIcon icon = new ImageIcon("src/res/icon.png");
            frame.setIconImage(icon.getImage());
        }

        frame.setTitle("Crypt Looter");

        camera = new CameraController(world, character, frame.getSize());

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().add(box.view); //TODO getview
        world.getView().add(box1.view);

        // Hide cursor
        Cursor blankCursor = frame.getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB), new Point(0,0), null);
        frame.setCursor(blankCursor);
    }

    public void update() {
        switch(state) {
            case NORMAL_GAME:
                update_normalgame();
                break;
            case PAUSED:
                update_paused();
                break;
        }
    }

    public void updateState(GameState_t newState) {

        prevState = state;
        state = newState;
        switch(newState) {
            case NORMAL_GAME:
                if(prevState == GameState_t.PAUSED) {
                    frame.getLayeredPane().remove(pauseMenu.getView());
                }
                break;
            case PAUSED:
                pauseMenu = new MenuPauseController(this);
                frame.getLayeredPane().add(pauseMenu.getView(), new Integer(1));
                break;
        }
    }

    private void update_normalgame() {
        world.deltaTime = this.deltaTime;
        character.deltaTime = this.deltaTime;
        camera.deltaTime = this.deltaTime;

        character.update();
        world.update();
        box.update();
        box1.update();
        camera.update();
    }

    private void update_paused() {
        pauseMenu.update();
    }
}
