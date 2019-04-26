import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class MainController {
    public double deltaTime = 0;
    public Dimension screenSize = new Dimension(1440, 900);

    JFrame frame;

    // World objects
    WorldController world;
    CameraController camera;
    PlayerController character;
    EnemyManager enamyManager;
    BoxManager boxManager;
    SoundController sound;
    ItemManager itemManager;

    // UI
    HUDController hud;
    MenuPauseController pauseMenu;
    MenuEscapeController escapeMenu;

    enum GameState_t {
        PAUSED, INIT_MAIN_MENU, MAIN_MENU, NORMAL_GAME, INIT_NORMAL_GAME, ESCAPE, GAME_OVER
    }

    GameState_t state = GameState_t.INIT_MAIN_MENU;
    GameState_t prevState = GameState_t.INIT_MAIN_MENU;

    // Cursors
    Cursor blankCursor;
    Cursor defaultCursor;

    public MainController() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Add some extra pixels to account for titlebar and window border
        frame.setSize(screenSize.width + 10, screenSize.height + 37);

        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(null);

        // Set frame background (visible when using maps smaller than screen size)
        frame.getContentPane().setBackground(Color.BLACK);

        blankCursor = frame.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), null);
        defaultCursor = Cursor.getDefaultCursor();
    }

    public void update() {
        // Not entirely sure why this is needed here, but the state machine WILL
        // have a fit if not here in some capacity :))
        System.out.print("");

        switch(state) {
            case INIT_MAIN_MENU:
                init_main_menu();
                break;
            case MAIN_MENU:
                // No tasks required
                break;
            case INIT_NORMAL_GAME:
                init_normalgame();
                break;
            case NORMAL_GAME:
                update_normalgame();
                break;
            case PAUSED:
                update_paused();
                break;
            case ESCAPE:
                update_escape();
            case GAME_OVER:
                update_gameover();
        }
    }

    public void updateState(GameState_t newState) {
        prevState = state;
        state = newState;
        switch(newState) {
            case INIT_MAIN_MENU:
                // Remove previous views
                //   (needed because there is no easy way to simply erase everything in a JFrame, or re-init a JFrame
                //     without having it close and re-open - nice.jpg)
                if(prevState == GameState_t.PAUSED) {
                    frame.getLayeredPane().remove(pauseMenu.getView());
                    frame.getLayeredPane().remove(hud.getView());
                }
                if(prevState == GameState_t.ESCAPE) {
                    frame.getLayeredPane().remove(escapeMenu.getView());
                    frame.getLayeredPane().remove(hud.getView());
                }
                frame.getContentPane().removeAll();
                frame.repaint();
                frame.revalidate();
                break;

            case INIT_NORMAL_GAME:
                if(prevState == GameState_t.MAIN_MENU) {
                    frame.getContentPane().removeAll();
                    frame.repaint();
                    frame.revalidate();
                }
                break;

            case NORMAL_GAME:
                if(prevState == GameState_t.PAUSED) {
                    frame.getLayeredPane().remove(pauseMenu.getView());
                    frame.repaint();
                    frame.revalidate();
                }
                if(prevState == GameState_t.ESCAPE) {
                    frame.getLayeredPane().remove(escapeMenu.getView());
                    frame.repaint();
                    frame.revalidate();
                }
                break;

            case PAUSED:
                pauseMenu = new MenuPauseController(this);
                frame.getLayeredPane().add(pauseMenu.getView(), new Integer(3));
                frame.repaint();
                break;

            case ESCAPE:
                escapeMenu = new MenuEscapeController(this, character.model);
                frame.getLayeredPane().add(escapeMenu.getView(), new Integer(3));
                frame.repaint();
                break;
            default:
                break;
        }
    }

    private void init_main_menu() {
        frame.setCursor(defaultCursor);
        frame.add(new MenuMainView(this));
        frame.repaint();
        frame.revalidate();
        updateState(GameState_t.MAIN_MENU);
    }

    private void init_normalgame() {
        MapReader mapReader;
        try {
            mapReader = new MapReader("src/maps/demomap.tmx");
        } catch (IOException | InvalidMapException ex) {
            // Invalid (or incorrect path to) map
            return;
        }

        frame.addKeyListener(new KeyController());

        boxManager = new BoxManager(mapReader.getWorld());

        world = new WorldController(this, mapReader.getWorld());
        character = new PlayerController(new Point(1100,500), boxManager, sound);
        sound = new SoundController(character.model);
        hud = new HUDController(this, character.model);
        enamyManager = new EnemyManager(world, mapReader.getWorld(), character.boxController, boxManager, sound);
        itemManager = new ItemManager(world, mapReader.getWorld(), boxManager, character.model);

        ImageIcon icon = new ImageIcon("src/res/icons/app_icon.png");
        frame.setIconImage(icon.getImage());

        frame.setTitle("Crypt Looter");

        camera = new CameraController(mapReader.getWorld(), world, character, screenSize);

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().add(character.attackController[0]);
        world.getView().add(character.attackController[1]);
        world.getView().add(character.attackController[2]);
        for(int x = 0; x < mapReader.getWorld().mapSize.width; x++){
            for (int y = 0; y < mapReader.getWorld().mapSize.height; y++) {
                try {
                    world.getView().add(boxManager.colliders[x][y].getView());
                } catch (NullPointerException e){
                    continue;
                }
            }
        }

        frame.getLayeredPane().add(hud.getView(), new Integer(1));

        frame.repaint();
        // Needed after adding components to frame
        frame.revalidate();

	    sound.playBackgroundMusic();

        // Go to normal game state
        updateState(GameState_t.NORMAL_GAME);
    }

    private void update_normalgame() {
        // Hide cursor
        frame.setCursor(blankCursor);

        world.deltaTime = this.deltaTime;
        character.deltaTime = this.deltaTime;
        camera.deltaTime = this.deltaTime;
        sound.deltaTime = this.deltaTime;
        enamyManager.deltaTime = this.deltaTime;

        character.update();
        boxManager.update();
        world.update();
        camera.update();
        enamyManager.update();
        itemManager.update();
        sound.update();
    }

    private void update_paused() {
        // Hide cursor
        frame.setCursor(defaultCursor);

        pauseMenu.update();
    }

    private void update_escape() {
        // Hide cursor
        frame.setCursor(defaultCursor);

        escapeMenu.update();
    }

    private void update_gameover(){

    }
}
