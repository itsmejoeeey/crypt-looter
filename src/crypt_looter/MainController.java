package crypt_looter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class MainController {
    public double deltaTime = 0;
    public Dimension screenSize = new Dimension(1440, 900);

    JFrame frame;

    CharacterModel playerModel;

    // World objects
    WorldController world;
    CameraController camera;
    PlayerController character;
    EnemyManager enemyManager;
    BoxManager boxManager;
    SoundController sound;
    ItemManager itemManager;
    ProjectileManager projectileManager;

    // UI
    HUDController hud;
    MenuPauseController pauseMenu;
    MenuEscapeController escapeMenu;
    MenuGameoverController gameOverMenu;
    MenuHighScoreController highScoresMenu;

    HighScoreController highScoreController;

    public boolean defaultMapLoaded;
    public String mapToLoad;
    private boolean mapExternalOrigin;

    public static String[] defaultMaps = {
            "maps/outdoorlevel.tmx",
            "maps/cryptlevel.tmx",
            "maps/bosslevel.tmx"

    };

    int currentMap = 0;
    final Point[][] levelControl = {
            {null,                  new Point(28,1)},
            {new Point(17,34),  new Point(17,2)},
            {new Point(17,34),  null}
    };
    final Point[] prevLevelSpawn = {
            new Point(28, 2), new Point(17, 3)
    };

    enum GameState_t {
        PAUSED, INIT_MAIN_MENU, MAIN_MENU, NORMAL_GAME, INIT_NORMAL_GAME, ESCAPE, GAME_OVER, HIGH_SCORES, INIT_CUSTOM_GAME
    }

    GameState_t state = GameState_t.INIT_MAIN_MENU;
    GameState_t prevState = GameState_t.INIT_MAIN_MENU;

    // Cursors
    Cursor blankCursor;
    Cursor defaultCursor;

    public MainController() {
        mapToLoad = defaultMaps[0];
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Add some extra pixels to account for titlebar and window border
        frame.setSize(screenSize.width + 10, screenSize.height + 37);

        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(null);

        // Set frame icon
        BufferedImage iconImage;
        try {
            iconImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("icons/app_icon.png"));
        } catch (IOException ex) {
            return;
        }
        frame.setIconImage(iconImage);

        // Set frame title
        frame.setTitle("Crypt Looter");

        // Set frame background (visible when using maps smaller than screen size)
        frame.getContentPane().setBackground(Color.BLACK);

        blankCursor = frame.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), null);
        defaultCursor = Cursor.getDefaultCursor();

        highScoreController = new HighScoreController();
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
                init_game();
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
                // No tasks required
                break;
            case HIGH_SCORES:
                // No tasks required;
                break;
            case INIT_CUSTOM_GAME:
                init_customgame();
                init_game();
        }
    }

    public void updateState(GameState_t newState) {
        prevState = state;
        state = newState;
        if(prevState == GameState_t.NORMAL_GAME){
            sound.stopAll();
        }
        if(newState == GameState_t.NORMAL_GAME){
            sound.playBackgroundMusic();
        }
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
                if(prevState == GameState_t.GAME_OVER) {
                    frame.getLayeredPane().remove(gameOverMenu.getView());
                    frame.getLayeredPane().remove(hud.getView());
                }
                if(prevState == GameState_t.HIGH_SCORES) {
                    frame.getLayeredPane().remove(highScoresMenu.getView());
                }
                frame.getContentPane().removeAll();
                frame.repaint();
                frame.revalidate();
                break;

            case INIT_NORMAL_GAME:
            case INIT_CUSTOM_GAME:
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
            case GAME_OVER:
                frame.setCursor(defaultCursor);
                gameOverMenu = new MenuGameoverController(this, character.model, highScoreController);
                frame.getLayeredPane().add(gameOverMenu.getView(), new Integer(4));
                frame.repaint();
                break;
            case HIGH_SCORES:
                highScoresMenu = new MenuHighScoreController(this, highScoreController.highScores);
                frame.getLayeredPane().add(highScoresMenu.getView(), new Integer(5));
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

    private void init_customgame() {
        defaultMapLoaded = false;
        playerModel = new CharacterModel(new Rectangle(20, 20, 50, 50), 2);
        mapExternalOrigin = true;
    }

    private void init_normalgame() {
        defaultMapLoaded = true;
        currentMap = 0;
        playerModel = new CharacterModel(new Rectangle(20, 20, 50, 50), 2);
        mapToLoad = defaultMaps[0];
        mapExternalOrigin = false;
    }

    private void init_game() {
        MapReader mapReader;
        try {
            mapReader = new MapReader(mapToLoad, mapExternalOrigin);
        } catch (IOException | InvalidMapException ex) {
            // Invalid (or incorrect path to) map
            System.out.println("INVALID_MAP_LOADED");
            JOptionPane.showMessageDialog(frame,
                    "Please select a valid map file.\n" +
                            "Crypt Looter supports map files made with the Tiled Map Editor.",
                    "Invalid Map Loaded",
                    JOptionPane.ERROR_MESSAGE);
            this.updateState(GameState_t.MAIN_MENU);
            return;
        }

        // Clear the screen (primarily the main menu)
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.revalidate();

        // Rough hack to not show the game screen until we suspect the game will be loaded
        JPanel blackScreen = new JPanel();
        blackScreen.setLayout(null);
        blackScreen.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
        blackScreen.setBackground(Color.BLACK);
        frame.getLayeredPane().add(blackScreen, new Integer(99));
        playerModel.canMove = false;
        //
        Timer loadingTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerModel.canMove = true;
                frame.getLayeredPane().remove(blackScreen);

                // Hide cursor
                frame.setCursor(blankCursor);

                frame.repaint();
                frame.revalidate();
                // Stop the timer before it loops
                ((Timer)e.getSource()).stop();
            }
        });
        loadingTimer.start();

        frame.addKeyListener(new KeyController());

        World map = mapReader.getWorld();

        playerModel.setWorld(map.spawnX * map.tileSize, map.spawnY * map.tileSize);

        world = new WorldController(this, map);
        boxManager = new BoxManager(map, world);
        projectileManager = new ProjectileManager(world, boxManager, new Dimension(map.mapSize.width * map.tileSize, map.mapSize.height * map.tileSize));
        character = new PlayerController(this, playerModel.baseTransform.getLocation(), boxManager, sound, projectileManager, playerModel);
        sound = new SoundController(this, playerModel);
        hud = new HUDController(this, playerModel);
        enemyManager = new EnemyManager(world, map, boxManager, sound, projectileManager);
        itemManager = new ItemManager(world, map, boxManager, playerModel, enemyManager.getBossModels());

        camera = new CameraController(map, world, character, screenSize);

        frame.add(world.getView());
        world.getView().add(character.getView());
        world.getView().setComponentZOrder(character.getView(), 0);
        world.getView().add(character.attackController[0]);
        world.getView().add(character.attackController[1]);
        world.getView().add(character.attackController[2]);

        for(int x = 0; x < map.mapSize.width; x++){
            for (int y = 0; y < map.mapSize.height; y++) {
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
        world.deltaTime = this.deltaTime;
        character.deltaTime = this.deltaTime;
        camera.deltaTime = this.deltaTime;
        sound.deltaTime = this.deltaTime;
        enemyManager.deltaTime = this.deltaTime;
        projectileManager.deltaTime = this.deltaTime;

        boxManager.update();
        character.update();
        enemyManager.update();
        projectileManager.update();
        world.update();
        camera.update();
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



    /*
        Only deals with manipulating teleporting levels for the default included maps
     */
    public void nextLevel() {
        if(currentMap < 2) {
            currentMap++;

            mapToLoad = defaultMaps[currentMap];

            frame.getLayeredPane().remove(hud.getView());
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.revalidate();

            if(sound != null){
                sound.stopAll();
            }
            init_game();
        }

    }
    public void previousLevel() {
        if(currentMap > 0) {
            currentMap--;

            mapToLoad = defaultMaps[currentMap];

            frame.getLayeredPane().remove(hud.getView());
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.revalidate();

            if(sound != null){
                sound.stopAll();
            }
            init_game();

            character.setPos(new Point(prevLevelSpawn[currentMap].x * world.world.tileSize, prevLevelSpawn[currentMap].y * world.world.tileSize));
        }
    }
}
