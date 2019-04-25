import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class MainController {
    public double deltaTime = 0;

    JFrame frame = new JFrame();

    WorldController world;
    CameraController camera;
    CharacterController character;
    EnemyController enemy;
    BoxManager boxManager;
    HUDController hud;
    public Dimension screenSize = new Dimension(1440, 900);

    MenuPauseController pauseMenu;
    MenuEscapeController escapeMenu;

    enum GameState_t {
        PAUSED, MAIN_MENU, NORMAL_GAME, ESCAPE
    }

    GameState_t state = GameState_t.NORMAL_GAME;
    GameState_t prevState = GameState_t.NORMAL_GAME;

    Cursor blankCursor = frame.getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB), new Point(0,0), null);
    Cursor defaultCursor = Cursor.getDefaultCursor();

    public MainController() {
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

        boxManager = new BoxManager(mapReader.getWorld());

        world = new WorldController(this, mapReader.getWorld());
        character = new CharacterController(new Point(800,500), boxManager);
        enemy = new EnemyController(new Point(1100, 500), character.boxController, boxManager);
        hud = new HUDController(this, character.model);

        ImageIcon icon = new ImageIcon("src/res/icons/app_icon.png");
        frame.setIconImage(icon.getImage());

        frame.setTitle("Crypt Looter");

        camera = new CameraController(mapReader.getWorld(), world, character, screenSize);

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().add(enemy.getView());
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
    }

    public void update() {
        switch(state) {
            case NORMAL_GAME:
                update_normalgame();
                break;
            case PAUSED:
                update_paused();
                break;
            case ESCAPE:
                update_escape();
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
                if(prevState == GameState_t.ESCAPE) {
                    frame.getLayeredPane().remove(escapeMenu.getView());
                    frame.repaint();
                    frame.revalidate();
                }
                break;

            case PAUSED:
                pauseMenu = new MenuPauseController(this);
                frame.getLayeredPane().add(pauseMenu.getView(), 2);
                frame.repaint();
                break;

            case ESCAPE:
                escapeMenu = new MenuEscapeController(this, character.model);
                frame.getLayeredPane().add(escapeMenu.getView(), 2);
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
        enemy.deltaTime = this.deltaTime;

        character.update();
        boxManager.update();
        world.update();
        camera.update();
        enemy.update();
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
}
