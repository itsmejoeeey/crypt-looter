import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Thread.sleep;

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

    public Dimension screenSize = new Dimension(1440, 900);

    MenuPauseController pauseMenu;

    enum GameState_t {
        PAUSED, MAIN_MENU, NORMAL_GAME, GAME_EXIT_CONFIRMATION
    }

    GameState_t state = GameState_t.NORMAL_GAME;
    GameState_t prevState = GameState_t.NORMAL_GAME;

    Cursor blankCursor = frame.getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB), new Point(0,0), null);
    Cursor defaultCursor = Cursor.getDefaultCursor();

    public MainController() {
        if(runningOnWindows) {
            ImageIcon icon = new ImageIcon("src/res/icon.png");
            frame.setIconImage(icon.getImage());
        }

        MapReader mapReader;
        try {
            mapReader = new MapReader();
        } catch (IOException | InvalidMapException ex) {
            // Invalid (or incorrect path to) map
            return;
        }

        frame.setDefaultCloseOperation(3);

        // Add some extra pixels to account for titlebar and window border
        frame.setSize(screenSize.width + 10, screenSize.height + 37);

        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(null);

        // Set frame background (visible when using maps smaller than screen size)
        frame.getContentPane().setBackground(Color.BLACK);

        frame.addKeyListener(new KeyController());

        world = new WorldController(this, mapReader.getWorld());
        character = new CharacterController(new Point(500,500));

        box = new BoxController(1650, 1500, 50, 50, true);
        box1 = new BoxController(1750, 1500, 50, 100, true);
        boxManager = new BoxManager();
        character.boxManager = boxManager;
        boxManager.colliders.add(box);
        boxManager.colliders.add(box1);

        if(!runningOnWindows) {
            ImageIcon icon = new ImageIcon("src/res/icon.png");
            frame.setIconImage(icon.getImage());
        }

        frame.setTitle("Crypt Looter");

        camera = new CameraController(mapReader.getWorld(), world, character, screenSize);

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().add(box.view); //TODO getview
        world.getView().add(box1.view);

        // Needed after adding components to frame
        frame.revalidate();
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
                    frame.repaint();
                    frame.revalidate();
                }
                break;
            case PAUSED:
                pauseMenu = new MenuPauseController(this);
                frame.getLayeredPane().add(pauseMenu.getView(), new Integer(1));
                frame.repaint();
                break;
        }
    }

    private void update_normalgame() {
        // Hide cursor
        frame.setCursor(blankCursor);

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
        // Hide cursor
        frame.setCursor(defaultCursor);

        pauseMenu.update();
    }
}
